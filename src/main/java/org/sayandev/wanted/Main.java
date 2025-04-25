package org.sayandev.wanted;

import org.sayandev.wanted.Commands.ComplaintCommand;
import org.sayandev.wanted.Commands.WantedCommand;
import org.sayandev.wanted.Commands.WantedsCommand;
import org.sayandev.wanted.DataManager.LanguageGenerator;
import org.sayandev.wanted.DataManager.Log;
import org.sayandev.wanted.DataManager.WantedsYML;
import org.sayandev.wanted.Dependencies.WorldGuard;
import org.sayandev.wanted.Listeners.EntityDamageByEntityListener;
import org.sayandev.wanted.Listeners.EntityDeathListener;
import org.sayandev.wanted.Listeners.PlayerDeathListener;
import org.sayandev.wanted.Listeners.PlayerDeathListenerComplaint;
import org.sayandev.wanted.Listeners.PlayerJoinListener;
import org.sayandev.wanted.Listeners.PlayerQuitListener;
import org.sayandev.wanted.GUI.RequestGUI;
import org.sayandev.wanted.Messages.Messages;
import org.sayandev.wanted.Utils.SkullBuilder;
import org.sayandev.wanted.Utils.TabCompleter;
import org.sayandev.wanted.Utils.Utils;
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
    public static boolean worldGuardFound;
    public static String languageName;

    public HashMap<String, String> playerDamagedMap = new HashMap<>();
    public HashMap<String, String> playerVictimMap = new HashMap<>();

    public File logDirectory;

    @Override
    public void onEnable() {
        plugin = this;
        placeholderAPIFound = false;
        worldGuardFound = false;

        initializeBstats();
        logDirectory = new File(getDataFolder() + File.separator + "logs");
        languageName = this.getConfig().getString("Wanted.LanguageFile");
        initializeYamlFiles();
        initializeInstances();
        Utils.checkDependencies("PlaceholderAPI", "Citizens", "WorldGuard");
        registerCommands();
        registerEvents();
        skullBuilder.addPlayersToCache();
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
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDeathListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new RequestGUI(), this);
    }

    public void initializeYamlFiles() {
        new Messages();
        wantedsYML = new WantedsYML(this);
        enUSLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "en_US");
        zhCNLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "zh_CN");
        viVNLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "vi_VN");
        esESLanguage = new LanguageGenerator(this.getDataFolder() + "/language", "es_ES");
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