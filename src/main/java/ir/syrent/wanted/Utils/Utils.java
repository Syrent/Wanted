package ir.syrent.wanted.Utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;

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

    public static String colorize(String string) {
        return IridiumColorAPI.process(string);
    }
}
