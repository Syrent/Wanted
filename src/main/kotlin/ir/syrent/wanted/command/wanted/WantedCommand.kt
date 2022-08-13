package ir.syrent.wanted.command.wanted

import ir.syrent.wanted.command.PluginCommand
import ir.syrent.wanted.storage.Message
import ir.syrent.wanted.utils.TextReplacement
import ir.syrent.wanted.utils.getWPlayer
import ir.syrent.wanted.utils.sendMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class WantedCommand : PluginCommand("wanted", "wanted.command.wanted", true) {
    init {
        this.register()

        addSubcommand(ReloadSubCommand())
        addSubcommand(AddSubCommand())
        addSubcommand(SetSubCommand())
        addSubcommand(TakeSubCommand())
        addSubcommand(GetSubCommand())
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender as Player
        sender.sendMessage(Message.WANTED_USE, TextReplacement("wanted_count", sender.getWPlayer().wanted.toString()))
    }
}