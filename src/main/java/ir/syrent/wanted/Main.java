package ir.syrent.wanted;

import ir.syrent.wanted.Commands.ComplaintCommand;
import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Commands.WantedsCommand;
import ir.syrent.wanted.DataManager.LanguageGenerator;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.DataManager.WantedsYML;
import ir.syrent.wanted.Dependencies.PlaceholderAPI;
import ir.syrent.wanted.Dependencies.WorldGuard;
import ir.syrent.wanted.Events.*;
import ir.syrent.wanted.GUI.RequestGUI;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.TabCompleter;
import ir.syrent.wanted.Utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin implements CommandExecutor {

    private static Main plugin;

    public WantedsYML wantedsYML;
    public LanguageGenerator enUSLanguage;
    public LanguageGenerator zhCNLanguage;
    public LanguageGenerator viVNLanguage;
    public Messages messages;
    public Log log;
    public SkullBuilder skullBuilder;
    public RequestGUI requestGUI;
    public WorldGuard worldGuard;

    public boolean placeholderAPIFound;
    public static String languageName;

    public HashMap<String, String> playerDamagedMap = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;

        initializeBstats();
        languageName = this.getConfig().getString("Wanted.LanguageFile");
        initializeYamlFiles();
        initializeInstances();
        dependencyChecker();
        registerCommands();
        registerEvents();
    }

    public void initializeBstats() {
        new Metrics(this, 12311);
    }

    public void registerCommands() {
        getCommand("wanted").setExecutor(new WantedCommand());
        getCommand("wanteds").setExecutor(new WantedsCommand());
        getCommand("complaint").setExecutor(new ComplaintCommand());
        getCommand("wanted").setTabCompleter(new TabCompleter());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListenerComplaint(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
    }

    public void initializeYamlFiles() {
        wantedsYML = new WantedsYML();
        wantedsYML.saveDefaultConfig();
        enUSLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "en_US");
        enUSLanguage.saveDefaultConfig();
        zhCNLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "zh_CN");
        zhCNLanguage.saveDefaultConfig();
        viVNLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "vi_VN");
        viVNLanguage.saveDefaultConfig();
    }

    public void initializeInstances() {
        this.saveDefaultConfig();
        skullBuilder = new SkullBuilder(this);
        requestGUI = new RequestGUI();
        messages = new Messages();
        log = new Log();
        new Wanted();
        new WantedManager();
    }

    public void dependencyChecker() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getLogger().info(Utils.color("PlaceholderAPI found! enabling hook..."));
            new PlaceholderAPI().register();
            placeholderAPIFound = true;
            this.getLogger().info(Utils.color("PlaceholderAPI hook enabled!"));
        } else {
            this.getLogger().info(Utils.color("PlaceholderAPI not found! disabling hook..."));
            placeholderAPIFound = false;
        }

        if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
            this.getLogger().info(Utils.color("Citizens found! enabling hook..."));
            this.getServer().getPluginManager().registerEvents(new NPCDeathListener(), this);
            this.getLogger().info(Utils.color("Citizens hook enabled!"));
        } else {
            this.getLogger().info(Utils.color("Citizens not found! disabling hook..."));
            placeholderAPIFound = false;
        }

        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            this.getLogger().info(Utils.color("WorldGuard found! enabling hook..."));
            worldGuard = new WorldGuard();
            this.getLogger().info(Utils.color("WorldGuard hook enabled!"));
        } else {
            this.getLogger().info(Utils.color("WorldGuard not found! disabling hook..."));
        }
    }

    public static Main getInstance() {
        return plugin;
    }
}