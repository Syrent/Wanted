package ir.syrent.wanted.Messages;

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
    private String helpFooter;


    private String messageFormatter(String message) {
        return Utils.color(String.format("%s%s", getPrefix(), message));
    }

    public Messages() {
        reload();
    }

    public void reload() {
        prefix = Main.getInstance().messagesYML.getConfig().getString("prefix");
        needPermission = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("need-permission"));
        needGPS = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("needGPS"));
        playerNotFound = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("player-not-found"));
        selfTarget = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("self-target"));
        searchTarget = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("search-target"));
        targetWarn = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("search-notification"));
        wantedTitle = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-title"));
        wantedList = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-list"));
        wantedTop = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-top"));
        playerLeaveOnFinding = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("player-leave-on-finding"));
        noWanteds = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("no-wanteds"));
        maximumWantedChanged = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("maximum-wanted-changed"));
        pluginReloaded = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("plugin-reloaded"));
        itemCooldown = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("item-cooldown"));
        getPlayerWanted = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("get-player-wanted"));
        findUsage = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("find-usage"));
        operation = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("operation"));
        clearOperator = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("clear-operator"));
        setMaximumUsage = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("maximum-usage"));
        getWantedUsage = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("get-wanted-usage"));
        clearWanted = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("clear-wanted"));
        takeWanted = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("take-wanted"));
        addWanted = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("add-wanted"));
        setWanted = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("set-wanted"));
        validNumber = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("valid-number"));
        playerWanted = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("player-wanted"));
        consoleSender = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("console-sender"));
        messageOnKillPlayer = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("message-on-kill-player"));
        messageOnKillMob = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("message-on-kill-mob"));
        messageOnKillNPC = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("message-on-kill-npc"));
        loadingData = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("loading-data"));

        //Help messages
        helpHeader = Utils.color(Main.getInstance().messagesYML.getConfig().getString("help-header"));
        wantedReloadHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-reload-help"));
        wantedFindHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-find-help"));
        wantedMaximumHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-maximum-help"));
        wantedClearHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-clear-help"));
        wantedSetHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-set-help"));
        wantedTakeHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-take-help"));
        wantedAddHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-add-help"));
        wantedsHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanteds-help"));
        nextPageHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("next-page-help"));
        wantedGUIHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-gui-help"));
        wantedGetHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-get-help"));
        wantedTopHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-top-help"));
        wantedHelpHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("wanted-help-help"));
        prevPageHelp = Utils.color(Main.getInstance().messagesYML.getConfig().getString("prev-page-help"));
        helpFooter = Utils.color(Main.getInstance().messagesYML.getConfig().getString("help-footer"));
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
        String format;
        switch (count) {
            case 1:
                format = "&b%player% &a» &7[&6✯&7]";
                break;
            case 2:
                format = "&b%player% &a» &7[&6✯✯&7]";
                break;
            case 3:
                format = "&b%player% &a» &7[&6✯✯✯&7]";
                break;
            case 4:
                format = "&b%player% &a» &7[&6✯✯✯✯&7]";
                break;
            case 5:
                format = "&b%player% &a» &7[&6✯✯✯✯✯&7]";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + count);
        }
        return Utils.color(format);
    }

    public String rawWantedSymbol(int count) {
        String format;
        switch (count) {
            case 1:
                format = "&6✯";
                break;
            case 2:
                format = "&6✯✯";
                break;
            case 3:
                format = "&6✯✯✯";
                break;
            case 4:
                format = "&6✯✯✯✯";
                break;
            case 5:
                format = "&6✯✯✯✯✯";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + count);
        }
        return Utils.color(format);
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

    public String getHelpFooter() {
        return helpFooter;
    }
}