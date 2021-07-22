package ir.syrent.wanted;

import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Commands.WantedsCommand;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.DataManager.MessagesYML;
import ir.syrent.wanted.DataManager.WantedsYML;
import ir.syrent.wanted.Dependencies.PlaceholderAPI;
import ir.syrent.wanted.Events.PlayerDeathListener;
import ir.syrent.wanted.Events.InventoryClickListener;
import ir.syrent.wanted.Events.PlayerJoinListener;
import ir.syrent.wanted.Events.PlayerQuitListener;
import ir.syrent.wanted.GUI.RequestGUI;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.TabCompleter;
import ir.syrent.wanted.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

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

    public boolean placeholderAPIFound;

    @Override
    public void onEnable() {
        plugin = this;

        initializePlaceholderAPI();
        registerCommands();
        registerEvents();
        initializeInstances();

        reloadData();
    }

    @Override
    public void onDisable() {
        reloadData();
    }

    public void reloadData() {
        if (!getConfig().getBoolean("DataSave.Enable", true)) return;
        ConfigurationSection section = wantedsYML.getConfig().getConfigurationSection("wanted");
        if (section == null) {
            this.getLogger().warning("wanted section is empty. Cancel player data loader...");
            return;
        }
        if (wantedMap.isEmpty()) {
            //If the wantedMap was empty, get wanteds from the configuration file.
            ConfigurationSection wantedSection = wantedsYML.getConfig().getConfigurationSection("wanted");
            if (wantedSection != null) {
                this.getLogger().info("Getting player's data...");
                for (String wantedPlayerName : wantedSection.getKeys(false)) {
                    wantedMap.put(wantedPlayerName, wantedsYML.getConfig().getInt("wanted." + wantedPlayerName));
                }
            }
        }
        //Saving data to the configuration file.
        for (String playerName : wantedMap.keySet()) {
            Player player = Bukkit.getPlayerExact(playerName);
            int wanteds = wantedMap.get(playerName);
            section.set(playerName, wanteds);
            //Putting player's head to our cache if they were online
            if (player != null)
                skullBuilder.cache.put(player, skullBuilder.getHead(player).serialize());
        }
        if (getConfig().getBoolean("DataSave.Notification"))
            this.getLogger().info("Wanted data has been saved.");
        wantedsYML.saveConfig();
    }

    public void registerCommands() {
        getCommand("wanted").setExecutor(new WantedCommand());
        getCommand("wanteds").setExecutor(new WantedsCommand());
        getCommand("wanted").setTabCompleter(new TabCompleter());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
    }

    public void initializeInstances() {
        this.saveDefaultConfig();
        wantedsYML = new WantedsYML();
        messagesYML = new MessagesYML();
        wantedsYML.saveDefaultConfig();
        messagesYML.saveDefaultConfig();
        skullBuilder = new SkullBuilder(this);
        requestGUI = new RequestGUI();
        messages = new Messages();
        log = new Log();
    }

    public void initializePlaceholderAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getLogger().info(Utils.color("PlaceholderAPI found! enabling hook..."));
            new PlaceholderAPI().register();
            this.getLogger().info(Utils.color("PlaceholderAPI hook enabled!"));
            placeholderAPIFound = true;
        } else {
            this.getLogger().warning(Utils.color("PlaceholderAPI not found! disabling hook..."));
            placeholderAPIFound = false;
        }
    }

    public static Main getInstance() {
        return plugin;
    }

}