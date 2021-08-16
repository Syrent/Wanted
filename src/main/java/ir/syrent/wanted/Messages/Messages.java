package ir.syrent.wanted.Messages;

import ir.syrent.wanted.DataManager.LanguageGenerator;
import ir.syrent.wanted.Main;
import ir.syrent.wanted.Utils.Utils;
import ir.syrent.wanted.WantedManager;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static Main getPlugin() {
        return Main.getInstance();
    }

    private String prefix;
    private String needPermission;
    private String needGPS;
    private String playerNotFound;
    private String selfTarget;
    private String searchTarget;
    private String targetWarn;
    private String wantedTitle;
    private String wantedList;
    private String wantedTop;
    private String playerLeaveOnFinding;
    private String noWanteds;
    private String maximumWantedChanged;
    private String pluginReloaded;
    private String itemCooldown;
    private String getPlayerWanted;
    private String findUsage;
    private String operation;
    private String clearOperator;
    private String setMaximumUsage;
    private String getWantedUsage;
    private String clearWanted;
    private String takeWanted;
    private String addWanted;
    private String setWanted;
    private String validNumber;
    private String playerWanted;
    private String consoleSender;
    private String messageOnKillPlayer;
    private String messageOnKillMob;
    private String messageOnKillNPC;
    private String loadingData;
    private String wantedSymbol;
    private String playerWantedListFormat;

    //Help messages
    private String helpHeader;
    private String wantedReloadHelp;
    private String wantedFindHelp;
    private String wantedMaximumHelp;
    private String wantedClearHelp;
    private String wantedSetHelp;
    private String wantedTakeHelp;
    private String wantedAddHelp;
    private String wantedsHelp;
    private String nextPageHelp;
    private String wantedGUIHelp;
    private String wantedGetHelp;
    private String wantedTopHelp;
    private String wantedHelpHelp;
    private String prevPageHelp;

    //WantedGUI
    private String wantedGUITitle;
    private String wantedGUIRefreshButton;
    private String wantedGUINextPageButton;
    private String wantedGUIPrevPageButton;
    private String wantedGUIPlayerTitle;
    private List<String> wantedGUIPlayerLore;

    //BossBar
    private String barTitle;


    private String messageFormatter(String message) {
        return Utils.color(String.format("%s%s", getPrefix(), message));
    }

    public Messages() {
        reload();
    }

    public void reload() {
        LanguageGenerator languageYML = new LanguageGenerator(Main.getInstance().getDataFolder() + "/language", Main.languageName);
        
        prefix = languageYML.getConfig().getString("prefix");
        needPermission = messageFormatter(languageYML.getConfig().getString("need-permission"));
        needGPS = messageFormatter(languageYML.getConfig().getString("needGPS"));
        playerNotFound = messageFormatter(languageYML.getConfig().getString("player-not-found"));
        selfTarget = messageFormatter(languageYML.getConfig().getString("self-target"));
        searchTarget = messageFormatter(languageYML.getConfig().getString("search-target"));
        targetWarn = messageFormatter(languageYML.getConfig().getString("search-notification"));
        wantedTitle = Utils.color(languageYML.getConfig().getString("wanted-title"));
        wantedList = Utils.color(languageYML.getConfig().getString("wanted-list"));
        wantedTop = Utils.color(languageYML.getConfig().getString("wanted-top"));
        playerLeaveOnFinding = messageFormatter(languageYML.getConfig().getString("player-leave-on-finding"));
        noWanteds = messageFormatter(languageYML.getConfig().getString("no-wanteds"));
        maximumWantedChanged = messageFormatter(languageYML.getConfig().getString("maximum-wanted-changed"));
        pluginReloaded = messageFormatter(languageYML.getConfig().getString("plugin-reloaded"));
        itemCooldown = messageFormatter(languageYML.getConfig().getString("item-cooldown"));
        getPlayerWanted = messageFormatter(languageYML.getConfig().getString("get-player-wanted"));
        findUsage = messageFormatter(languageYML.getConfig().getString("find-usage"));
        operation = messageFormatter(languageYML.getConfig().getString("operation"));
        clearOperator = messageFormatter(languageYML.getConfig().getString("clear-operator"));
        setMaximumUsage = messageFormatter(languageYML.getConfig().getString("maximum-usage"));
        getWantedUsage = messageFormatter(languageYML.getConfig().getString("get-wanted-usage"));
        clearWanted = messageFormatter(languageYML.getConfig().getString("clear-wanted"));
        takeWanted = messageFormatter(languageYML.getConfig().getString("take-wanted"));
        addWanted = messageFormatter(languageYML.getConfig().getString("add-wanted"));
        setWanted = messageFormatter(languageYML.getConfig().getString("set-wanted"));
        validNumber = messageFormatter(languageYML.getConfig().getString("valid-number"));
        playerWanted = messageFormatter(languageYML.getConfig().getString("player-wanted"));
        consoleSender = messageFormatter(languageYML.getConfig().getString("console-sender"));
        messageOnKillPlayer = messageFormatter(languageYML.getConfig().getString("message-on-kill-player"));
        messageOnKillMob = messageFormatter(languageYML.getConfig().getString("message-on-kill-mob"));
        messageOnKillNPC = messageFormatter(languageYML.getConfig().getString("message-on-kill-npc"));
        loadingData = messageFormatter(languageYML.getConfig().getString("loading-data"));
        wantedSymbol = Utils.color(languageYML.getConfig().getString("wanted-symbol"));
        playerWantedListFormat = Utils.color(languageYML.getConfig().getString("player-wanted-list-format"));

        //Help messages
        helpHeader = Utils.color(languageYML.getConfig().getString("help-header"));
        wantedReloadHelp = Utils.color(languageYML.getConfig().getString("wanted-reload-help"));
        wantedFindHelp = Utils.color(languageYML.getConfig().getString("wanted-find-help"));
        wantedMaximumHelp = Utils.color(languageYML.getConfig().getString("wanted-maximum-help"));
        wantedClearHelp = Utils.color(languageYML.getConfig().getString("wanted-clear-help"));
        wantedSetHelp = Utils.color(languageYML.getConfig().getString("wanted-set-help"));
        wantedTakeHelp = Utils.color(languageYML.getConfig().getString("wanted-take-help"));
        wantedAddHelp = Utils.color(languageYML.getConfig().getString("wanted-add-help"));
        wantedsHelp = Utils.color(languageYML.getConfig().getString("wanteds-help"));
        nextPageHelp = Utils.color(languageYML.getConfig().getString("next-page-help"));
        wantedGUIHelp = Utils.color(languageYML.getConfig().getString("wanted-gui-help"));
        wantedGetHelp = Utils.color(languageYML.getConfig().getString("wanted-get-help"));
        wantedTopHelp = Utils.color(languageYML.getConfig().getString("wanted-top-help"));
        wantedHelpHelp = Utils.color(languageYML.getConfig().getString("wanted-help-help"));
        prevPageHelp = Utils.color(languageYML.getConfig().getString("prev-page-help"));

        //WantedGUI
        wantedGUITitle = Utils.color(languageYML.getConfig().getString("wanted-gui-title"));
        wantedGUIRefreshButton = Utils.color(languageYML.getConfig().getString("wanted-gui-refresh-button"));
        wantedGUINextPageButton = Utils.color(languageYML.getConfig().getString("wanted-gui-next-page-button"));
        wantedGUIPrevPageButton = Utils.color(languageYML.getConfig().getString("wanted-gui-prev-page-button"));
        wantedGUIPlayerTitle = Utils.color(languageYML.getConfig().getString("wanted-gui-player-title"));
        wantedGUIPlayerLore = new ArrayList<>();
        for (String line : languageYML.getConfig().getStringList("wanted-gui-player-lore")) {
            wantedGUIPlayerLore.add(Utils.color(line));
        }

        //BossBar
        barTitle = Utils.color(languageYML.getConfig().getString("bar-title"));
    }

    public void helpMessage1(CommandSender sender) {
        sender.sendMessage(getHelpHeader().replace("%version%", getPlugin().getDescription().getVersion()));
        TextComponent reloadCommand = new TextComponent(getWantedReloadHelp());
        reloadCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted reload"));
        sender.spigot().sendMessage(reloadCommand);
        TextComponent findCommand = new TextComponent(getWantedFindHelp());
        findCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted find "));
        sender.spigot().sendMessage(findCommand);
        TextComponent setMaximumCommand = new TextComponent(getWantedMaximumHelp());
        setMaximumCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted maximum "));
        sender.spigot().sendMessage(setMaximumCommand);
        TextComponent clearCommand = new TextComponent(getWantedClearHelp());
        clearCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted clear "));
        sender.spigot().sendMessage(clearCommand);
        TextComponent setCommand = new TextComponent(getWantedSetHelp());
        setCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted set "));
        sender.spigot().sendMessage(setCommand);
        TextComponent takeCommand = new TextComponent(getWantedTakeHelp());
        takeCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted take "));
        sender.spigot().sendMessage(takeCommand);
        TextComponent addCommand = new TextComponent(getWantedAddHelp());
        addCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted add "));
        sender.spigot().sendMessage(addCommand);
        TextComponent wantedsCommand = new TextComponent(getWantedsHelp());
        wantedsCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanteds"));
        sender.spigot().sendMessage(wantedsCommand);
        TextComponent textComponent = new TextComponent(getNextPageHelp());
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted help 2"));
        sender.spigot().sendMessage(textComponent);
    }

    public void helpMessage2(CommandSender sender) {
        sender.sendMessage(getHelpHeader().replace("%version%", getPlugin().getDescription().getVersion()));
        TextComponent wantedGUICommand = new TextComponent(getWantedGUIHelp());
        wantedGUICommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted gui"));
        sender.spigot().sendMessage(wantedGUICommand);
        TextComponent getWantedCommand = new TextComponent(getWantedGetHelp());
        getWantedCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted get "));
        sender.spigot().sendMessage(getWantedCommand);
        TextComponent topWantedCommand = new TextComponent(getWantedTopHelp());
        topWantedCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted top"));
        sender.spigot().sendMessage(topWantedCommand);
        TextComponent helpCommand = new TextComponent(getWantedHelpHelp());
        helpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted help "));
        sender.spigot().sendMessage(helpCommand);
        TextComponent textComponent = new TextComponent(getPrevPageHelp());
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted help 1"));
        sender.spigot().sendMessage(textComponent);
    }

    public String wantedSymbol(int count) {
        StringBuilder star = new StringBuilder();
        for (int i = 0; i < count; i++) {
            star.append(getWantedSymbol());
        }
        return Utils.color(getPlayerWantedListFormat().replace("%symbol%", star));
    }

    public String rawWantedSymbol(int count) {
        StringBuilder star = new StringBuilder();
        for (int i = 0; i < count; i++) {
            star.append(getWantedSymbol());
        }
        return Utils.color("%star%".replace("%symbol%", star));
    }

    public String playerDeathLogger(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer == null) return null;
        Integer wanted = WantedManager.getInstance().getWanted(player);
        if (wanted == null) return null;
        return "[Player] [" + Main.getInstance().log.formatMessage() + "] "
                + killer.getName() + " killed " + player.getName()
                + " in " + player.getWorld().getName()
                + " at X:" + (int) player.getLocation().getX()
                + " Y:" + (int) player.getLocation().getY()
                + " Z:" + (int) player.getLocation().getZ()
                + " | New Wanted: " + wanted;
    }

    public String npcDeathLogger(NPCDeathEvent event) {
        Entity npc = event.getEvent().getEntity();
        Player killer = event.getEvent().getEntity().getKiller();
        if (killer == null) return null;
        Integer wanted = WantedManager.getInstance().getWanted(killer);
        if (wanted == null) return null;
        return "[NPC] [" + Main.getInstance().log.formatMessage() + "] "
                + killer.getName() + " killed " + npc.getName()
                + " in " + npc.getWorld().getName()
                + " at X:" + (int) npc.getLocation().getX()
                + " Y:" + (int) npc.getLocation().getY()
                + " Z:" + (int) npc.getLocation().getZ()
                + " | New Wanted: " + wanted;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNeedPermission() {
        return needPermission;
    }

    public String getNeedGPS() {
        return needGPS;
    }

    public String getPlayerNotFound() {
        return playerNotFound;
    }

    public String getSelfTarget() {
        return selfTarget;
    }

    public String getSearchTarget() {
        return searchTarget;
    }

    public String getTargetWarn() {
        return targetWarn;
    }

    public String getWantedTitle() {
        return wantedTitle;
    }

    public String getWantedList() {
        return wantedList;
    }

    public String getPlayerLeaveOnFinding() {
        return playerLeaveOnFinding;
    }

    public String getNoWanteds() {
        return noWanteds;
    }

    public String getFindUsage() {
        return findUsage;
    }

    public String getOperation() {
        return operation;
    }

    public String getClearOperator() {
        return clearOperator;
    }

    public String getClearWanted() {
        return clearWanted;
    }

    public String getTakeWanted() {
        return takeWanted;
    }

    public String getAddWanted() {
        return addWanted;
    }

    public String getSetWanted() {
        return setWanted;
    }

    public String getValidNumber() {
        return validNumber;
    }

    public String getConsoleSender() {
        return consoleSender;
    }

    public String getMaximumWantedChanged() {
        return maximumWantedChanged;
    }

    public String getPluginReloaded() {
        return pluginReloaded;
    }

    public String getSetMaximumUsage() {
        return setMaximumUsage;
    }

    public String getGetWantedUsage() {
        return getWantedUsage;
    }

    public String getGetPlayerWanted() {
        return getPlayerWanted;
    }

    public String getWantedTop() {
        return wantedTop;
    }

    public String getItemCooldown() {
        return itemCooldown;
    }

    public String getPlayerWanted() {
        return playerWanted;
    }

    public String getMessageOnKillPlayer() {
        return messageOnKillPlayer;
    }

    public String getMessageOnKillMob() {
        return messageOnKillMob;
    }

    public String getMessageOnKillNPC() {
        return messageOnKillNPC;
    }

    public String getLoadingData() {
        return loadingData;
    }

    public String getHelpHeader() {
        return helpHeader;
    }

    public String getWantedReloadHelp() {
        return wantedReloadHelp;
    }

    public String getWantedFindHelp() {
        return wantedFindHelp;
    }

    public String getWantedMaximumHelp() {
        return wantedMaximumHelp;
    }

    public String getWantedClearHelp() {
        return wantedClearHelp;
    }

    public String getWantedSetHelp() {
        return wantedSetHelp;
    }

    public String getWantedTakeHelp() {
        return wantedTakeHelp;
    }

    public String getWantedAddHelp() {
        return wantedAddHelp;
    }

    public String getWantedsHelp() {
        return wantedsHelp;
    }

    public String getNextPageHelp() {
        return nextPageHelp;
    }

    public String getWantedGUIHelp() {
        return wantedGUIHelp;
    }

    public String getWantedGetHelp() {
        return wantedGetHelp;
    }

    public String getWantedTopHelp() {
        return wantedTopHelp;
    }

    public String getWantedHelpHelp() {
        return wantedHelpHelp;
    }

    public String getPrevPageHelp() {
        return prevPageHelp;
    }

    public String getWantedGUITitle() {
        return wantedGUITitle;
    }

    public String getWantedGUIRefreshButton() {
        return wantedGUIRefreshButton;
    }

    public String getWantedGUINextPageButton() {
        return wantedGUINextPageButton;
    }

    public String getWantedGUIPrevPageButton() {
        return wantedGUIPrevPageButton;
    }

    public String getWantedGUIPlayerTitle() {
        return wantedGUIPlayerTitle;
    }

    public List<String> getWantedGUIPlayerLore() {
        return wantedGUIPlayerLore;
    }

    public String getWantedSymbol() {
        return wantedSymbol;
    }

    public String getPlayerWantedListFormat() {
        return playerWantedListFormat;
    }

    public String getBarTitle() {
        return barTitle;
    }
}