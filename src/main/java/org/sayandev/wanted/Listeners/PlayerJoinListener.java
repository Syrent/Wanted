package org.sayandev.wanted.Listeners;

import org.sayandev.wanted.Utils.SkullBuilder;
import org.sayandev.wanted.WantedManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (WantedManager.getInstance().getWanted(player.getName()) != 0) {
            SkullBuilder.getInstance().saveHead(player);
        }
    }
}
