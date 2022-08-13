package ir.syrent.wanted.core

import ir.syrent.wanted.WPlayer
import ir.syrent.wanted.storage.Database
import ir.syrent.wanted.storage.Settings
import me.mohamad82.ruom.Ruom
import org.bukkit.entity.Player
import java.util.Optional
import java.util.UUID

object WantedManager {

    val wPlayers = HashMap<UUID, WPlayer>()

    init {
        Database.instance.getWPlayers().whenComplete { wPlayers, _ ->
            for (wPlayer in wPlayers) {
                this.wPlayers[wPlayer.uuid] = wPlayer
            }
            for (player in Ruom.getOnlinePlayers()) {
                if (!WantedManager.wPlayers.containsKey(player.uniqueId)) {
                    Database.instance.getWPlayer(player.uniqueId).whenComplete { wPlayer, _ ->
                        WantedManager.wPlayers[player.uniqueId] = wPlayer ?: WPlayer(player.uniqueId, player.name, Settings.defaultWanted)
                    }
                }
            }
        }
    }

    fun getWPlayer(player: Player): WPlayer {
        return wPlayers[player.uniqueId]!!
    }

    fun getWPlayer(user: String): Optional<WPlayer> {
        return wPlayers.values.stream().filter { it.username.lowercase() == user.lowercase() }.findFirst()
    }

    fun getWPlayer(uuid: UUID): WPlayer {
        return wPlayers[uuid]!!
    }

    fun removeWPlayer(wPlayer: WPlayer) {
        wPlayers.remove(wPlayer.uuid)
    }

    fun setWanted(uuid: UUID, wanted: String): Boolean {
        getWPlayer(uuid).apply {
            return try {
                this.wanted = Integer.parseInt(wanted)
                true
            } catch (_: NumberFormatException) {
                false
            }
        }
    }

    fun addWanted(uuid: UUID, wanted: String): Boolean {
        getWPlayer(uuid).apply {
            return try {
                this.wanted += Integer.parseInt(wanted)
                true
            } catch (_: NumberFormatException) {
                false
            }
        }
    }

    fun takeWanted(uuid: UUID, wanted: String): Boolean {
        getWPlayer(uuid).apply {
            return try {
                this.wanted -= Integer.parseInt(wanted)
                true
            } catch (_: NumberFormatException) {
                false
            }
        }
    }
}