package ir.syrent.wanted.Messages;

import ir.syrent.wanted.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Localizations {

    public void generateFile() {
        if (Main.getInstance().getConfig().getConfigurationSection("Wanted") == null)
            Main.getInstance().getConfig().createSection("Wanted");
        addDefault("Language", "en_US");
        addDefault("Maximum_Wanted", "5");
        addDefault("Remove_Only_If_Killed_By_Hunter", "false");
        addDefault("Clear_On_Death", "true");

        if (Main.getInstance().getConfig().getConfigurationSection("Compass") == null)
            Main.getInstance().getConfig().createSection("Compass");
        addDefault("Compass", "Refresh_Interval", "20");
        addDefault("Compass", "Stop_Finding_After_Death", "true");

        if (Main.getInstance().getConfig().getConfigurationSection("Arrest") == null)
            Main.getInstance().getConfig().createSection("Arrest");
        addDefault("Arrest", "Enable", "true");
        addDefault("Arrest", "Prevent_Self_Arrest", "true");
        addDefault("Arrest", "Distance", "10");

        if (Main.getInstance().getConfig().getConfigurationSection("Arrest").getConfigurationSection("Commands") == null)
            Main.getInstance().getConfig().createSection("Arrest.Commands");
        List<String> commands = new ArrayList<>();
        commands.add("PLAYER;spawn");
        commands.add("VICTIM;spawn");
        commands.add("CONSOLE;give %player% diamond %wanted_level%");
        commands.add("CONSOLE;say %player% arrested %victim%");
        addList("Wanted", "Arrest.Command", commands);
    }

    public void addDefault(FileConfiguration file, String key, String value) {
        file.addDefault(key, value);
    }

    public void addDefault(String section, String key, String value) {
        Main.getInstance().getConfig().addDefault(section + "." + key, value);
    }

    public void addList(String section, String key, List<String> values) {
        Main.getInstance().getConfig().getStringList(section + "." + key).addAll(values);
    }

    public void addDefault(String key, String value) {
        Main.getInstance().getConfig().addDefault("Wanted." + key, value);
    }
}
