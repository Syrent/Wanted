package ir.syrent.wanted.Messages;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Messages {

    public static Main getPlugin() {
        return Main.getInstance();
    }

    private final String prefix = Main.getInstance().messagesYML.getConfig().getString("prefix");
    private final String needPermission = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("need-permission")));
    private final String needGPS = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("needGPS")));
    private final String playerNotFound = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("player-not-found")));
    private final String selfTarget = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("self-target")));
    private final String searchTarget = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("search-target")));
    private final String targetWarn = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("search-notification")));
    private final String wantedTitle = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("wanted-title")));
    private final String wantedList = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("wanted-title")));
    private final String wantedTop = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("wanted-top")));
    private final String playerLeaveOnFinding = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("player-leave-on-finding")));
    private final String noWanteds = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("no-wanteds")));
    private final String maximumWantedChanged = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("maximum-wanted-changed")));
    private final String pluginReloaded = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("plugin-reloaded")));

    private final String getPlayerWanted = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("get-player-wanted")));
    private final String findUsage = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted find <player>"));
    private final String operation = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted %action% <player> <wanted>"));
    private final String clearOperator = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted Clear <player>"));
    private final String setMaximumUsage = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted set-maximum <number>"));
    private final String getWantedUsage = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted get <player>"));
    private final String clearWanted = Utils.color(String.format("%s%s", getPrefix(), "§7Wanted successfully cleared."));
    private final String takeWanted = Utils.color(String.format("%s%s", getPrefix(), "§7Wanted successfully taked."));
    private final String addWanted = Utils.color(String.format("%s%s", getPrefix(), "§7Wanted successfully added."));
    private final String setWanted = Utils.color(String.format("%s%s", getPrefix(), "§7Wanted was set."));
    private final String validNumber = Utils.color(String.format("%s%s", getPrefix(), "§cPlease enter an valid number."));

    private final String consoleSender = Utils.color(String.format("%s%s", getPrefix(), "§cOnly player can run this command."));

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
        int wanted = Main.getInstance().wantedMap.get(killer.getName());
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