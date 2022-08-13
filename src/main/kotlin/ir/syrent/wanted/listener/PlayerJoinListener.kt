package ir.syrent.wanted.listener

import ir.syrent.wanted.WPlayer
import ir.syrent.wanted.core.WantedManager
import ir.syrent.wanted.storage.Database
import ir.syrent.wanted.storage.Settings
import me.mohamad82.ruom.Ruom
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener: Listener {

    init {
        Ruom.registerListener(this)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (!WantedManager.wPlayers.containsKey(player.uniqueId)) {
            Database.instance.getWPlayer(player.uniqueId).whenComplete { wPlayer, _ ->
                WantedManager.wPlayers[player.uniqueId] = wPlayer ?: WPlayer(player.uniqueId, player.name, Settings.defaultWanted)
            }
        }
    }
}