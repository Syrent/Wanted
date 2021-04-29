package ir.syrent.wanted.Core;

import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Commands.WantedsCommand;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.DataManager.MessagesYML;
import ir.syrent.wanted.DataManager.WantedsYML;
import ir.syrent.wanted.Dependencies.PlaceholderAPI;
import ir.syrent.wanted.Events.DeathEvent;
import ir.syrent.wanted.GUI.RequestGUI;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.TabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin implements CommandExecutor {

    private static Main plugin;

    public static WantedsYML wantedsYML;
    public static MessagesYML messagesYML;
    public static Messages messages;
    public static Log log;
    private static Main instance;
    public SkullBuilder skullBuilder;
    public RequestGUI requestGUI;
    public HashMap<String, Integer> wantedMap = new HashMap<>();
    public List<Inventory> playersGUI = new ArrayList<>();


    public Main() {
        instance = this;
    }

    public void saveData() {
        wantedsYML = new WantedsYML();
        if (wantedMap.isEmpty() && wantedsYML.getConfig().getConfigurationSection("wanted") == null) return;
        if (!wantedMap.isEmpty()) {
            for (Map.Entry<String, Integer> wantedlist : wantedMap.entrySet()) {
                wantedsYML.getConfig().set("wanted." + wantedlist.getKey(), wantedlist.getValue());
                if (Bukkit.getPlayerExact(wantedlist.getKey()) != null)
                    skullBuilder.cache.put(Bukkit.getPlayerExact(wantedlist.getKey()), skullBuilder.getHead(Bukkit.getPlayerExact(wantedlist.getKey())).serialize());
            }
            if (getConfig().getBoolean("DataSave.Notification"))
                Bukkit.getLogger().info(messages.getRawPrefix() + "Wanted data saved!");
            wantedsYML.saveConfig();
        } else {
            for (String wantedlist : wantedsYML.getConfig().getConfigurationSection("wanted").getKeys(false)) {
                wantedMap.remove(wantedlist);
                wantedMap.put(wantedlist, wantedsYML.getConfig().getInt("wanted." + wantedlist));
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

        plugin = this;
        if (getConfig().getBoolean("DataSave.Enable")) saveData();

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
        skullBuilder = new SkullBuilder();
        requestGUI = new RequestGUI();
        requestGUI.start();
        this.saveDefaultConfig();
        messagesYML.saveDefaultConfig();
        wantedsYML.saveDefaultConfig();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }

    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("DataSave.Enable")) saveData();
    }

    public static Main getInstance() {
        return plugin;
    }
}