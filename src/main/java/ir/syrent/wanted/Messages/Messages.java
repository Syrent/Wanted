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

    private final String prefix;
    private final String needPermission;
    private final String needGPS;
    private final String playerNotFound;
    private final String selfTarget;
    private final String searchTarget;
    private final String targetWarn;
    private final String wantedTitle;
    private final String wantedList;
    private final String wantedTop;
    private final String playerLeaveOnFinding;
    private final String noWanteds;
    private final String maximumWantedChanged;
    private final String pluginReloaded;
    private final String itemCooldown;

    private final String getPlayerWanted;
    private final String findUsage;
    private final String operation;
    private final String clearOperator;
    private final String setMaximumUsage;
    private final String getWantedUsage;
    private final String clearWanted;
    private final String takeWanted;
    private final String addWanted;
    private final String setWanted;
    private final String validNumber;

    private final String playerWanted;

    private final String consoleSender;

    public Messages() {
        prefix = Main.getInstance().messagesYML.getConfig().getString("prefix");
        needPermission = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("need-permission")));
        needGPS = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("needGPS")));
        playerNotFound = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("player-not-found")));
        selfTarget = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("self-target")));
        searchTarget = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("search-target")));
        targetWarn = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("search-notification")));
        wantedTitle = Utils.color(String.format("%s", Main.getInstance().messagesYML.getConfig().getString("wanted-title")));
        wantedList = Utils.color(String.format("%s", Main.getInstance().messagesYML.getConfig().getString("wanted-list")));
        wantedTop = Utils.color(String.format("%s", Main.getInstance().messagesYML.getConfig().getString("wanted-top")));
        playerLeaveOnFinding = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("player-leave-on-finding")));
        noWanteds = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("no-wanteds")));
        maximumWantedChanged = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("maximum-wanted-changed")));
        pluginReloaded = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("plugin-reloaded")));
        itemCooldown = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("item-cooldown")));

        getPlayerWanted = Utils.color(String.format("%s%s", getPrefix(), Main.getInstance().messagesYML.getConfig().getString("get-player-wanted")));
        findUsage = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted find <player>"));
        operation = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted %action% <player> <wanted>"));
        clearOperator = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted Clear <player>"));
        setMaximumUsage = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted set-maximum <number>"));
        getWantedUsage = Utils.color(String.format("%s%s", getPrefix(), "§cUsage: /wanted get <player>"));
        clearWanted = Utils.color(String.format("%s%s", getPrefix(), "§aWanted successfully cleared."));
        takeWanted = Utils.color(String.format("%s%s", getPrefix(), "§aWanted successfully taked."));
        addWanted = Utils.color(String.format("%s%s", getPrefix(), "§aWanted successfully added."));
        setWanted = Utils.color(String.format("%s%s", getPrefix(), "§aWanted was set."));
        validNumber = Utils.color(String.format("%s%s", getPrefix(), "§cPlease enter an valid number."));

        playerWanted = Utils.color(String.format("%s%s", getPrefix(), "&7Your Wanted is &b%wanted%."));

        consoleSender = Utils.color(String.format("%s%s", getPrefix(), "§cOnly player can run this command."));
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

    public String getItemCooldown() {
        return itemCooldown;
    }

    public String getPlayerWanted() {
        return playerWanted;
    }
}