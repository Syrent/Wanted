package ir.syrent.wanted.command.wanted

import ir.syrent.wanted.command.SubCommand
import ir.syrent.wanted.core.WantedManager
import ir.syrent.wanted.storage.Message
import ir.syrent.wanted.storage.Settings
import ir.syrent.wanted.utils.TextReplacement
import ir.syrent.wanted.utils.sendMessage
import me.mohamad82.ruom.Ruom
import org.bukkit.command.CommandSender

class GetSubCommand : SubCommand("get", "wanted.command.get", false) {

    override fun onExecute(sender: CommandSender, args: List<String>) {
        Settings.load()
        if (args.size != 1) {
            sender.sendMessage(Message.WANTED_GET_USAGE)
            return
        }

        val target = WantedManager.getWPlayer(args[0])
        if (!target.isPresent) {
            sender.sendMessage(Message.WANTED_NO_TARGET)
            return
        }

        sender.sendMessage(Message.WANTED_GET_USE, TextReplacement("player", target.get().username), TextReplacement("count", target.get().wanted.toString()))
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        if (args.size == 1) {
            return Ruom.getOnlinePlayers().map { it.name }
        }

        return emptyList()
    }
}