package ir.syrent.wanted.Core;

import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Commands.WantedsCommand;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.DataManager.MessagesYML;
import ir.syrent.wanted.DataManager.WantedsYML;
import ir.syrent.wanted.Dependencies.PlaceholderAPI;
import ir.syrent.wanted.Events.DeathEvent;
import ir.syrent.wanted.GUI.SpiGUI;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.TabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public final class Wanted extends JavaPlugin implements CommandExecutor {

    public static WantedsYML wantedsYML;
    public static MessagesYML messagesYML;
    public static Messages messages;
    public static Log log;
    public static SpiGUI spiGUI;
    private static Wanted instance;
    private final HashMap<String, Integer> setWanted = new HashMap<>();

    public Wanted() {
        instance = this;
    }

    public static SpiGUI getSpiGUI() {
        return spiGUI;
    }

    public void saveData() {
        wantedsYML = new WantedsYML();
        if (getSetWanted().isEmpty() && wantedsYML.getConfig().getConfigurationSection("wanted") == null) return;
        if (!getSetWanted().isEmpty()) {
            for (Map.Entry<String, Integer> wantedlist : getSetWanted().entrySet()) {
                wantedsYML.getConfig().set("wanted." + wantedlist.getKey(), wantedlist.getValue());
            }
            if (getConfig().getBoolean("DataSave.Notification"))
                Bukkit.getLogger().info(messages.getRawPrefix() + "Wanted data saved!");
            wantedsYML.saveConfig();
        } else {
            for (String wantedlist : wantedsYML.getConfig().getConfigurationSection("wanted").getKeys(false)) {
                getSetWanted().remove(wantedlist);
                getSetWanted().put(wantedlist, wantedsYML.getConfig().getInt("wanted." + wantedlist));
            }
        }
    }

    public void autoSaveData() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().runTaskAsynchronously(instance, () -> saveData());
            }
        }.runTaskTimer(instance, 0, 20L * 60 * getConfig().getInt("DataSave.Interval"));
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);

        getCommand("wanted").setExecutor(new WantedCommand());
        getCommand("wanteds").setExecutor(new WantedsCommand());

        getCommand("wanted").setTabCompleter(new TabCompleter());

        messages = new Messages();

        log = new Log();
        log.setupLogFolder();

        autoSaveData();

        wantedsYML = new WantedsYML();
        messagesYML = new MessagesYML();
        this.saveDefaultConfig();
        messagesYML.saveDefaultConfig();
        wantedsYML.saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }

        spiGUI = new SpiGUI(this);

        if (getConfig().getBoolean("DataSave.Enable")) saveData();
    }

    public HashMap<String, Integer> getSetWanted() {
        return setWanted;
    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("DataSave.Enable")) saveData();
    }
}