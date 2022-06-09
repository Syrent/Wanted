package ir.syrent.wanted.Dependencies;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.WantedManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return Main.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "wanted";
    }

    @Override
    public String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        //Wanted placeholder
        if (identifier.equals("wanted")) {
            if (WantedManager.getInstance().getWanteds().get(player.getName()) == null) return "";
            else if (WantedManager.getInstance().getWanted(player.getName()) == 0) return "";
            else if (WantedManager.getInstance().getWanted(player.getName()) <= 5)
                return Messages.rawWantedSymbol(WantedManager.getInstance().getWanted(player.getName()));
            else return String.valueOf(WantedManager.getInstance().getWanted(player.getName()));
        }

        return null;
    }
}
