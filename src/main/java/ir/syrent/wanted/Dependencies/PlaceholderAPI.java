package ir.syrent.wanted.Dependencies;

import ir.syrent.wanted.Core.Wanted;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final Wanted plugin;

    public PlaceholderAPI(Wanted plugin) {
        this.plugin = plugin;
    }

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
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "wanted";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        if (player == null) {
            return "";
        }

        //Wanted placeholder
        if (identifier.equals("wanted")) {
            if (plugin.getSetWanted().get(player.getName()) == null) return "0";
            return String.valueOf(plugin.getSetWanted().get(player.getName()));
        }

        return null;
    }
}
