package ir.syrent.wanted.Utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
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

    public static String colorize(String string) {
        return IridiumColorAPI.process(string);
    }
}
