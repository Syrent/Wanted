package ir.syrent.wanted.command

import me.mohamad82.ruom.adventure.AdventureApi
import me.mohamad82.ruom.kotlinextensions.component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WantedCommand : CommandExecutor {

    override fun onCommand(player: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (player !is Player) {
            AdventureApi.get().sender(player).sendMessage("<gradient:dark_red:red>Only players can use this command.".component())
            return true
        }

        return false
    }
}