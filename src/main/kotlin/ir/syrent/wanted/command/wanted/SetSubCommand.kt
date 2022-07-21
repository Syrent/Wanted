package ir.syrent.wanted.command.wanted

import ir.syrent.wanted.command.SubCommand
import ir.syrent.wanted.core.WantedManager
import ir.syrent.wanted.storage.Message
import ir.syrent.wanted.storage.Settings
import ir.syrent.wanted.utils.TextReplacement
import ir.syrent.wanted.utils.sendMessage
import me.mohamad82.ruom.Ruom
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class SetSubCommand : SubCommand("set", "wanted.command.set", false) {
    private val numbers = listOf("1", "2", "3", "4", "5")

    override fun onExecute(sender: CommandSender, args: List<String>) {
        Settings.load()
        if (args.size != 2) {
            sender.sendMessage(Message.WANTED_SET_USAGE)
            return
        }

        val target = WantedManager.getWPlayer(args[0])
        if (!target.isPresent) {
            sender.sendMessage(Message.WANTED_NO_TARGET)
            return
        }

        if (!WantedManager.setWanted(target.get().uuid, args[1])) {
            sender.sendMessage(Message.VALID_PARAMS, TextReplacement("argument", args[1]))
            return
        }

        sender.sendMessage(Message.WANTED_SET_USE, TextReplacement("player", target.get().username), TextReplacement("wanted_count", args[1]))
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        if (args.size == 1) {
            return Ruom.getOnlinePlayers().map { it.name }
        }

        if (args.size == 2) {
            return numbers
        }

        return emptyList()
    }
}