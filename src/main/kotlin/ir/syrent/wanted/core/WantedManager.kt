package ir.syrent.wanted.core

import ir.syrent.wanted.WPlayer
import ir.syrent.wanted.storage.Database
import org.bukkit.entity.Player
import java.util.UUID

class WantedManager private constructor() {

    val wPlayers = HashMap<UUID, WPlayer>()

    init {
        Database.INSTANCE.getWPlayers().whenComplete { wPlayers, _ ->
            for (wPlayer in wPlayers) {
                this.wPlayers[wPlayer.uuid] = wPlayer
            }
        }
    }

    fun getWPlayer(player: Player): WPlayer {
        return wPlayers[player.uniqueId]!!
    }

    fun getWPlayer(uuid: UUID): WPlayer {
        return wPlayers[uuid]!!
    }

    companion object {
        val INSTANCE = WantedManager()
    }
}