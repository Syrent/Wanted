package ir.syrent.wanted.storage

import ir.syrent.wanted.Wanted
import org.bukkit.configuration.file.FileConfiguration

class Settings {

    var defaultWanted: Int? = null

    private var settingsConfig: FileConfiguration? = null

    init {
        reload()
    }

    fun reload() {
        settingsConfig = Wanted.INSTANCE!!.settings.config

        defaultWanted = settingsConfig?.getInt("default-wanted") ?: 0
    }

    companion object {
        val INSTANCE = Settings()
    }

}