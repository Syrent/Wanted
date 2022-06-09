package ir.syrent.wanted.utils

import me.mohamad82.ruom.adventure.ComponentUtils
import me.mohamad82.ruom.adventure.text.Component
import org.bukkit.ChatColor
import java.util.*

fun String.capitalize(): String {
    return this.uppercase(Locale.getDefault())[0].toString() + this.lowercase(Locale.getDefault()).substring(1)
}

fun String.toComponent(): Component {
    return ComponentUtils.parse(this)
}
fun String.colorize(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}