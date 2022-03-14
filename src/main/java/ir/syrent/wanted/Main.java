package ir.syrent.wanted;

import ir.syrent.wanted.Commands.ComplaintCommand;
import ir.syrent.wanted.Commands.WantedCommand;
import ir.syrent.wanted.Commands.WantedsCommand;
import ir.syrent.wanted.DataManager.LanguageGenerator;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.DataManager.WantedsYML;
import ir.syrent.wanted.Dependencies.WorldGuard;
import ir.syrent.wanted.Events.*;
import ir.syrent.wanted.GUI.RequestGUI;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.TabCompleter;
import ir.syrent.wanted.Utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class Main extends JavaPlugin implements CommandExecutor {

    private static Main plugin;

    public WantedsYML wantedsYML;
    public LanguageGenerator enUSLanguage;
    public LanguageGenerator zhCNLanguage;
    public LanguageGenerator viVNLanguage;
    public LanguageGenerator esESLanguage;
    public LanguageGenerator languageYML;
    public Log log;
    public SkullBuilder skullBuilder;
    public RequestGUI requestGUI;
    public static WorldGuard worldGuard;

    public static boolean placeholderAPIFound;
    public static String languageName;

    public HashMap<String, String> playerDamagedMap = new HashMap<>();
    public HashMap<String, String> playerVictimMap = new HashMap<>();

    public File logDirectory;

    @Override
    public void onEnable() {
        plugin = this;
        placeholderAPIFound = false;

        initializeBstats();
        logDirectory = new File(getDataFolder() + File.separator + "logs");
        languageName = this.getConfig().getString("Wanted.LanguageFile");
        initializeYamlFiles();
        initializeInstances();
        Utils.checkDependencies("PlaceholderAPI", "Citizens", "WorldGuard");
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
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new RequestGUI(), this);
    }

    public void initializeYamlFiles() {
        new Messages();
        wantedsYML = new WantedsYML();
        wantedsYML.saveDefaultConfig();
        enUSLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "en_US");
        enUSLanguage.saveDefaultConfig();
        zhCNLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "zh_CN");
        zhCNLanguage.saveDefaultConfig();
        viVNLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "vi_VN");
        viVNLanguage.saveDefaultConfig();
        esESLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "es_ES");
        esESLanguage.saveDefaultConfig();
    }

    public void initializeInstances() {
        this.saveDefaultConfig();
        skullBuilder = new SkullBuilder(this);
        requestGUI = new RequestGUI();
        log = new Log();
        new Wanted();
        new WantedManager();
    }

    public LanguageGenerator getLanguageYML() {
        return languageYML;
    }

    public static Main getInstance() {
        return plugin;
    }
}