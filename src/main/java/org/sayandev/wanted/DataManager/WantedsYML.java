package org.sayandev.wanted.DataManager;

import org.sayandev.wanted.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class WantedsYML {
    private FileConfiguration dataConfig = null;
    private final File configFile;
    private final Main plugin;

    public WantedsYML(Main plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "wanteds.yml");
    }

    public void reloadConfig() {
        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        try {
            dataConfig = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load wanteds.yml", e);
            dataConfig = new YamlConfiguration(); // Fallback
            return;
        }

        try (InputStream defaultStream = plugin.getResource("wanteds.yml")) {
            if (defaultStream != null) {
                dataConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream)));
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load default wanteds.yml", e);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) {
            reloadConfig();
        }
        return dataConfig;
    }

    public void saveConfig() {
        if (dataConfig == null || !configFile.exists()) {
            return;
        }

        try {
            dataConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("wanteds.yml", false);
        }
    }
}