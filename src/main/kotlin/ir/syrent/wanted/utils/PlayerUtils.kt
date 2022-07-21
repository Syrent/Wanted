package ir.syrent.wanted.utils

import ir.syrent.wanted.WPlayer
import ir.syrent.wanted.core.WantedManager
import ir.syrent.wanted.storage.Message
import ir.syrent.wanted.storage.Settings
import me.mohamad82.ruom.adventure.AdventureApi
import me.mohamad82.ruom.kotlinextensions.component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun Player.getWPlayer(): WPlayer {
    return WantedManager.getWPlayer(this.uniqueId)
}
fun CommandSender.sendMessage(message: Message, vararg replacements: TextReplacement) {
    AdventureApi.get().sender(this).sendMessage(Settings.formatMessage(message, *replacements).component())
}

fun Player.sendMessage(message: Message, vararg replacements: TextReplacement) {
    AdventureApi.get().sender(this).sendMessage(Settings.formatMessage(message, *replacements).component())
}