package ir.syrent.wanted.Events;

import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.WantedManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (WantedManager.getInstance().getWanted(player.getName()) != 0) {
            SkullBuilder.getInstance().cache.remove(player);
        }
    }
}
