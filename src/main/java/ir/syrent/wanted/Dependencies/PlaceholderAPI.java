package ir.syrent.wanted.Dependencies;

import ir.syrent.wanted.Main;
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
            if (Main.getInstance().wantedMap.get(player.getName()) == null || Main.getInstance().wantedMap.get(player.getName()) == 0) return "0";
            else if (Main.getInstance().wantedMap.get(player.getName()) <= 5)
                return Main.getInstance().messages.rawWantedSymbol(Main.getInstance().wantedMap.get(player.getName()));
            else return String.valueOf(Main.getInstance().wantedMap.get(player.getName()));
        }

        return null;
    }
}
