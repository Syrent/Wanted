package ir.syrent.wanted.Messages;

import ir.syrent.wanted.Core.Wanted;
import ir.syrent.wanted.DataManager.Log;
import ir.syrent.wanted.DataManager.MessagesYML;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    private static final Wanted plugin = Wanted.getPlugin(Wanted.class);
    private final MessagesYML messagesYML = new MessagesYML();

    private final String prefix = getMessagesYML().getConfig().getString("prefix");

    private final String needPermission = getMessagesYML().getConfig().getString("need-permission").replace("%prefix%", getPrefix());
    private final String needGPS = getMessagesYML().getConfig().getString("needGPS").replace("%prefix%", getPrefix());
    private final String playerNotFound = getMessagesYML().getConfig().getString("player-not-found").replace("%prefix%", getPrefix());
    private final String selfTarget = getMessagesYML().getConfig().getString("self-target").replace("%prefix%", getPrefix());
    private final String searchTarget = getMessagesYML().getConfig().getString("search-target").replace("%prefix%", getPrefix());
    private final String targetWarn = getMessagesYML().getConfig().getString("search-notification").replace("%prefix%", getPrefix());
    private final String wantedTitle = getMessagesYML().getConfig().getString("wanted-title").replace("%prefix%", getPrefix());
    private final String wantedList = getMessagesYML().getConfig().getString("wanted-list").replace("%prefix%", getPrefix());
    private final String wantedTop = getMessagesYML().getConfig().getString("wanted-top").replace("%prefix%", getPrefix());
    private final String playerLeaveOnFinding = getMessagesYML().getConfig().getString("player-leave-on-finding").replace("%prefix%", getPrefix());
    private final String noWanteds = getMessagesYML().getConfig().getString("no-wanteds").replace("%prefix%", getPrefix());
    private final String maximumWantedChanged = getMessagesYML().getConfig().getString("maximum-wanted-changed").replace("%prefix%", getPrefix());
    private final String pluginReloaded = getMessagesYML().getConfig().getString("plugin-reloaded").replace("%prefix%", getPrefix());
    private final String getPlayerWanted = getMessagesYML().getConfig().getString("get-player-wanted").replace("%prefix%", getPrefix());

    private final String findUsage = "%prefix% §cUsage: /wanted find <player>".replace("%prefix%", getPrefix());
    private final String operation = "%prefix% §cUsage: /wanted %action% <player> <wanted>".replace("%prefix%", getPrefix());
    private final String clearOperator = "%prefix% §cUsage: /wanted Clear <player>".replace("%prefix%", getPrefix());
    private final String setMaximumUsage = "%prefix% §cUsage: /wanted set-maximum <number>".replace("%prefix%", getPrefix());
    private final String getWantedUsage = "%prefix% §cUsage: /wanted get <player>".replace("%prefix%", getPrefix());
    private final String clearWanted = "%prefix% §7Wanted successfully cleared.".replace("%prefix%", getPrefix());
    private final String takeWanted = "%prefix% §7Wanted successfully taked.".replace("%prefix%", getPrefix());
    private final String addWanted = "%prefix% §7Wanted successfully added.".replace("%prefix%", getPrefix());
    private final String setWanted = "%prefix% §7Wanted was set.".replace("%prefix%", getPrefix());
    private final String validNumber = "%prefix% §cPlease enter an valid number.".replace("%prefix%", getPrefix());
    private final String consoleSender = "%prefix% §cOnly player can run this command.".replace("%prefix%", getPrefix());

    private final List<TextComponent> message = new ArrayList<>();

    public static Wanted getPlugin() {
        return plugin;
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
        return format;
    }

    public String logger(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        assert killer != null;
        int wanted = plugin.getSetWanted().get(killer.getName());
        Log log = new Log();
        return "[" + log.formatMessage() + "] "
                + killer.getName() + " killed " + player.getName()
                + " in " + player.getWorld().getName()
                + " at X:" + (int) player.getLocation().getX()
                + " Y:" + (int) player.getLocation().getY()
                + " Z:" + (int) player.getLocation().getZ()
                + " | New Wanted: " + wanted;
    }

    public MessagesYML getMessagesYML() {
        return messagesYML;
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

    public List<TextComponent> getMessage() {
        return message;
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

    public String getRawPrefix() {
        return "[Wanted] ";
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
}