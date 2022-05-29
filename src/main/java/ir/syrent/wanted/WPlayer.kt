package ir.syrent.wanted

import me.mohamad82.ruom.Ruom
import org.bukkit.entity.Player
import java.util.*

data class WPlayer(
    val uuid: UUID,
    val username: String,
    val wanted: Int
) {

    fun getPlayer(): Optional<Player> {
        for (onlinePlayer in Ruom.getOnlinePlayers()) {
            if (onlinePlayer.uniqueId == uuid) {
                return Optional.of(onlinePlayer)
            }
        }
        return Optional.empty()
    }

    companion object {
        class Builder {
            var uuid: UUID? = null
            var username: String? = null
            var wanted: Int? = null

            fun uuid(uuid: UUID) = apply { this.uuid = uuid }
            fun username(username: String) = apply { this.username = username }
            fun wanted(wanted: Int) = apply { this.wanted = wanted }

            fun build(): WPlayer {
                return WPlayer(
                    uuid = uuid!!,
                    username = username!!,
                    wanted = wanted!!
                )
            }
        }
    }
}