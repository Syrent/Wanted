package ir.syrent.wanted

import org.bukkit.plugin.java.JavaPlugin

class Wanted : JavaPlugin() {

    override fun onEnable() {
        instance = this
    }

    override fun onDisable() {}

    companion object {
        var instance: Wanted? = null
            private set
    }
}