package ir.syrent.wanted.Dependencies;

import ir.syrent.wanted.Main;
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
    public @NotNull String getAuthor() {
        return Main.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "wanted";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (player == null) {
            return "";
        }

        //Wanted placeholder
        if (identifier.equals("wanted")) {
            if (WantedManager.getInstance().getWanteds().get(player.getName()) == null) return "0";
            else if (WantedManager.getInstance().getWanted(player) == 0) return "";
            else if (WantedManager.getInstance().getWanted(player) <= 5)
                return Main.getInstance().messages.rawWantedSymbol(WantedManager.getInstance().getWanted(player));
            else return String.valueOf(WantedManager.getInstance().getWanted(player));
        }

        return null;
    }
}
