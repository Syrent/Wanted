package ir.syrent.wanted.Events;

import ir.syrent.wanted.Core.WPlayer;
import ir.syrent.wanted.Core.Wanted;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.Messages.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class DeathEvent implements Listener {

    private final Wanted plugin = Wanted.getPlugin(Wanted.class);
    Log log = new Log();
    Messages messages = new Messages();
    WPlayer wPlayer = new WPlayer();

    @EventHandler
    public void getWanted(PlayerDeathEvent event) {
        wPlayer.setKiller(event.getEntity().getKiller());
        wPlayer.setVictim(event.getEntity());

        if ((wPlayer.getKiller() instanceof Player)) {

            if (wPlayer.getKiller().hasPermission("wanted.reset")) {
                if (!(plugin.getSetWanted().get(wPlayer.getVictim().getName()) == null)) {
                    plugin.getSetWanted().remove(wPlayer.getVictim().getName());
                    return;
                }
            }

            log.logToFile(log.logTime(), messages.logger(event));

            int wanted = plugin.getSetWanted().get(wPlayer.getKiller().getName());
            int maximum = plugin.getConfig().getInt("Wanted.Maximum");

            for (PermissionAttachmentInfo permissionList : wPlayer.getVictim().getEffectivePermissions()) {
                if (wPlayer.getKiller().hasPermission("wanted.bypass")) break;
                String wantedPermission = permissionList.getPermission();
                if (wantedPermission.contains("wanted") && wantedPermission.contains("receive")) {
                    String[] permissionSplit = wantedPermission.split("\\.");
                    int number = Integer.parseInt(permissionSplit[2]);
                    if (!(plugin.getSetWanted().get(wPlayer.getVictim().getName()) == null)) {
                        plugin.getSetWanted().remove(wPlayer.getVictim().getName());
                    }
                    if ((wanted + number) > maximum) {
                        plugin.getSetWanted().put(wPlayer.getKiller().getName(), maximum);
                        break;
                    }
                    plugin.getSetWanted().replace(wPlayer.getKiller().getName(), (wanted + number));
                    break;
                } else {
                    if ((wanted + 1) > maximum) {
                        plugin.getSetWanted().put(wPlayer.getKiller().getName(), maximum);
                        break;
                    }
                    plugin.getSetWanted().replace(wPlayer.getKiller().getName(), (wanted + plugin.getConfig().getInt("Wanted.ReceiveOnKill")));
                }
            }
        }
    }
}