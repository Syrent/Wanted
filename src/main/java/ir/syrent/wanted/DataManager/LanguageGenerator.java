package ir.syrent.wanted.DataManager;

import ir.syrent.wanted.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LanguageGenerator {

    private FileConfiguration dataConfig = null;
    private File configFile;
    private final String path;
    private final String name;

    public LanguageGenerator(String path, String name) {
        this.path = path;
        this.name = name;
        this.configFile = new File(path, name + ".yml");
    }

    public void reloadConfig() {
        if (this.configFile == null) this.configFile = new File(path, name + ".yml");

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = Main.getInstance().getResource("language/" + name  + ".yml");
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
        if (this.configFile == null) this.configFile = new File(path, name + ".yml");

        if (!this.configFile.exists()) {
            Main.getInstance().saveResource("language/" + name + ".yml", false);
        }
    }

}