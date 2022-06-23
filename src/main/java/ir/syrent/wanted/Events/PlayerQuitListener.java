package ir.syrent.wanted.Events;

import ir.syrent.wanted.Utils.SkullBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        //Removing player from cache to avoid memory leaks
        SkullBuilder.getInstance().cache.remove(player);
    }

}
