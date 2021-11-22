package ir.syrent.wanted.Utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;

public class Utils {
    public static String color(String string) {
        return IridiumColorAPI.process(string);
    }
}
