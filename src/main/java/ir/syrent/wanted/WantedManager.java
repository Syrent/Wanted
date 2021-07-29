package ir.syrent.wanted;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WantedManager {

    private final Map<String, Integer> wanteds = new HashMap<>();

    private ConfigurationSection wantedSection;

    private static WantedManager instance;
    public static WantedManager getInstance() {
        return instance;
    }

    public WantedManager() {
        instance = this;

        wantedSection = Main.getInstance().wantedsYML.getConfig().getConfigurationSection("wanteds");
        if (wantedSection == null) wantedSection = Main.getInstance().wantedsYML.getConfig().createSection("wanteds");

        for (String playerName : wantedSection.getKeys(false)) {
            wanteds.put(playerName, wantedSection.getInt(playerName));
        }
    }

    public int addWanted(Player player, int wanted) {
        int currentWanted = getWanted(player);
        int maximumWanted = Main.getInstance().getConfig().getInt("Wanted.Maximum");

        int newWanted = Math.min(Math.max(currentWanted + wanted, 0), maximumWanted);

        wantedSection.set(player.getName(), newWanted == 0 ? null : newWanted);
        if (newWanted == 0) wanteds.remove(player.getName());
        else wanteds.put(player.getName(), newWanted);

        Main.getInstance().wantedsYML.saveConfig();

        return newWanted;
    }

    public int setWanted(Player player, int wanted) {
        int maximumWanted = Main.getInstance().getConfig().getInt("Wanted.Maximum");

        int newWanted = Math.min(Math.max(wanted, 0), maximumWanted);

        wantedSection.set(player.getName(), newWanted == 0 ? null : newWanted);
        if (newWanted == 0) wanteds.remove(player.getName());
        else wanteds.put(player.getName(), newWanted);

        Main.getInstance().wantedsYML.saveConfig();

        return newWanted;
    }

    public int takeWanted(Player player, int wanted) {
        int currentWanted = getWanted(player);
        int maximumWanted = Main.getInstance().getConfig().getInt("Wanted.Maximum");

        int newWanted = Math.min(Math.max(currentWanted - wanted, 0), maximumWanted);

        wantedSection.set(player.getName(), newWanted == 0 ? null : newWanted);
        if (newWanted == 0) wanteds.remove(player.getName());
        else wanteds.put(player.getName(), newWanted);

        Main.getInstance().wantedsYML.saveConfig();

        return newWanted;
    }

    public int getWanted(Player player) {
        if (wanteds.containsKey(player.getName()))
            return wanteds.get(player.getName());
        else
        if (wantedSection.contains(player.getName()))
            return wantedSection.getInt(player.getName());
        else
            return 0;
    }

    public Map<String, Integer> getWanteds() {
        return wanteds;
    }

}