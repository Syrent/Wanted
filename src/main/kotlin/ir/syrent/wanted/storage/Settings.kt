package ir.syrent.wanted.storage

import ir.syrent.wanted.utils.TextReplacement
import me.mohamad82.ruom.Ruom
import me.mohamad82.ruom.configuration.YamlConfig
import org.bukkit.configuration.file.FileConfiguration

object Settings {

    var settings: YamlConfig? = null
    var language: YamlConfig? = null
    private var settingsConfig: FileConfiguration? = null
    private var languageConfig: FileConfiguration? = null

    private val messages = mutableMapOf<Message, String>()

    var defaultWanted = 0
    var defaultLanguage = "en_US"

    init {
        load()
    }

    fun load() {
        settings = YamlConfig(Ruom.getPlugin().dataFolder, "settings.yml")
        settingsConfig = settings?.config

        defaultWanted = settingsConfig?.getInt("default_wanted") ?: 0
        defaultLanguage = settingsConfig?.getString("default_language") ?: "en_US"

        language = YamlConfig(Ruom.getPlugin().dataFolder, "languages/$defaultLanguage.yml")
        languageConfig = language?.config

        messages.apply {
            this.clear()
            for (message in Message.values()) {
                if (message == Message.EMPTY) {
                    this[message] = ""
                    continue
                }

                this[message] = languageConfig?.getString(message.path) ?: languageConfig?.getString(Message.UNKNOWN_MESSAGE.path) ?: "Cannot find message: ${message.name}"
            }
        }

        settings?.saveConfig()
        settings?.reloadConfig()
        language?.saveConfig()
        language?.reloadConfig()
    }

    fun formatMessage(message: Message, vararg replacements: TextReplacement): String {
        var formattedMessage = getMessage(message)
            .replace("\$prefix", getMessage(Message.PREFIX))
            .replace("\$successful_prefix", getMessage(Message.SUCCESSFUL_PREFIX))
            .replace("\$warn_prefix", getMessage(Message.WARN_PREFIX))
            .replace("\$error_prefix", getMessage(Message.ERROR_PREFIX))
        for (replacement in replacements) {
            formattedMessage = formattedMessage.replace("\$${replacement.from}", replacement.to)
        }
        return formattedMessage
    }

    private fun getMessage(message: Message): String {
        return messages[message] ?: messages[Message.UNKNOWN_MESSAGE]?.replace("\$error_prefix", messages[Message.ERROR_PREFIX] ?: "") ?: "Unknown message $message"
    }

    fun getConsolePrefix(): String {
        return getMessage(Message.CONSOLE_PREFIX)
    }
}