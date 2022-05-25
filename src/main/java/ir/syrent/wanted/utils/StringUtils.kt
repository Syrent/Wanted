package ir.syrent.wanted.utils

import org.bukkit.ChatColor
import java.util.*

fun String.capitalize(): String {
    return this.uppercase(Locale.getDefault())[0].toString() + this.lowercase(Locale.getDefault()).substring(1)
}

fun String.colorize(): String {
    return ChatColor.translateAlternateColorCodes('&', this)
}