package ir.syrent.wanted.Commands;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WantedCommand implements CommandExecutor {

    HashMap<Player, Player> getTarget = new HashMap<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        boolean isAdmin = sender.hasPermission("wanted.admin");
        if (label.equalsIgnoreCase("wanted")) {
            if (args.length > 0) {
                //Get Wanted
                if (args[0].equalsIgnoreCase("get")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getConsoleSender()));
                        return true;
                    }
                    if (!isAdmin) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getGetWantedUsage()));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == null) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getPlayerNotFound()));
                        return true;
                    }
                    sender.sendMessage(Utils.color(Main.getInstance().messages.getGetPlayerWanted()
                            .replace("%wanted%", String.valueOf(Main.getInstance().wantedMap.get(args[1])))
                            .replace("%player%", args[1])));
                    return true;
                }
                //WantedFinder
                if (args[0].equalsIgnoreCase("find")) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getConsoleSender()));
                        return true;
                    }
                    Player player = (Player) sender;
                    if (!(player.hasPermission("wanted.find") || isAdmin)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getFindUsage()));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (target == sender) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getSelfTarget()));
                        return true;
                    }
                    if (target == null) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getPlayerNotFound()));
                        return true;
                    }
                    if (!player.getInventory().contains(Material.COMPASS)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedGPS()));
                        return true;
                    }
                    player.sendMessage(Utils.color(Main.getInstance().messages.getSearchTarget().replace("%target%", target.getName())));
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission("wanted.notify")) {
                            if (sender == onlinePlayer) continue;
                            onlinePlayer.sendMessage(Utils.color(Main.getInstance().messages.getTargetWarn()
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
                            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                                if (!target.isOnline()) {
                                    player.sendMessage(Utils.color(Main.getInstance().messages.getPlayerLeaveOnFinding()
                                            .replace("%player%", target.getName())));
                                    cancel();
                                }
                                player.setCompassTarget(getTarget.get(player).getLocation());
                            });
                        }
                    }.runTaskTimer(Main.getInstance(), 0, Main.getInstance().getConfig().getInt("Wanted.CompassRefreshInterval"));
                    return true;
                }
                //Set maximum command
                if (args[0].equalsIgnoreCase("set-maximum")) {
                    if (!isAdmin) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getSetMaximumUsage()));
                        return true;
                    }
                    int number;
                    try {
                        if (Integer.parseInt(args[1]) < 1) {
                            sender.sendMessage(Utils.color(Main.getInstance().messages.getValidNumber()));
                            return true;
                        }
                        number = Integer.parseInt(args[1]);
                        Main.getInstance().getConfig().set("Wanted.Maximum", number);
                        Main.getInstance().saveConfig();
                        Main.getInstance().reloadConfig();
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getMaximumWantedChanged()));
                        return true;
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getValidNumber()));
                        return true;
                    }
                }
                //Reload command
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!isAdmin) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    sender.sendMessage(Utils.color(Main.getInstance().messages.getPluginReloaded()));
                    Main.getInstance().reloadConfig();
                    Main.getInstance().messagesYML.reloadConfig();
                    return true;
                }
                //ClearWanted command
                if (args[0].equalsIgnoreCase("clear")) {
                    if (!(sender.hasPermission("wanted.clear") || isAdmin)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getClearOperator()));
                        return true;
                    }
                    Main.getInstance().wantedMap.remove(args[1]);
                    SkullBuilder.getInstance().cache.remove(Bukkit.getPlayerExact(args[1]));
                    Main.getInstance().reloadData();
                    sender.sendMessage(Utils.color(Main.getInstance().messages.getClearWanted()));
                    return true;
                }
                //TakeWanted command
                if (args[0].equalsIgnoreCase("take")) {
                    if (!(sender.hasPermission("wanted.take") || isAdmin)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getOperation().replace("%action%", "Take")));
                        return true;
                    }
                    if (Main.getInstance().wantedMap.get(args[1]) == null) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getPlayerNotFound()));
                        return true;
                    }
                    int currentWanted = Main.getInstance().wantedMap.get(args[1]);
                    int newWanted;
                    try {
                        newWanted = Integer.parseInt(args[2]);
                        if (newWanted < 1) {
                            sender.sendMessage(Utils.color(Main.getInstance().messages.getValidNumber()));
                            return true;
                        }
                        Main.getInstance().wantedMap.remove(args[1]);
                        if ((currentWanted - newWanted) >= 1) {
                            Main.getInstance().wantedMap.put(args[1], (currentWanted - newWanted));
                            Main.getInstance().reloadData();
                        }
                        if (currentWanted - newWanted <= 0)
                            SkullBuilder.getInstance().cache.remove(Bukkit.getPlayerExact(args[1]));

                        sender.sendMessage(Utils.color(Main.getInstance().messages.getTakeWanted()));
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getValidNumber()));
                        return true;
                    }
                    return true;
                }
                //AddWanted command
                if (args[0].equalsIgnoreCase("add")) {
                    if (!(sender.hasPermission("wanted.add") || isAdmin)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getOperation().replace("%action%", "Add")));
                        return true;
                    }
                    Integer currentWanted = Main.getInstance().wantedMap.get(args[1]);
                    int newWanted;
                    int maximum = Main.getInstance().getConfig().getInt("Wanted.Maximum");
                    try {
                        newWanted = Integer.parseInt(args[2]);
                        if (currentWanted == null) currentWanted = 0;
                        Player target = Bukkit.getPlayerExact(args[1]);
                        if ((currentWanted + newWanted) > maximum) {
                            Main.getInstance().wantedMap.remove(args[1]);
                            Main.getInstance().wantedMap.put(args[1], maximum);
                            sender.sendMessage(Utils.color(Main.getInstance().messages.getAddWanted()));
                            return true;
                        }
                        Main.getInstance().wantedMap.remove(args[1]);
                        Main.getInstance().wantedMap.put(args[1], (currentWanted + newWanted));
                        Main.getInstance().reloadData();
                        SkullBuilder.getInstance().saveHead(target);

                        sender.sendMessage(Utils.color(Main.getInstance().messages.getAddWanted()));
                        return true;
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getValidNumber()));
                        return true;
                    }
                }
                //SetWanted command
                if (args[0].equalsIgnoreCase("set")) {
                    if (!(sender.hasPermission("wanted.set") || isAdmin)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getOperation().replace("%action%", "Set")));
                        return true;
                    }
                    int newWanted;
                    try {
                        newWanted = Integer.parseInt(args[2]);
                        if (newWanted < 1) {
                            Main.getInstance().wantedMap.remove(args[1]);
                            return true;
                        }
                        int maximum = Main.getInstance().getConfig().getInt("Wanted.Maximum");
                        if (newWanted > maximum) {
                            Main.getInstance().wantedMap.remove(args[1]);
                            Main.getInstance().wantedMap.put(args[1], maximum);
                            sender.sendMessage(Utils.color(Main.getInstance().messages.getAddWanted()));
                            return true;
                        }
                        Main.getInstance().wantedMap.remove(args[1]);
                        Main.getInstance().wantedMap.put(args[1], newWanted);
                        SkullBuilder.getInstance().saveHead(Bukkit.getPlayerExact(args[1]));
                        Main.getInstance().reloadData();
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getSetWanted()));
                    } catch (Exception e) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getValidNumber()));
                        return true;
                    }
                    return true;
                }
                //TopWanted command
                if (args[0].equalsIgnoreCase("top")) {
                    if (!(sender.hasPermission("wanted.top") || isAdmin)) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (Main.getInstance().wantedMap.isEmpty()) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNoWanteds()));
                        return true;
                    }

                    List<String> playerList = new ArrayList<>();
                    List<Integer> numberList = new ArrayList<>();
                    for (Map.Entry<String, Integer> getPlayer : Main.getInstance().wantedMap.entrySet()) {
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
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getWantedTop()).replace("%wanted%", String.valueOf(numberList.get(i)))
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
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getConsoleSender()));
                        return true;
                    }
                    Player player = (Player) sender;
                    if (!(sender.hasPermission("wanted.gui") || sender.hasPermission("wanted.admin"))) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }

                    Main.getInstance().requestGUI.open(player);
                    return true;
                }
                //Help command
                if (args[0].equalsIgnoreCase("help")) {
                    if (!(sender.hasPermission("wanted.help") || sender.hasPermission("wanted.admin"))) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
                        return true;
                    }
                    if (args.length == 2) {
                        if (args[1].equals("1")) {
                            Main.getInstance().messages.helpMessage1(sender);
                            return true;
                        }
                        if (args[1].equals("2")) {
                            Main.getInstance().messages.helpMessage2(sender);
                            return true;
                        }
                    }
                    Main.getInstance().messages.helpMessage1(sender);
                    return true;
                }
                //Debug command
                if (args[0].equalsIgnoreCase("debug")) {
                    if (!sender.hasPermission("wanted.admin")) {
                        sender.sendMessage(Utils.color(Main.getInstance().messages.getNeedPermission()));
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
                sender.sendMessage(Utils.color(Main.getInstance().messages.getConsoleSender()));
                return true;
            }
            int currentWanted;
            try {
                currentWanted = Main.getInstance().wantedMap.get(sender.getName());
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