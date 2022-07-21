package ir.syrent.wanted.command.wanted

import ir.syrent.wanted.command.SubCommand
import ir.syrent.wanted.storage.Message
import ir.syrent.wanted.storage.Settings
import ir.syrent.wanted.utils.sendMessage
import org.bukkit.command.CommandSender

class ReloadSubCommand : SubCommand("reload", "wanted.command.reload", false) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        Settings.load()
        sender.sendMessage(Message.WANTED_RELOAD_USE)
    }
}