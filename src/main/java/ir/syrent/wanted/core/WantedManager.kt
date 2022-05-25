package ir.syrent.wanted.core

import ir.syrent.wanted.WPlayer
import ir.syrent.wanted.storage.Database
import java.util.UUID

class WantedManager private constructor() {
    val wPlayers = mutableMapOf<UUID, WPlayer>()

    init {
        //TODO: Add WPlayers from database
        Database.INSTANCE.getWPlayers().whenComplete { wPlayers, _ ->
            for (wPlayer in wPlayers) {
                this.wPlayers[wPlayer.uuid] = wPlayer
            }
        }

    }

    companion object {
        val INSTANCE = WantedManager()
    }
}