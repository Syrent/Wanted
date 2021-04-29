package ir.syrent.wanted.Commands;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WantedsCommand implements CommandExecutor {

    private final static Main plugin = Main.getPlugin(Main.class);
    Messages messages = new Messages();
    List<String> message = new ArrayList<>();
    int number = 0;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        boolean isAdmin = sender.hasPermission("wanted.admin");
        if (label.equalsIgnoreCase("wanteds")) {
            if (!(sender.hasPermission("wanted.list") || isAdmin)) {
                sender.sendMessage(Utils.color(messages.getNeedPermission().replace("%prefix%", messages.getPrefix()).replace("%player%", sender.getName())));
                return true;
            }
            if (plugin.wantedMap.isEmpty()) {
                sender.sendMessage(Utils.color(messages.getNoWanteds()));
                return true;
            }

            for (Map.Entry<String, Integer> wantedPlayer : plugin.wantedMap.entrySet()) {
                if (Bukkit.getPlayerExact(wantedPlayer.getKey()) != null) number++;
            }
            if (number == 0) {
                sender.sendMessage(Utils.color(messages.getNoWanteds()));
                return true;
            }
            number = 0;

            sender.sendMessage(Utils.color(messages.getWantedTitle()));
            for (Map.Entry<String, Integer> wantedlist : plugin.wantedMap.entrySet()) {
                String key = wantedlist.getKey();

                Player wantedPlayer = Bukkit.getPlayerExact(key);
                if (wantedPlayer == null) continue;

                int maximum = plugin.getConfig().getInt("Wanted.Maximum");
                int currentWanted = plugin.wantedMap.get(key);

                if (currentWanted <= 0 || currentWanted > maximum) continue;
                int count = plugin.wantedMap.get(key);

                if (maximum <= 5) {
                    message.add(Utils.color(messages.wantedSymbol(currentWanted).replace("%player%", key)));
                } else {
                    message.add(Utils.color(messages.getWantedList()).replace("%player%", key).replace("%wanted%", String.valueOf(count)));
                }
            }

            int counter = 0;
            for (String messageList : message) {
                sender.sendMessage(messageList);
                if (counter == 10) break;
                counter++;
            }
            message.clear();

        }
        return false;
    }
}
