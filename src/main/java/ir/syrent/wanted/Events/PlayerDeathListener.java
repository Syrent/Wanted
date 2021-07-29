package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void getWanted(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        Player victim = event.getEntity();

        if (killer != null) {


            int wanted = Main.getInstance().wantedMap.getOrDefault(killer.getName(), 0);
            int maximum = Main.getInstance().getConfig().getInt("Wanted.Maximum");

            for (PermissionAttachmentInfo permissionList : victim.getEffectivePermissions()) {
                if (killer.hasPermission("wanted.bypass")) break;
                String wantedPermission = permissionList.getPermission();

                if (wantedPermission.contains("wanted") && wantedPermission.contains("receive")) {
                    String[] permissionSplit = wantedPermission.split("\\.");
                    int number = Integer.parseInt(permissionSplit[2]);
                    if (Main.getInstance().wantedMap.get(victim.getName()) != null) {
                        Main.getInstance().wantedMap.remove(victim.getName());
                    }

                    if (!Main.getInstance().skullBuilder.cache.containsKey(killer)) {
                        Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());
                    }

                    if ((wanted + number) > maximum) {
                        Main.getInstance().wantedMap.put(killer.getName(), maximum);
                        break;
                    }

                    Main.getInstance().wantedMap.replace(killer.getName(), (wanted + number));
                } else {
                    if (!Main.getInstance().skullBuilder.cache.containsKey(killer)) {
                        Main.getInstance().skullBuilder.cache.put(killer, killer.serialize());
                    }

                    if ((wanted + 1) > maximum) {
                        Main.getInstance().wantedMap.put(killer.getName(), maximum);
                        break;
                    }

                    if (wanted == 0) {
                        Main.getInstance().wantedMap.put(killer.getName(), (wanted + Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill")));
                    }

                    Main.getInstance().wantedMap.replace(killer.getName(), (wanted + Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill")));
                }
                Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Main.getInstance().messages.logger(event));
                break;
            }
        }
    }
}