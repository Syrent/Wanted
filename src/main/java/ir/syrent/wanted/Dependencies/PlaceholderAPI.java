package ir.syrent.wanted.Dependencies;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.WantedManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    public  String getAuthor() {
        return String.join(", ", Main.getInstance().getDescription().getAuthors());
    }

    @Override
    @NotNull
    public  String getIdentifier() {
        return "wanted";
    }

    @Override
    @NotNull
    public  String getVersion() {
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

        //Formatted Wanted placeholder
        if (identifier.equals("wanted_formatted")) {
            if (WantedManager.getInstance().getWanteds().get(player.getName()) == null) return "";
            else if (WantedManager.getInstance().getWanted(player.getName()) == 0) return "";
            else if (WantedManager.getInstance().getWanted(player.getName()) <= 5)
                return Messages.wantedSymbol(WantedManager.getInstance().getWanted(player.getName())).replace("%player%", player.getName());
            else
                return Messages.WANTED_LIST.replace("%wanted%", String.valueOf(WantedManager.getInstance().getWanted(player.getName()))).replace("%player%", player.getName());
        }


        //Formatted Wanted placeholder
        if (identifier.equals("wanted_count")) {
            if (WantedManager.getInstance().getWanteds().get(player.getName()) == null) return String.valueOf(0);
            else return String.valueOf(WantedManager.getInstance().getWanted(player.getName()));
        }

        return null;
    }
}
