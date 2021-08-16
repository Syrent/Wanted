package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
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

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void getWanted(PlayerDeathEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.Enable")) return;

        Player victim = event.getEntity();
        if (Bukkit.getPluginManager().getPlugin("Citizens") != null)
            if (CitizensAPI.getNPCRegistry().isNPC(victim)) return;
        Player killer = event.getEntity().getKiller();

        Wanted.getInstance().runCommand(killer, victim, "Player");

        if (Main.getInstance().getConfig().getBoolean("Wanted.ClearWantedOnDeath")) {
            if (killer == null) {
                if (WantedManager.getInstance().getWanted(victim) != 0)
                    WantedManager.getInstance().setWanted(victim, 0);
                Main.getInstance().skullBuilder.cache.remove(victim);
                return;
            }
        }

        int finalWanted;
        int wanted = WantedManager.getInstance().getWanted(killer);

        Player fightStarter = Main.getInstance().playerDamagedMap.containsKey(victim) ?
                Main.getInstance().playerDamagedMap.get(victim) : Main.getInstance().playerDamagedMap.get(killer);

        for (PermissionAttachmentInfo permissionList : victim.getEffectivePermissions()) {
            if (killer.hasPermission("wanted.bypass")) break;
            if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.PreventReceiveIfDefender"))
                if (!killer.equals(fightStarter)) break;
            String wantedPermission = permissionList.getPermission();

            if (wantedPermission.contains("wanted") && wantedPermission.contains("receive")) {
                String[] permissionSplit = wantedPermission.split("\\.");
                int number = Integer.parseInt(permissionSplit[2]);
                if (WantedManager.getInstance().getWanted(victim) != 0)
                    WantedManager.getInstance().setWanted(victim, 0);

                Main.getInstance().skullBuilder.cache.remove(victim);

                if (!Main.getInstance().skullBuilder.cache.containsKey(killer))
                    Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());

                WantedManager.getInstance().setWanted(killer, (wanted + number));
                finalWanted = wanted + number;
            } else {
                if (!Main.getInstance().skullBuilder.cache.containsKey(killer))
                    Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());

                int defaultReceive = Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill.Player.Receive");
                WantedManager.getInstance().addWanted(killer, defaultReceive);
                finalWanted = defaultReceive;
            }

            if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.KillMessage")) {
                killer.sendMessage(Main.getInstance().messages.getMessageOnKillPlayer()
                        .replace("%player_name%", victim.getName()).replace("%wanted%", String.valueOf(finalWanted))
                        .replace("%fight_starter%", fightStarter == null ? "UNKNOWN" : fightStarter.getName()));
            }
            break;
        }

        if (Main.getInstance().playerDamagedMap.containsKey(killer))
            Main.getInstance().playerDamagedMap.remove(killer);
        else Main.getInstance().playerDamagedMap.remove(victim);

        Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Main.getInstance().messages.playerDeathLogger(event));
    }
}