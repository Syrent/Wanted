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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Messages extends Utils {

    public static Main getPlugin() {
        return Main.getInstance();
    }

    public static String PREFIX;
    public static String NEED_PERMISSION;
    public static String NEED_GPS;
    public static String PLAYER_NOT_FOUND;
    public static String SELF_TARGET;
    public static String SEARCH_TARGET;
    public static String TARGET_WARN;
    public static String WANTED_TITLE;
    public static String WANTED_LIST;
    public static String WANTED_TOP;
    public static String PLAYER_LEAVE_ON_FINDING;
    public static String NO_WANTEDS;
    public static String MAXIMUM_WANTED_CHANGED;
    public static String PLUGIN_RELOADED;
    public static String ITEM_COOLDOWN;
    public static String GET_PLAYER_WANTED;
    public static String OPERATION;
    public static String CLEAR_OPERATOR;
    public static String CLEAR_WANTED;
    public static String TAKE_WANTED;
    public static String ADD_WANTED;
    public static String SET_WANTED;
    public static String INVALID_NUMBER;
    public static String PLAYER_WANTED;
    public static String CONSOLE_SENDER;
    public static String ON_KILL_PLAYER;
    public static String ON_KILL_MOB;
    public static String ON_KILL_NPC;
    public static String LOADING_DATA;
    public static String WANTED_SYMBOL;
    public static String PLAYER_WANTED_LIST_FORMAT;
    public static String DIFFERENT_WORLD;


    public Messages() {
        initialize();
    }

    public static class Usage {
        public static String FIND;
        public static String SET_MAXIMUM;
        public static String GET_MAXIMUM;
        public static String LOG;
        public static String ARREST;


        public static void initialize() {
            FIND = formatMessage(getMessage("find-usage"));
            SET_MAXIMUM = formatMessage(getMessage("maximum-usage"));
            GET_MAXIMUM = formatMessage(getMessage("get-wanted-usage"));
            LOG = formatMessage(getMessage("log-usage"));
            ARREST = colorize(getMessage("arrest-usage"));
        }
    }

    public static class Help {
        public static String HEADER;
        public static String RELOAD;
        public static String FIND;
        public static String MAXIMUM;
        public static String CLEAR;
        public static String SET;
        public static String TAKE;
        public static String ADD;
        public static String WANTEDS;
        public static String GUI;
        public static String COMPLAINT;
        public static String GET;
        public static String TOP;
        public static String HELP;

        public static void initialize() {
            HEADER = colorize(getMessage("help-header"));
            RELOAD = colorize(getMessage("wanted-reload-help"));
            FIND = colorize(getMessage("wanted-find-help"));
            MAXIMUM = colorize(getMessage("wanted-maximum-help"));
            CLEAR = colorize(getMessage("wanted-clear-help"));
            SET = colorize(getMessage("wanted-set-help"));
            TAKE = colorize(getMessage("wanted-take-help"));
            ADD = colorize(getMessage("wanted-add-help"));
            WANTEDS = colorize(getMessage("wanteds-help"));
            GUI = colorize(getMessage("wanted-gui-help"));
            COMPLAINT = colorize(getMessage("wanted-complaint-help"));
            GET = colorize(getMessage("wanted-get-help"));
            TOP = colorize(getMessage("wanted-top-help"));
            HELP = colorize(getMessage("wanted-help-help"));
        }

        public static class Page {
            public static String NEXT_PAGE;
            public static String PREV_PAGE;

            public static void initialize() {
                NEXT_PAGE = colorize(getMessage("next-page-help"));
                PREV_PAGE = colorize(getMessage("prev-page-help"));
            }
        }
    }

    public static class GUI {
        public static String TITLE;

        public void initialize() {
            TITLE = colorize(getMessage("wanted-gui-title"));
        }

        public static class Button {
            public static String REFRESH;
            public static String NEXT_PAGE;
            public static String PREV_PAGE;

            public void initialize() {
                REFRESH = colorize(getMessage("wanted-gui-refresh-button"));
                NEXT_PAGE = colorize(getMessage("wanted-gui-next-page-button"));
                PREV_PAGE = colorize(getMessage("wanted-gui-prev-page-button"));
            }
        }

        public static class Player {
            public static String TITLE;
            public static List<String> LORE;

            public void initialize() {
                TITLE = colorize(getMessage("wanted-gui-player-title"));
                LORE = new ArrayList<>();
                for (String line : getMessageList("wanted-gui-player-lore")) {
                    LORE.add(colorize(line));
                }
            }
        }
    }

    public static class BossBar {
        public static String TITLE;

        public static void initialize() {
            TITLE = colorize(getMessage("bar-title"));
        }
    }

    public static class Complaint {
        public static String CONFIRM;
        public static String EXPIRE;
        public static String ALREADY_EXPIRED;
        public static String SUBMIT;
        public static String CANT;

        public static void initialize() {
            CONFIRM = colorize(getMessage("complaint-confirm"));
            EXPIRE = colorize(getMessage("complaint-expire"));
            ALREADY_EXPIRED = colorize(getMessage("complaint-already-expire"));
            SUBMIT = colorize(getMessage("complaint-submit"));
            CANT = colorize(getMessage("cant-complain"));
        }
    }

    public static class Log {
        public static String HEADER;
        public static String MESSAGE;

        public static void initialize() {
            HEADER = colorize(getMessage("log-message-header"));
            MESSAGE = colorize(getMessage("log-message"));
        }
    }

    public static class Arrest {
        public static String SUCCESSFULLY;
        public static String CANT;
        public static String PREVENT_SELF;
        public static String NOTIFICATION;
        public static String DISABLED;

        public static void initialize() {
            SUCCESSFULLY = colorize(getMessage("successfully-arrest"));
            CANT = colorize(getMessage("cant-arrest"));
            PREVENT_SELF = colorize(getMessage("self-arrest"));
            NOTIFICATION = colorize(getMessage("arrest-notification"));
            DISABLED = colorize(getMessage("disabled-arrest"));
        }
    }

    public static void initialize() {
        Main.getInstance().languageYML =
                new LanguageGenerator(Main.getInstance().getDataFolder() + File.separator + "language", Main.languageName);

        PREFIX = getMessage("PREFIX");
        NEED_PERMISSION = formatMessage(getMessage("need-permission"));
        NEED_GPS = formatMessage(getMessage("NEED_GPS"));
        PLAYER_NOT_FOUND = formatMessage(getMessage("player-not-found"));
        SELF_TARGET = formatMessage(getMessage("self-target"));
        SEARCH_TARGET = formatMessage(getMessage("search-target"));
        TARGET_WARN = formatMessage(getMessage("search-notification"));
        WANTED_TITLE = colorize(getMessage("wanted-title"));
        WANTED_LIST = colorize(getMessage("wanted-list"));
        WANTED_TOP = colorize(getMessage("wanted-top"));
        PLAYER_LEAVE_ON_FINDING = formatMessage(getMessage("player-leave-on-finding"));
        NO_WANTEDS = formatMessage(getMessage("no-wanteds"));
        MAXIMUM_WANTED_CHANGED = formatMessage(getMessage("maximum-wanted-changed"));
        PLUGIN_RELOADED = formatMessage(getMessage("plugin-reloaded"));
        ITEM_COOLDOWN = formatMessage(getMessage("item-cooldown"));
        GET_PLAYER_WANTED = formatMessage(getMessage("get-player-wanted"));
        OPERATION = formatMessage(getMessage("OPERATION"));
        CLEAR_OPERATOR = formatMessage(getMessage("clear-operator"));
        CLEAR_WANTED = formatMessage(getMessage("clear-wanted"));
        TAKE_WANTED = formatMessage(getMessage("take-wanted"));
        ADD_WANTED = formatMessage(getMessage("add-wanted"));
        SET_WANTED = formatMessage(getMessage("set-wanted"));
        INVALID_NUMBER = formatMessage(getMessage("valid-number"));
        PLAYER_WANTED = formatMessage(getMessage("player-wanted"));
        CONSOLE_SENDER = formatMessage(getMessage("console-sender"));
        ON_KILL_PLAYER = formatMessage(getMessage("message-on-kill-player"));
        ON_KILL_MOB = formatMessage(getMessage("message-on-kill-mob"));
        ON_KILL_NPC = formatMessage(getMessage("message-on-kill-npc"));
        LOADING_DATA = formatMessage(getMessage("loading-data"));
        WANTED_SYMBOL = colorize(getMessage("wanted-symbol"));
        PLAYER_WANTED_LIST_FORMAT = colorize(getMessage("player-wanted-list-format"));
        DIFFERENT_WORLD = colorize(getMessage("different-world"));
    }

    public static void helpMessage1(CommandSender sender) {
        sender.sendMessage(Help.HEADER.replace("%version%", getPlugin().getDescription().getVersion()));
        TextComponent reloadCommand = new TextComponent(Help.RELOAD);
        reloadCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted reload"));
        sender.spigot().sendMessage(reloadCommand);
        TextComponent findCommand = new TextComponent(Help.FIND);
        findCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted find "));
        sender.spigot().sendMessage(findCommand);
        TextComponent setMaximumCommand = new TextComponent(Help.MAXIMUM);
        setMaximumCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted maximum "));
        sender.spigot().sendMessage(setMaximumCommand);
        TextComponent clearCommand = new TextComponent(Help.CLEAR);
        clearCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted clear "));
        sender.spigot().sendMessage(clearCommand);
        TextComponent setCommand = new TextComponent(Help.SET);
        setCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted set "));
        sender.spigot().sendMessage(setCommand);
        TextComponent takeCommand = new TextComponent(Help.TAKE);
        takeCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted take "));
        sender.spigot().sendMessage(takeCommand);
        TextComponent addCommand = new TextComponent(Help.ADD);
        addCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted add "));
        sender.spigot().sendMessage(addCommand);
        TextComponent wantedsCommand = new TextComponent(Help.WANTEDS);
        wantedsCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanteds"));
        sender.spigot().sendMessage(wantedsCommand);
        TextComponent textComponent = new TextComponent(Help.Page.NEXT_PAGE);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted help 2"));
        sender.spigot().sendMessage(textComponent);
    }

    public static void helpMessage2(CommandSender sender) {
        sender.sendMessage(Help.HEADER.replace("%version%", getPlugin().getDescription().getVersion()));
        TextComponent wantedGUICommand = new TextComponent(Help.GUI);
        wantedGUICommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted gui"));
        sender.spigot().sendMessage(wantedGUICommand);
        TextComponent getWantedCommand = new TextComponent(Help.GET);
        getWantedCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted get "));
        sender.spigot().sendMessage(getWantedCommand);
        TextComponent topWantedCommand = new TextComponent(Help.TOP);
        topWantedCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted top"));
        sender.spigot().sendMessage(topWantedCommand);
        TextComponent helpCommand = new TextComponent(Help.HELP);
        helpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wanted help "));
        sender.spigot().sendMessage(helpCommand);
        TextComponent complaintCommand = new TextComponent(Help.COMPLAINT);
        complaintCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/complaint"));
        sender.spigot().sendMessage(complaintCommand);
        TextComponent textComponent = new TextComponent(Help.Page.PREV_PAGE);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wanted help 1"));
        sender.spigot().sendMessage(textComponent);
    }

    public static String wantedSymbol(int count) {
        StringBuilder star = new StringBuilder();
        for (int i = 0; i < count; i++) {
            star.append(WANTED_SYMBOL);
        }
        return colorize(PLAYER_WANTED_LIST_FORMAT.replace("%symbol%", star));
    }

    public static String rawWantedSymbol(int count) {
        StringBuilder star = new StringBuilder();
        for (int i = 0; i < count; i++) {
            star.append(WANTED_SYMBOL);
        }
        return colorize(PLAYER_WANTED_LIST_FORMAT.replace("%symbol%", star));
    }

    public static String playerDeathLogger(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer == null) return null;
        Integer wanted = WantedManager.getInstance().getWanted(player.getName());
        if (wanted == null) return null;
        return "[Player] [" + Main.getInstance().log.formatMessage() + "] "
                + killer.getName() + " killed " + player.getName()
                + " in " + player.getWorld().getName()
                + " at X:" + (int) player.getLocation().getX()
                + " Y:" + (int) player.getLocation().getY()
                + " Z:" + (int) player.getLocation().getZ()
                + " | New Wanted: " + wanted;
    }

    public static String npcDeathLogger(NPCDeathEvent event) {
        Entity npc = event.getEvent().getEntity();
        Player killer = event.getEvent().getEntity().getKiller();
        if (killer == null) return null;
        Integer wanted = WantedManager.getInstance().getWanted(killer.getName());
        if (wanted == null) return null;
        return "[NPC] [" + Main.getInstance().log.formatMessage() + "] "
                + killer.getName() + " killed " + npc.getName()
                + " in " + npc.getWorld().getName()
                + " at X:" + (int) npc.getLocation().getX()
                + " Y:" + (int) npc.getLocation().getY()
                + " Z:" + (int) npc.getLocation().getZ()
                + " | New Wanted: " + wanted;
    }

}