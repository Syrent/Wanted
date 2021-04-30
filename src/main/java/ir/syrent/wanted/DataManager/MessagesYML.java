package ir.syrent.wanted.DataManager;

import ir.syrent.wanted.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessagesYML {

    private final static Main plugin = Main.getPlugin(Main.class);
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public void reloadConfig() {
        if (this.configFile == null) this.configFile = new File(plugin.getDataFolder(), "messages.yml");

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = plugin.getResource("messages.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null) reloadConfig();
        return this.dataConfig;
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) this.configFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!this.configFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
    }

}