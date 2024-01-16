package ir.syrent.wanted.Events;

import ir.syrent.wanted.Commands.Permissions;
import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.Utils;
import ir.syrent.wanted.Wanted;
import ir.syrent.wanted.WantedManager;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void getWanted(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (victim.isDead()) {
                int unchangedWanted = WantedManager.getInstance().getWanted(victim.getName());

                if (Main.getInstance().getConfig().getBoolean("Wanted.ClearWantedOnDeath")) {
                    if (WantedManager.getInstance().getWanted(victim.getName()) != 0)
                        WantedManager.getInstance().setWanted(victim.getName(), 0);
                    Main.getInstance().skullBuilder.cache.remove(victim);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        WantedCommand.getTarget.remove(player);
                        if (WantedCommand.playerBossBarHashMap.containsKey(player))
                            WantedCommand.playerBossBarHashMap.get(player).removePlayer(player);
                        WantedCommand.playerBossBarHashMap.remove(player);
                    }

                    if (Main.getInstance().getConfig().getBoolean("Wanted.Log")) {
                        Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Messages.playerDeathLogger(event));
                    }
                }

                if (Main.getInstance().getConfig().getBoolean("Wanted.ComplaintMode.Enable")) return;
                if (!Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.Enable")) return;

                if (Main.getInstance().getConfig().getBoolean("Wanted.WorldGuard.Enable") && Main.worldGuardFound) {
                    List<String> regions = Main.getInstance().getConfig().getStringList("Wanted.WorldGuard.BlacklistRegions");

                    if (Main.getInstance().getConfig().getBoolean("Wanted.WorldGuard.RevertBlacklist")) {
                        if (!Main.worldGuard.isRegionBlacklisted(regions, victim.getLocation())) return;
                    }
                    if (Main.worldGuard.isRegionBlacklisted(regions, victim.getLocation())) return;
                }

                if (Bukkit.getPluginManager().getPlugin("Citizens") != null)
                    if (CitizensAPI.getNPCRegistry().isNPC(victim)) return;
                Player killer = victim.getKiller();
                if (killer == null) return;

                Wanted.getInstance().runCommand(killer, victim, "Player");
                if (Utils.hasPermission(killer, false, Permissions.HUNTER))
                    Wanted.getInstance().runCommand(killer, victim, "Wanted", unchangedWanted);

                int finalWanted = 1;
                int wanted = WantedManager.getInstance().getWanted(killer.getName());

                String fightStarter = Main.getInstance().playerDamagedMap.containsKey(victim.getName()) ?
                        Main.getInstance().playerDamagedMap.get(victim.getName()) : Main.getInstance().playerDamagedMap.get(killer.getName());
                Main.getInstance().playerVictimMap.put(killer.getName(), victim.getName());

                for (PermissionAttachmentInfo permissionList : victim.getEffectivePermissions()) {
                    if (Utils.hasPermission(killer, false, Permissions.BYPASS)) break;
                    if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.PreventReceiveIfDefender"))
                        if (!killer.getName().equals(fightStarter)) break;
                    String wantedPermission = permissionList.getPermission();

                    if (wantedPermission.contains("wanted") && wantedPermission.contains("receive")) {
                        String[] permissionSplit = wantedPermission.split("\\.");
                        int number = Integer.parseInt(permissionSplit[2]);
                        if (WantedManager.getInstance().getWanted(victim.getName()) != 0)
                            WantedManager.getInstance().setWanted(victim.getName(), 0);

                        Main.getInstance().skullBuilder.cache.remove(victim);

                        if (!Main.getInstance().skullBuilder.cache.containsKey(killer))
                            Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());

                        WantedManager.getInstance().setWanted(killer.getName(), (wanted + number));
                        finalWanted = wanted + number;
                    } else {
                        if (!Main.getInstance().skullBuilder.cache.containsKey(killer))
                            Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());

                        int defaultReceive = Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill.Player.Receive");
                        WantedManager.getInstance().addWanted(killer, defaultReceive);
                        finalWanted = defaultReceive;
                    }

                    break;
                }

                if (Main.getInstance().playerDamagedMap.containsKey(killer.getName()))
                    Main.getInstance().playerDamagedMap.remove(killer.getName());
                else Main.getInstance().playerDamagedMap.remove(victim.getName());

                if (Main.getInstance().getConfig().getBoolean("Wanted.ClearWantedOnDeath")) {
                    if (Main.getInstance().getConfig().getBoolean("Wanted.RemoveWantedOnlyIfKilledByHunter")
                            && !Utils.hasPermission(killer, false, Permissions.HUNTER)) return;

                    if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.KillMessage")) {
                        killer.sendMessage(Messages.ON_KILL_PLAYER
                                .replace("%player_name%", victim.getName()).replace("%wanted%", String.valueOf(finalWanted))
                                .replace("%fight_starter%", fightStarter == null ? "UNKNOWN" : fightStarter)
                                .replace("%region%", !Main.worldGuardFound ?
                                        "UNKNOWN" : Main.worldGuard.getRegionName(victim.getLocation()) == null ?
                                        "UNKNOWN" : Main.worldGuard.getRegionName(victim.getLocation()))
                        );
                    }
                }
            }
        }, 1);
    }
}