package ir.syrent.wanted;

import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Commands.WantedsCommand;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.DataManager.YamlGenerator;
import ir.syrent.wanted.DataManager.WantedsYML;
import ir.syrent.wanted.Dependencies.PlaceholderAPI;
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

public final class Main extends JavaPlugin implements CommandExecutor {

    private static Main plugin;

    public WantedsYML wantedsYML;
    public YamlGenerator enUSLanguage;
    public YamlGenerator zhCNLanguage;
    public Messages messages;
    public Log log;
    public SkullBuilder skullBuilder;
    public RequestGUI requestGUI;

    public boolean placeholderAPIFound;
    public static String languageName;

    @Override
    public void onEnable() {
        plugin = this;

        initializeBstats();
        languageName = this.getConfig().getString("Wanted.LanguageFile");
        dependencyChecker();
        registerCommands();
        registerEvents();
        initializeInstances();
    }

    public void initializeBstats() {
        new Metrics(this, 12311);
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
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
    }

    public void initializeInstances() {
        this.saveDefaultConfig();
        wantedsYML = new WantedsYML();
        wantedsYML.saveDefaultConfig();
        enUSLanguage = new YamlGenerator(this.getDataFolder() + "/language", "en_US");
        enUSLanguage.saveDefaultConfig();
        zhCNLanguage = new YamlGenerator(this.getDataFolder() + "/language", "zh_CN");
        zhCNLanguage.saveDefaultConfig();
        skullBuilder = new SkullBuilder(this);
        requestGUI = new RequestGUI();
        messages = new Messages();
        log = new Log();
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
    }

    public static Main getInstance() {
        return plugin;
    }
}