package org.sayandev.wanted.Utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.sayandev.wanted.Dependencies.PlaceholderAPI;
import org.sayandev.wanted.Dependencies.WorldGuard;
import org.sayandev.wanted.Listeners.NPCDeathListener;
import org.sayandev.wanted.Main;
import org.sayandev.wanted.Messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Utils {

    public static String getMessage(String path) {
        return Main.getInstance().getLanguageYML().getConfig().getString(path);
    }

    public static List<String> getMessageList(String path) {
        return Main.getInstance().getLanguageYML().getConfig().getStringList(path);
    }

    public static String formatMessage(String message) {
        return colorize(String.format("%s%s", Messages.PREFIX, message));
    }

    public static boolean hasPermission(Player player, boolean sendMessage, String... permissions) {
        return hasPermission((CommandSender) player, sendMessage, permissions);
    }

    public static boolean hasPermission(CommandSender sender, boolean sendMessage, String... permissions) {
        boolean hasPermission = false;
        for (String permission : permissions) {
            if (sender.hasPermission(permission)) hasPermission = true;
        }
        if (!hasPermission)
            if (sendMessage) sender.sendMessage(Messages.NEED_PERMISSION);
        return hasPermission;
    }

    public static void checkDependencies(String... plugins) {
        for (String plugin : plugins) {
            if (Bukkit.getPluginManager().getPlugin(plugin) != null) {
                Main.getInstance().getLogger().info(Utils.colorize(String.format("%s found! enabling hook...", plugin)));
                switch (plugin) {
                    case "PlaceholderAPI": {
                        new PlaceholderAPI().register();
                        Main.placeholderAPIFound = true;
                        break;
                    }
                    case "Citizens": {
                        Main.getInstance().getServer().getPluginManager().registerEvents(new NPCDeathListener(), Main.getInstance());
                        break;
                    }
                    case "WorldGuard": {
                        Main.worldGuard = new WorldGuard();
                        try {
                            Main.worldGuard.getWorldGuard();
                            Main.worldGuardFound = true;
                        } catch (NoClassDefFoundError ignored) {
                        }
                        break;
                    }
                }
                Main.getInstance().getLogger().info(Utils.colorize(String.format("%s hook enabled!", plugin)));
            } else {
                Main.getInstance().getLogger().info(Utils.colorize(String.format("%s not found! disabling hook...", plugin)));
                if (plugin.equals("PlaceholderAPI")) Main.placeholderAPIFound = false;
                if (plugin.equals("WorldGuard")) Main.worldGuardFound = false;
            }
        }
    }

    public static String colorize(String string) {
        return IridiumColorAPI.process(string);
    }
}
