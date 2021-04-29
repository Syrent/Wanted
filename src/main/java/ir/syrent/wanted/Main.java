package ir.syrent.wanted;

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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin implements CommandExecutor {

    private static Main plugin;

    public HashMap<String, Integer> wantedMap = new HashMap<>();
    public List<Inventory> playersGUI = new ArrayList<>();

    public WantedsYML wantedsYML;
    public MessagesYML messagesYML;
    public Messages messages;
    public Log log;
    public SkullBuilder skullBuilder;
    public RequestGUI requestGUI;

    public void reloadData() {
        if (!getConfig().getBoolean("DataSave.Enable", true)) return;
        if (wantedsYML.getConfig().getConfigurationSection("wanted") == null) return;
        if (!wantedMap.isEmpty()) {
            //Saving data to the configuration file.
            for (String playerName : wantedMap.keySet()) {
                Player player = Bukkit.getPlayerExact(playerName);
                int wanteds = wantedMap.get(playerName);
                wantedsYML.getConfig().set("wanted." + playerName, wanteds);
                //Putting player's head to our cache if they were online
                if (player != null)
                    skullBuilder.cache.put(player, skullBuilder.getHead(player).serialize());
            }
            if (getConfig().getBoolean("DataSave.Notification"))
                this.getLogger().info("Wanted data has been saved.");
            wantedsYML.saveConfig();
        } else {
            //If the wantedMap was empty, get wanteds from the configuration file.
            for (String wantedPlayerName : wantedsYML.getConfig().getConfigurationSection("wanted").getKeys(false)) {
                wantedMap.put(wantedPlayerName, wantedsYML.getConfig().getInt("wanted." + wantedPlayerName));
            }
        }
    }

    @Override
    public void onEnable() {
        plugin = this;

        registerCmds();
        registerEvents();
        initializeInstances();
        initializePlaceholderAPI();

        reloadData();
        autoSaveData();
    }

    @Override
    public void onDisable() {
        reloadData();
    }

    public void autoSaveData() {
        new BukkitRunnable() {
            @Override
            public void run() {
                reloadData();
            }
        }.runTaskTimer(this, 0, 20L * 60 * getConfig().getInt("DataSave.Interval"));
    }

    public void registerCmds() {
        getCommand("wanted").setExecutor(new WantedCommand());
        getCommand("wanteds").setExecutor(new WantedsCommand());
        getCommand("wanted").setTabCompleter(new TabCompleter());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new DeathEvent(), this);
    }

    public void initializeInstances() {
        this.saveDefaultConfig();
        wantedsYML = new WantedsYML();
        messagesYML = new MessagesYML();
        messagesYML.saveDefaultConfig();
        wantedsYML.saveDefaultConfig();
        skullBuilder = new SkullBuilder();
        requestGUI = new RequestGUI();
        messages = new Messages();
        log = new Log();
    }

    public void initializePlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }
    }

    public static Main getInstance() {
        return plugin;
    }

}