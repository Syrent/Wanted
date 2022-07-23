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
                    WantedManager.wPlayers[player.uniqueId] = WPlayer(player.uniqueId, player.name, Settings.defaultWanted)
                }
            }
        }
    }

    fun getWPlayer(player: Player): WPlayer {
        return wPlayers[player.uniqueId]!!
    }

    fun getWPlayer(user: String): Optional<WPlayer> {
        for (wPlayer in wPlayers.values) {
            Ruom.warn("Checking $user with ${wPlayer.username}")
            if (wPlayer.username.lowercase() == user.lowercase()) {
                Ruom.warn("Found player $user")
                return Optional.of(wPlayer)
            }
        }
        return Optional.empty<WPlayer>()
    }

    fun getWPlayer(uuid: UUID): WPlayer {
        return wPlayers[uuid]!!
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