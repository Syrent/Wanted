package ir.syrent.wanted.Commands;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.WantedManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WantedCommand implements CommandExecutor {

    HashMap<Player, Player> getTarget = new HashMap<>();
    HashMap<Player, BossBar> playerBossBarHashMap = new HashMap<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @Nullable String[] args) {
        boolean isAdmin = sender.hasPermission("wanted.admin");
        if (args.length > 0) {
            //Get Wanted
            if (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("look")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Main.getInstance().messages.getConsoleSender());
                    return true;
                }

                Player player = (Player) sender;

                if (!isAdmin || !player.hasPermission("wanted.get")) {
                    player.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (args.length == 1) {
                    player.sendMessage(Main.getInstance().messages.getGetWantedUsage());
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    player.sendMessage(Main.getInstance().messages.getPlayerNotFound());
                    return true;
                }

                int wanted = WantedManager.getInstance().getWanted(player);

                player.sendMessage(Main.getInstance().messages.getGetPlayerWanted()
                        .replace("%wanted%", String.valueOf(wanted))
                        .replace("%player%", args[1]));
                return true;
            }


            HashMap<Player, BossBar> bossBarHashMap = new HashMap<>();
            //Find Wanted
            if (args[0].equalsIgnoreCase("find")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Main.getInstance().messages.getConsoleSender());
                    return true;
                }

                Player player = (Player) sender;
                if (!(player.hasPermission("wanted.find") || isAdmin)) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (args.length == 1) {
                    sender.sendMessage(Main.getInstance().messages.getFindUsage());
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == sender) {
                    sender.sendMessage(Main.getInstance().messages.getSelfTarget());
                    return true;
                }
                if (target == null) {
                    sender.sendMessage(Main.getInstance().messages.getPlayerNotFound());
                    return true;
                }

                if (!player.getInventory().contains(Material.COMPASS)) {
                    sender.sendMessage(Main.getInstance().messages.getNeedGPS());
                    return true;
                }

                player.sendMessage(Main.getInstance().messages.getSearchTarget().replace("%target%", target.getName()));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.hasPermission("wanted.notify")) {
                        if (sender == onlinePlayer) continue;
                        onlinePlayer.sendMessage(Main.getInstance().messages.getTargetWarn()
                                .replace("%player%", player.getName())
                                .replace("%target%", target.getName())
                        );
                    }
                }

                getTarget.remove(player);
                getTarget.put(player, target);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!target.isOnline()) {
                            player.sendMessage(Main.getInstance().messages.getPlayerLeaveOnFinding()
                                    .replace("%player%", target.getName()));
                            cancel();
                        }
                        player.setCompassTarget(getTarget.get(player).getLocation());
                    }
                }.runTaskTimerAsynchronously(Main.getInstance(), 0,
                        Main.getInstance().getConfig().getInt("Wanted.CompassRefreshInterval"));

                if (!Main.getInstance().getConfig().getBoolean("Wanted.TackerBossBar.Enable")) return true;

                BossBar bossBar = Main.getInstance().getServer().createBossBar(
                        Main.getInstance().messages.getBarTitle().replace("%distance%",
                                String.valueOf((int) player.getLocation().distance(getTarget.get(player).getLocation()))),
                        BarColor.valueOf(Main.getInstance().messages.getBarColor()), BarStyle.valueOf(Main.getInstance().messages.getBarType()));
                playerBossBarHashMap.put(player, bossBar);
                playerBossBarHashMap.get(player).addPlayer(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!target.isOnline()) {
                            playerBossBarHashMap.get(player).removePlayer(player);
                            playerBossBarHashMap.remove(player);
                            cancel();
                            return;
                        }

                        playerBossBarHashMap.get(player).setTitle(Main.getInstance().messages.getBarTitle().replace("%distance%",
                                String.valueOf((int) player.getLocation().distance(getTarget.get(player).getLocation()))));
                    }
                }.runTaskTimerAsynchronously(Main.getInstance(), 0,
                        Main.getInstance().getConfig().getInt("Wanted.TackerBossBar.RefreshInterval"));

                return true;
            }

            //Maximum command
            if (args[0].equalsIgnoreCase("maximum")) {
                if (!isAdmin) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (args.length == 1) {
                    sender.sendMessage(Main.getInstance().messages.getSetMaximumUsage());
                    return true;
                }

                int number;
                try {
                    if (Integer.parseInt(args[1]) < 1) {
                        sender.sendMessage(Main.getInstance().messages.getValidNumber());
                        return true;
                    }
                    number = Integer.parseInt(args[1]);
                    Main.getInstance().getConfig().set("Wanted.Maximum", number);
                    Main.getInstance().saveConfig();
                    Main.getInstance().reloadConfig();
                    sender.sendMessage(Main.getInstance().messages.getMaximumWantedChanged());
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(Main.getInstance().messages.getValidNumber());
                    return true;
                }
            }

            //Reload command
            if (args[0].equalsIgnoreCase("reload")) {
                if (!isAdmin) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                Main.getInstance().reloadConfig();

                Main.languageName = Main.getInstance().getConfig().getString("Wanted.LanguageFile");
                Main.getInstance().messages.reload();
                File languageDirectory = new File(Main.getInstance().getDataFolder() + "/language");
                for (File configFile : languageDirectory.listFiles()) {

                    FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);

                    InputStream defaultStream = Main.getInstance().getResource("language/" + configFile.getName()  + ".yml");
                    if (defaultStream != null) {
                        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                        dataConfig.setDefaults(defaultConfig);
                    }
                }

                sender.sendMessage(Main.getInstance().messages.getPluginReloaded());
                return true;
            }

            //ClearWanted command
            if (args[0].equalsIgnoreCase("clear")) {
                if (!(sender.hasPermission("wanted.clear") || isAdmin)) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (args.length == 1) {
                    sender.sendMessage(Main.getInstance().messages.getClearOperator());
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Main.getInstance().messages.getPlayerNotFound());
                    return true;
                }

                WantedManager.getInstance().setWanted(target, 0);
                SkullBuilder.getInstance().cache.remove(target);

                sender.sendMessage(Main.getInstance().messages.getClearWanted());
                return true;
            }

            //TakeWanted command
            if (args[0].equalsIgnoreCase("take")) {
                if (!(sender.hasPermission("wanted.take") || isAdmin)) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (args.length < 3) {
                    sender.sendMessage(Main.getInstance().messages.getOperation().replace("%action%", "Take"));
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Main.getInstance().messages.getPlayerNotFound());
                    return true;
                }

                int wanted;
                try {
                    wanted = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Main.getInstance().messages.getValidNumber());
                    return true;
                }

                if (WantedManager.getInstance().takeWanted(target, wanted) == 0)
                    SkullBuilder.getInstance().cache.remove(Bukkit.getPlayerExact(args[1]));

                sender.sendMessage(Main.getInstance().messages.getTakeWanted());
                return true;
            }
            //AddWanted command
            if (args[0].equalsIgnoreCase("add")) {
                if (!(sender.hasPermission("wanted.add") || isAdmin)) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (args.length < 3) {
                    sender.sendMessage(Main.getInstance().messages.getOperation().replace("%action%", "Add"));
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Main.getInstance().messages.getPlayerNotFound());
                    return true;
                }

                int wanted;
                try {
                    wanted = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Main.getInstance().messages.getValidNumber());
                    return true;
                }

                if (WantedManager.getInstance().addWanted(target, wanted) != 0)
                    SkullBuilder.getInstance().saveHead(target);

                sender.sendMessage(Main.getInstance().messages.getTakeWanted());
                return true;
            }

            //SetWanted command
            if (args[0].equalsIgnoreCase("set")) {
                if (!(sender.hasPermission("wanted.set") || isAdmin)) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (args.length < 3) {
                    sender.sendMessage(Main.getInstance().messages.getOperation().replace("%action%", "Set"));
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Main.getInstance().messages.getPlayerNotFound());
                    return true;
                }

                int wanted;
                try {
                    wanted = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Main.getInstance().messages.getValidNumber());
                    return true;
                }

                if (WantedManager.getInstance().setWanted(target, wanted) != 0)
                    SkullBuilder.getInstance().saveHead(target);

                sender.sendMessage(Main.getInstance().messages.getSetWanted());
                return true;
            }

            //TopWanted command
            if (args[0].equalsIgnoreCase("top")) {
                if (!(sender.hasPermission("wanted.top") || isAdmin)) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                if (WantedManager.getInstance().getWanteds().isEmpty()) {
                    sender.sendMessage(Main.getInstance().messages.getNoWanteds());
                    return true;
                }

                List<String> playerList = new ArrayList<>();
                List<Integer> numberList = new ArrayList<>();
                for (Map.Entry<String, Integer> getPlayer : WantedManager.getInstance().getWanteds().entrySet()) {
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
                    sender.sendMessage(Main.getInstance().messages.getWantedTop().replace("%wanted%", String.valueOf(numberList.get(i)))
                            .replace("%player%", playerList.get(i)).replace("%number%", String.valueOf(counter)));

                    if (counter == 10) break;
                    counter++;
                }

                playerList.clear();
                numberList.clear();
                return true;
            }
            //GUI command
            if (args[0].equalsIgnoreCase("gui")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Main.getInstance().messages.getConsoleSender());
                    return true;
                }

                Player player = (Player) sender;
                if (!(sender.hasPermission("wanted.gui") || sender.hasPermission("wanted.admin"))) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
                    return true;
                }

                Main.getInstance().requestGUI.open(player);
                return true;
            }
            //Help command
            if (args[0].equalsIgnoreCase("help")) {
                if (!(sender.hasPermission("wanted.help") || sender.hasPermission("wanted.admin"))) {
                    sender.sendMessage(Main.getInstance().messages.getNeedPermission());
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
        }
        //Wanted (default)
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getInstance().messages.getConsoleSender());
            return true;
        }

        Player player = (Player) sender;

        if (!isAdmin || !player.hasPermission("wanted.use")) {
            player.sendMessage(Main.getInstance().messages.getNeedPermission());
            return true;
        }

        try {
            sender.sendMessage(Main.getInstance().messages.getPlayerWanted().replace("%wanted%",
                    String.valueOf(WantedManager.getInstance().getWanted(player))));
            return true;
        } catch (Exception e) {
            sender.sendMessage(Main.getInstance().messages.getPlayerWanted().replace("%wanted%", "0"));
            return true;
        }
    }
}