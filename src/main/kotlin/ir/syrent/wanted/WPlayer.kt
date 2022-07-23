package ir.syrent.wanted

import me.mohamad82.ruom.Ruom
import org.bukkit.entity.Player
import java.util.*

data class WPlayer(
    var uuid: UUID,
    var username: String,
    var wanted: Int
) {

    fun getPlayer(): Optional<Player> {
        return Ruom.getOnlinePlayers().find { it.uniqueId == uuid }.let {
            if (it == null) Optional.empty()
            else Optional.of(it)
        }
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