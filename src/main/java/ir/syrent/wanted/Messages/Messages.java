package ir.syrent.wanted.Messages;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Utils.Utils;
import ir.syrent.wanted.WantedManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
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
    private String loadingData;

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
        setMaximumUsage = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("set-maximum-usage"));
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
        loadingData = messageFormatter(Main.getInstance().messagesYML.getConfig().getString("loading-data"));
    }

    public void helpMessage1(CommandSender sender) {
        sender.sendMessage("§7§l§m---------------§f[§r §bWanted v%version% §f]§7§l§m---------------§r".replace("%version%", getPlugin().getDescription().getVersion()));
        if (sender.hasPermission("wanted.admin")) {
            TextComponent reloadCommand = new TextComponent("§7- §a/wanted §breload");
            reloadCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted reload"));
            sender.spigot().sendMessage(reloadCommand);
        }
        if (sender.hasPermission("wanted.find") || sender.hasPermission("wanted.admin")) {
            TextComponent findCommand = new TextComponent("§7- §a/wanted §bfind <player>");
            findCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted find "));
            sender.spigot().sendMessage(findCommand);
        }
        if (sender.hasPermission("wanted.admin") || sender.hasPermission("wanted.admin")) {
            TextComponent setMaximumCommand = new TextComponent("§7- §a/wanted §bset-maximum <number>");
            setMaximumCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted set-maximum "));
            sender.spigot().sendMessage(setMaximumCommand);
        }
        if (sender.hasPermission("wanted.clear") || sender.hasPermission("wanted.admin")) {
            TextComponent clearCommand = new TextComponent("§7- §a/wanted §bclear <player>");
            clearCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted clear "));
            sender.spigot().sendMessage(clearCommand);
        }
        if (sender.hasPermission("wanted.set") || sender.hasPermission("wanted.admin")) {
            TextComponent setCommand = new TextComponent("§7- §a/wanted §bset <player> <number>");
            setCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted set "));
            sender.spigot().sendMessage(setCommand);
        }
        if (sender.hasPermission("wanted.take") || sender.hasPermission("wanted.admin")) {
            TextComponent takeCommand = new TextComponent("§7- §a/wanted §btake <player> <number>");
            takeCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted take "));
            sender.spigot().sendMessage(takeCommand);
        }
        if (sender.hasPermission("wanted.add") || sender.hasPermission("wanted.admin")) {
            TextComponent addCommand = new TextComponent("§7- §a/wanted §badd <player> <number>");
            addCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted add "));
            sender.spigot().sendMessage(addCommand);
        }
        if (sender.hasPermission("wanted.list") || sender.hasPermission("wanted.admin")) {
            TextComponent wantedsCommand = new TextComponent("§7- §a/wanteds");
            wantedsCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanteds"));
            sender.spigot().sendMessage(wantedsCommand);
        }
        TextComponent textComponent = new TextComponent("§e(Page 1/2) §7Next Page »");
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted help 2"));
        sender.spigot().sendMessage(textComponent);
    }

    public void helpMessage2(CommandSender sender) {
        sender.sendMessage("§7§l§m---------------§f[§r §bWanted v%version% §f]§7§l§m---------------§r".replace("%version%", getPlugin().getDescription().getVersion()));
        if (sender.hasPermission("wanted.gui") || sender.hasPermission("wanted.admin")) {
            TextComponent wantedGUICommand = new TextComponent("§7- §a/wanted §bgui");
            wantedGUICommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted gui"));
            sender.spigot().sendMessage(wantedGUICommand);
        }
        if (sender.hasPermission("wanted.get") || sender.hasPermission("wanted.admin")) {
            TextComponent getWantedCommand = new TextComponent("§7- §a/wanted §bget <player>");
            getWantedCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted get "));
            sender.spigot().sendMessage(getWantedCommand);
        }
        if (sender.hasPermission("wanted.top") || sender.hasPermission("wanted.admin")) {
            TextComponent topWantedCommand = new TextComponent("§7- §a/wanted §btop");
            topWantedCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted top"));
            sender.spigot().sendMessage(topWantedCommand);
        }
        if (sender.hasPermission("wanted.help") || sender.hasPermission("wanted.admin")) {
            TextComponent helpCommand = new TextComponent("§7- §a/wanted §bhelp <page>");
            helpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted help "));
            sender.spigot().sendMessage(helpCommand);
        }
        TextComponent textComponent = new TextComponent("§e(Page 2/2) §7Prev Page «");
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

    public String logger(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        assert killer != null;
        int wanted = WantedManager.getInstance().getWanteds().get(killer.getName());
        return "[" + Main.getInstance().log.formatMessage() + "] "
                + killer.getName() + " killed " + player.getName()
                + " in " + player.getWorld().getName()
                + " at X:" + (int) player.getLocation().getX()
                + " Y:" + (int) player.getLocation().getY()
                + " Z:" + (int) player.getLocation().getZ()
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

    public String getLoadingData() {
        return loadingData;
    }
}