package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.WantedManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void getWanted(PlayerDeathEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.Enable")) return;

        Player killer = event.getEntity().getKiller();
        Player victim = event.getEntity();

        if (killer != null) {
            int wanted = WantedManager.getInstance().getWanted(killer);
            int maximum = Main.getInstance().getConfig().getInt("Wanted.Maximum");

            for (PermissionAttachmentInfo permissionList : victim.getEffectivePermissions()) {
                if (killer.hasPermission("wanted.bypass")) break;
                String wantedPermission = permissionList.getPermission();

                if (wantedPermission.contains("wanted") && wantedPermission.contains("receive")) {
                    String[] permissionSplit = wantedPermission.split("\\.");
                    int number = Integer.parseInt(permissionSplit[2]);
                    if (WantedManager.getInstance().getWanted(victim) == 0)
                        WantedManager.getInstance().setWanted(victim, 0);

                    if (!Main.getInstance().skullBuilder.cache.containsKey(killer)) {
                        Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());
                    }

                    WantedManager.getInstance().setWanted(killer, (wanted + number));
                } else {
                    if (!Main.getInstance().skullBuilder.cache.containsKey(killer)) {
                        Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());
                    }

                    WantedManager.getInstance().addWanted(killer, Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill.Player.Receive"));
                }

                Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Main.getInstance().messages.logger(event));
                break;
            }
        }
    }
}