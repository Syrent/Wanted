package ir.syrent.wanted

import ir.syrent.wanted.command.wanted.WantedCommand
import ir.syrent.wanted.core.WantedManager
import ir.syrent.wanted.listener.PlayerJoinListener
import ir.syrent.wanted.listener.PlayerQuitListener
import ir.syrent.wanted.storage.Database
import ir.syrent.wanted.storage.Settings
import me.mohamad82.ruom.RUoMPlugin
import me.mohamad82.ruom.Ruom
import me.mohamad82.ruom.adventure.AdventureApi
import me.mohamad82.ruom.kotlinextensions.component
import me.mohamad82.ruom.utils.ServerVersion


class Wanted : RUoMPlugin() {

    override fun onEnable() {
        instance = this

        Database
        Settings
        WantedManager

        Ruom.initializeAdventure()
        initialize()
    }

    override fun onDisable() {
        Database.instance.shutdown()
        Ruom.log("Database closed.")
        Ruom.shutdown()
    }

    private fun initialize() {
        sendFiglet()
        checkVersion()
        checkData()
        registerListeners()
        registerCommands()
    }

    private fun sendFiglet() {
        val list = ArrayList<String>()
        list.add("<gradient:dark_red:red> _       __            __           __")
        list.add("<gradient:dark_red:red>| |     / /___ _____  / /____  ____/ /")
        list.add("<gradient:dark_red:red>| | /| / / __ `/ __ \\/ __/ _ \\/ __  / ")
        list.add("<gradient:dark_red:red>| |/ |/ / /_/ / / / / /_/  __/ /_/ /  ")
        list.add("<gradient:dark_red:red>|__/|__/\\__,_/_/ /_/\\__/\\___/\\__,_/   ")
        list.add("                                      ")

        for (text in list) {
            AdventureApi.get().console().sendMessage(text.component())
        }
    }

    /**
     * Disable plugin if the server is legacy
     */
    private fun checkVersion() {
        if (!ServerVersion.supports(16)) {
            AdventureApi.get().console().sendMessage("<dark_red>Plugin only supports 1.16-1.19".component())
            AdventureApi.get().console().sendMessage("<dark_red>Plugin only supports 1.16-1.19".component())
            AdventureApi.get().console().sendMessage("<dark_red>Plugin only supports 1.16-1.19".component())
            AdventureApi.get().console().sendMessage("<dark_red>Disabling plugin...".component())
            server.pluginManager.disablePlugin(this)
        }

        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder")
        } catch (e: ClassNotFoundException) {
            AdventureApi.get().console().sendMessage("<gradient:gold:yellow>We strongly recommend you to use PaperMC.".component())
            AdventureApi.get().console().sendMessage("<gradient:gold:yellow>Paper is the next generation of Minecraft servers, compatible with Spigot plugins, offering uncompromising performance.".component())
            AdventureApi.get().console().sendMessage("<gradient:dark_green:green>You can download it from: <gradient:dark_purple:blue>https://papermc.io/".component())
        }
    }

    /**
     * Create data folder if not exists
     */
    private fun checkData() {
        if (!dataFolder.exists()) {
            AdventureApi.get().console().sendMessage("<gradient:dark_purple:blue>Data folder not found, creating...".component())
            dataFolder.mkdir()
        }
        AdventureApi.get().console().sendMessage("<gradient:dark_purple:blue>Using ${Database.instance.type} as storage type.".component())
    }

    private fun registerListeners() {
        AdventureApi.get().console().sendMessage("<gradient:dark_purple:blue>Register listeners...".component())
        PlayerJoinListener()
        PlayerQuitListener()
    }

    private fun registerCommands() {
        AdventureApi.get().console().sendMessage("<gradient:dark_purple:blue>Register commands...".component())
        WantedCommand()
    }

    companion object {
        lateinit var instance: Wanted
            private set
    }
}