package ir.syrent.wanted.listener

import ir.syrent.wanted.WPlayer
import ir.syrent.wanted.core.WantedManager
import ir.syrent.wanted.storage.Database
import ir.syrent.wanted.storage.Settings
import me.mohamad82.ruom.Ruom
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener: Listener {

    init {
        Ruom.registerListener(this)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        if (WantedManager.INSTANCE.wPlayers.containsKey(player.uniqueId))
            Database.INSTANCE.saveWPlayer(WantedManager.INSTANCE.wPlayers[player.uniqueId]!!)
    }
}