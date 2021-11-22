package ir.syrent.wanted.Events;

import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Main;
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
        if (Main.getInstance().getConfig().getBoolean("Wanted.ComplaintMode.Enable")) return;
        if (!Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.Enable")) return;

        Player victim = event.getEntity();
        if (Main.getInstance().getConfig().getBoolean("Wanted.WorldGuard.Enable") && Main.getInstance().worldGuard != null) {
            List<String> regions = Main.getInstance().getConfig().getStringList("Wanted.WorldGuard.BlacklistRegions");

            if (Main.getInstance().getConfig().getBoolean("Wanted.WorldGuard.RevertBlacklist")) {
                if (!Main.getInstance().worldGuard.isRegionBlacklisted(regions, victim.getLocation())) return;
            }
            if (Main.getInstance().worldGuard.isRegionBlacklisted(regions, victim.getLocation())) return;
        }

        if (Bukkit.getPluginManager().getPlugin("Citizens") != null)
            if (CitizensAPI.getNPCRegistry().isNPC(victim)) return;
        Player killer = victim.getKiller();
        if (killer == null) return;

        Wanted.getInstance().runCommand(killer, victim, "Player");
        if (killer.hasPermission("wanted.hunter"))
            Wanted.getInstance().runCommand(killer, victim, "Wanted");

        int finalWanted = 1;
        int wanted = WantedManager.getInstance().getWanted(killer.getName());

        String fightStarter = Main.getInstance().playerDamagedMap.containsKey(victim.getName()) ?
                Main.getInstance().playerDamagedMap.get(victim.getName()) : Main.getInstance().playerDamagedMap.get(killer.getName());
        Main.getInstance().playerVictimMap.put(killer.getName(), victim.getName());

        for (PermissionAttachmentInfo permissionList : victim.getEffectivePermissions()) {
            if (killer.hasPermission("wanted.bypass")) break;
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
                && !killer.hasPermission("wanted.hunter")) return;

            if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.KillMessage")) {
                killer.sendMessage(Main.getInstance().messages.getMessageOnKillPlayer()
                        .replace("%player_name%", victim.getName()).replace("%wanted%", String.valueOf(finalWanted))
                        .replace("%fight_starter%", fightStarter == null ? "UNKNOWN" : fightStarter)
                        .replace("%region%", Main.getInstance().worldGuard == null ?
                                "UNKNOWN" : Main.getInstance().worldGuard.getRegionName(victim.getLocation()) == null ?
                                "UNKNOWN" : Main.getInstance().worldGuard.getRegionName(victim.getLocation()))
                );
            }
            if (WantedManager.getInstance().getWanted(victim.getName()) != 0)
                WantedManager.getInstance().setWanted(victim.getName(), 0);
            Main.getInstance().skullBuilder.cache.remove(victim);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (WantedCommand.getTarget.containsKey(player)) {
                WantedCommand.getTarget.remove(player);
            }
            if (WantedCommand.playerBossBarHashMap.containsKey(player)) WantedCommand.playerBossBarHashMap.get(player).removePlayer(player);
            WantedCommand.playerBossBarHashMap.remove(player);
        }


        Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Main.getInstance().messages.playerDeathLogger(event));
    }
}