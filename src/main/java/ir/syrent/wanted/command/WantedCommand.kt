package ir.syrent.wanted.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("wanted")
class WantedCommand: BaseCommand() {

    @Default
    @Syntax("<player>")
    @CatchUnknown
    @CommandCompletion("sosis|sosis2 sosmast|sosismast2 koobs|koobs2")
    //@CommandPermission("wanted.use")
    fun onCommand(player: CommandSender, command: Command, label: String, args: Array<String>) {
        player.sendMessage("player: ${player.name}")
        player.sendMessage("command: $command")
        player.sendMessage("label: $label")
        player.sendMessage("args: $args")
    }

    @Subcommand("sosis")
    @Syntax("sosis benevis")
    @CommandCompletion("@players")
    fun onSubCommand(player: Player) {
        player.sendMessage("sosis")
    }
}