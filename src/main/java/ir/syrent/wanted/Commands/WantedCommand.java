package ir.syrent.wanted.Commands;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.DataManager.MessagesYML;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class WantedCommand implements CommandExecutor {

    private static final Main plugin = Main.getPlugin(Main.class);
    MessagesYML messagesYML = new MessagesYML();
    Messages messages = new Messages();

    HashMap<Player, Player> getTarget = new HashMap<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        boolean isAdmin = sender.hasPermission("wanted.admin");
        if (label.equalsIgnoreCase("wanted")) {
            if (args.length > 0) {
                //Get Wanted
                if (args[0].equalsIgnoreCase("get")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.color(messages.getConsoleSender()));
                        return true;
                    }
                    if (!isAdmin) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(messages.getGetWantedUsage()));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        sender.sendMessage(Utils.color(messages.getPlayerNotFound()));
                        return true;
                    }
                    sender.sendMessage(Utils.color(messages.getGetPlayerWanted()
                            .replace("%wanted%", String.valueOf(plugin.wantedMap.get(args[1])))
                            .replace("%player%", args[1])));
                    return true;
                }
                //WantedFinder
                if (args[0].equalsIgnoreCase("find")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.color(messages.getConsoleSender()));
                        return true;
                    }
                    Player player = (Player) sender;
                    if (!(player.hasPermission("wanted.find") || isAdmin)) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(messages.getFindUsage()));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == sender) {
                        sender.sendMessage(Utils.color(messages.getSelfTarget()));
                        return true;
                    }
                    if (target == null) {
                        sender.sendMessage(Utils.color(messages.getPlayerNotFound()));
                        return true;
                    }
                    if (!player.getInventory().contains(Material.COMPASS)) {
                        sender.sendMessage(Utils.color(messages.getNeedGPS()));
                        return true;
                    }
                    player.sendMessage(Utils.color(messages.getSearchTarget().replace("%target%", target.getName())));
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("wanted.notify")) {
                            if (sender == onlinePlayer) continue;
                            onlinePlayer.sendMessage(Utils.color(messages.getTargetWarn()
                                    .replace("%player%", player.getName())
                                    .replace("%target%", target.getName())
                            ));
                        }
                    }
                    getTarget.remove(player);
                    getTarget.put(player, target);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                if (!target.isOnline()) {
                                    player.sendMessage(Utils.color(messages.getPlayerLeaveOnFinding()
                                            .replace("%player%", target.getName())));
                                    cancel();
                                }
                                player.setCompassTarget(getTarget.get(player).getLocation());
                            });
                        }
                    }.runTaskTimer(plugin, 0, plugin.getConfig().getInt("Wanted.CompassRefreshInterval"));
                    return true;
                }
                //Set maximum command
                if (args[0].equalsIgnoreCase("set-maximum")) {
                    if (!isAdmin) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(messages.getSetMaximumUsage()));
                        return true;
                    }
                    int number;
                    try {
                        if (Integer.parseInt(args[1]) < 1) {
                            sender.sendMessage(Utils.color(messages.getValidNumber()));
                            return true;
                        }
                        number = Integer.parseInt(args[1]);
                        plugin.getConfig().set("Wanted.Maximum", number);
                        plugin.saveConfig();
                        plugin.reloadConfig();
                        sender.sendMessage(Utils.color(messages.getMaximumWantedChanged()));
                        return true;
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(messages.getValidNumber()));
                        return true;
                    }
                }
                //Reload command
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!isAdmin) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    sender.sendMessage(Utils.color(messages.getPluginReloaded()));
                    plugin.reloadConfig();
                    messagesYML.reloadConfig();
                    return true;
                }
                //ClearWanted command
                if (args[0].equalsIgnoreCase("clear")) {
                    if (!(sender.hasPermission("wanted.clear") || isAdmin)) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(messages.getClearOperator()));
                        return true;
                    }
                    plugin.wantedMap.remove(args[1]);
                    sender.sendMessage(Utils.color(messages.getClearWanted()));
                    return true;
                }
                //TakeWanted command
                if (args[0].equalsIgnoreCase("take")) {
                    if (!(sender.hasPermission("wanted.take") || isAdmin)) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(Utils.color(messages.getOperation().replace("%action%", "Take")));
                        return true;
                    }
                    if (plugin.wantedMap.get(args[1]) == null) {
                        sender.sendMessage(Utils.color(messages.getPlayerNotFound()));
                        return true;
                    }
                    int currentWanted = plugin.wantedMap.get(args[1]);
                    int newWanted;
                    try {
                        newWanted = Integer.parseInt(args[2]);
                        if (newWanted < 1) {
                            sender.sendMessage(Utils.color(messages.getValidNumber()));
                            return true;
                        }
                        plugin.wantedMap.remove(args[1]);
                        if ((currentWanted - newWanted) >= 1) {
                            plugin.wantedMap.put(args[1], (currentWanted - newWanted));
                        }
                        sender.sendMessage(Utils.color(messages.getTakeWanted()));
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(messages.getValidNumber()));
                        return true;
                    }
                    return true;
                }
                //AddWanted command
                if (args[0].equalsIgnoreCase("add")) {
                    if (!(sender.hasPermission("wanted.add") || isAdmin)) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(Utils.color(messages.getOperation().replace("%action%", "Add")));
                        return true;
                    }
                    Integer currentWanted = plugin.wantedMap.get(args[1]);
                    int newWanted;
                    int maximum = plugin.getConfig().getInt("Wanted.Maximum");
                    try {
                        newWanted = Integer.parseInt(args[2]);
                        if (currentWanted == null) currentWanted = 0;
                        ItemStack playerHead = Main.getInstance().skullBuilder.getHead(args[1]);
                        if ((currentWanted + newWanted) > maximum) {
                            plugin.wantedMap.remove(args[1]);
                            plugin.wantedMap.put(args[1], maximum);
                            sender.sendMessage(Utils.color(messages.getAddWanted()));
                            return true;
                        }
                        plugin.wantedMap.remove(args[1]);
                        plugin.wantedMap.put(args[1], (currentWanted + newWanted));

                        if (!Main.getInstance().skullBuilder.cache.containsKey(Bukkit.getPlayerExact(args[1])))
                            Main.getInstance().skullBuilder.cache.put(Bukkit.getPlayerExact(args[1]), playerHead.serialize());

                        sender.sendMessage(Utils.color(messages.getAddWanted()));
                        return true;
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(messages.getValidNumber()));
                        return true;
                    }
                }
                //SetWanted command
                if (args[0].equalsIgnoreCase("set")) {
                    if (!(sender.hasPermission("wanted.set") || isAdmin)) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(Utils.color(messages.getOperation().replace("%action%", "Set")));
                        return true;
                    }
                    int newWanted;
                    try {
                        newWanted = Integer.parseInt(args[2]);
                        if (newWanted < 1) {
                            plugin.wantedMap.remove(args[1]);
                            return true;
                        }
                        int maximum = plugin.getConfig().getInt("Wanted.Maximum");
                        if (newWanted > maximum) {
                            plugin.wantedMap.remove(args[1]);
                            plugin.wantedMap.put(args[1], maximum);
                            sender.sendMessage(Utils.color(messages.getAddWanted()));
                            return true;
                        }
                        plugin.wantedMap.remove(args[1]);
                        plugin.wantedMap.put(args[1], newWanted);
                        sender.sendMessage(Utils.color(messages.getSetWanted()));
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(messages.getValidNumber()));
                        return true;
                    }
                    return true;
                }
                //TopWanted command
                if (args[0].equalsIgnoreCase("top")) {
                    if (!(sender.hasPermission("wanted.top") || isAdmin)) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (plugin.wantedMap.isEmpty()) {
                        sender.sendMessage(Utils.color(messages.getNoWanteds()));
                        return true;
                    }

                    List<String> playerList = new ArrayList<>();
                    List<Integer> numberList = new ArrayList<>();
                    for (Map.Entry<String, Integer> getPlayer : plugin.wantedMap.entrySet()) {
                        Player wantedPlayer = Bukkit.getPlayerExact(getPlayer.getKey());
                        if (wantedPlayer == null) continue;
                        playerList.add(getPlayer.getKey());
                        numberList.add(getPlayer.getValue());
                    }

                    int temp;
                    String temp2;
                    boolean sorted = false;
                    while (!sorted) {
                        sorted = true;
                        for (int i = 0; i < numberList.size() - 1; i++) {
                            if (numberList.get(i) < numberList.get(i + 1)) {
                                temp = numberList.get(i);
                                temp2 = playerList.get(i);
                                numberList.set(i, numberList.get(i + 1));
                                playerList.set(i, playerList.get(i + 1));
                                numberList.set(i + 1, temp);
                                playerList.set(i + 1, temp2);
                                sorted = false;
                            }
                        }
                    }
                    int counter = 1;
                    sender.sendMessage("§aTOP Wanted(s):");

                    for (int i = 0; i < numberList.size(); i++) {
                        sender.sendMessage(Utils.color(messages.getWantedTop()).replace("%wanted%", String.valueOf(numberList.get(i)))
                                .replace("%player%", playerList.get(i)).replace("%number%", String.valueOf(counter)));
                        if (counter == 10) {
                            break;
                        }
                        counter++;
                    }

                    playerList.clear();
                    numberList.clear();
                    return true;
                }
                //GUI command
                if (args[0].equalsIgnoreCase("gui")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.color(messages.getConsoleSender()));
                        return true;
                    }
                    Player player = (Player) sender;
                    if (!(sender.hasPermission("wanted.gui") || sender.hasPermission("wanted.admin"))) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }

                    Main.getInstance().requestGUI.open(player);
                    return true;
                }
                //Help command
                if (args[0].equalsIgnoreCase("help")) {
                    if (!(sender.hasPermission("wanted.help") || sender.hasPermission("wanted.admin"))) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 2) {
                        if (args[1].equals("1")) {
                            messages.helpMessage1(sender);
                            return true;
                        }
                        if (args[1].equals("2")) {
                            messages.helpMessage2(sender);
                            return true;
                        }
                    }
                    messages.helpMessage1(sender);
                    return true;
                }
                //Debug command
                if (args[0].equalsIgnoreCase("debug")) {
                    if (!sender.hasPermission("wanted.admin")) {
                        sender.sendMessage(Utils.color(messages.getNeedPermission()));
                        return true;
                    }
                    Bukkit.broadcastMessage(Main.getInstance().skullBuilder.cache.toString());
                    Player player = (Player) sender;
                    player.getInventory().addItem(Main.getInstance().skullBuilder.getHead(player));
                    for (Player fuckingBot : Bukkit.getOnlinePlayers()) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wanted add " + fuckingBot.getName() + " 3");
                    }
                    return true;
                }
            }
            //Wanted (default)
            if (!(sender instanceof Player)) {
                sender.sendMessage(Utils.color(messages.getConsoleSender()));
                return true;
            }
            int currentWanted;
            try {
                currentWanted = plugin.wantedMap.get(sender.getName());
                sender.sendMessage(Utils.color("§aYour wanted: §e[" + currentWanted + "§e]"));
                return true;
            } catch (Exception e) {
                sender.sendMessage(Utils.color("§aYour wanted: §e[0]"));
                return true;
            }
        }
        return true;
    }
}