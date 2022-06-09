package ir.syrent.wanted

import ir.syrent.wanted.core.WantedManager
import ir.syrent.wanted.listener.PlayerJoinListener
import ir.syrent.wanted.listener.PlayerQuitListener
import ir.syrent.wanted.storage.Database
import ir.syrent.wanted.storage.Settings
import ir.syrent.wanted.utils.toComponent
import me.mohamad82.ruom.RUoMPlugin
import me.mohamad82.ruom.Ruom
import me.mohamad82.ruom.adventure.AdventureApi
import me.mohamad82.ruom.configuration.YamlConfig


class Wanted : RUoMPlugin() {

    val settings = YamlConfig(dataFolder, "settings.yml")

    override fun onEnable() {
        INSTANCE = this

        Database
        Settings
        WantedManager

        Ruom.initializeAdventure()
        initialize()
    }

    override fun onDisable() {
        Database.INSTANCE.shutdown()
        Ruom.log("Database closed.")
        Ruom.shutdown()
    }

    private fun initialize() {
        sendFiglet()
        checkData()
        registerListeners()
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
            AdventureApi.get().console().sendMessage(text.toComponent())
        }
    }

    private fun checkData() {
        if (!dataFolder.exists()) {
            AdventureApi.get().console().sendMessage("<gradient:dark_purple:blue>Data folder not found, creating...".toComponent())
            dataFolder.mkdir()
        }
        AdventureApi.get().console().sendMessage("<gradient:dark_purple:blue>Using ${Database.INSTANCE.type} as storage type.".toComponent())
    }

    private fun registerListeners() {
        AdventureApi.get().console().sendMessage("<gradient:dark_purple:blue>Register listeners...".toComponent())
        PlayerJoinListener()
        PlayerQuitListener()
    }

    companion object {
        val RAW_PREFIX = "[Wanted] "
        val PREFIX = "<gradient:dark_red:red><bold><it>Wanted »</it></bold></gradient><gradient:dark_purple:blue>"

        var INSTANCE: Wanted? = null
            private set
    }
}