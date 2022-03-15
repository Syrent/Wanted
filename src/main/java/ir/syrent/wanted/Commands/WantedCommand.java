package ir.syrent.wanted.Commands;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.Utils;
import ir.syrent.wanted.Wanted;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WantedCommand implements CommandExecutor {

    public static HashMap<Player, Player> getTarget = new HashMap<>();
    public static HashMap<Player, BossBar> playerBossBarHashMap = new HashMap<>();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @Nullable String[] args) {
        if (args.length > 0) {
            //Get Wanted
            if (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("look")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.CONSOLE_SENDER);
                    return true;
                }

                Player player = (Player) sender;

                if (!Utils.hasPermission(player, true, Permissions.GET, Permissions.ADMIN)) return true;

                if (args.length == 1) {
                    player.sendMessage(Messages.Usage.GET_MAXIMUM);
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    player.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                int wanted = WantedManager.getInstance().getWanted(target.getName());

                player.sendMessage(Messages.GET_PLAYER_WANTED
                        .replace("%wanted%", String.valueOf(wanted))
                        .replace("%player%", args[1]));
                return true;
            }


            //Find Wanted
            if (args[0].equalsIgnoreCase("find")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.CONSOLE_SENDER);
                    return true;
                }

                Player player = (Player) sender;
                if (!Utils.hasPermission(player, true, Permissions.ADMIN, Permissions.FIND)) return true;

                if (args.length == 1) {
                    getTarget.remove(player);
                    if (playerBossBarHashMap.containsKey(player)) playerBossBarHashMap.get(player).removePlayer(player);
                    playerBossBarHashMap.remove(player);
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == sender) {
                    sender.sendMessage(Messages.SELF_TARGET);
                    return true;
                }

                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                if (!player.getWorld().getName().equals(target.getWorld().getName())) {
                    sender.sendMessage(Messages.DIFFERENT_WORLD);
                    return true;
                }

                if (!player.getInventory().contains(Material.COMPASS)) {
                    sender.sendMessage(Messages.NEED_GPS);
                    return true;
                }

                player.sendMessage(Messages.SEARCH_TARGET.replace("%target%", target.getName()));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (Utils.hasPermission(player, false, Permissions.NOTIFY)) {
                        if (sender == onlinePlayer) continue;
                        onlinePlayer.sendMessage(Messages.TARGET_WARN
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
                        if (!target.isOnline() || !getTarget.containsKey(player)) {
                            player.sendMessage(Messages.PLAYER_LEAVE_ON_FINDING
                                    .replace("%player%", target.getName()));
                            cancel();
                            return;
                        }
                        player.setCompassTarget(getTarget.get(player).getLocation());
                    }
                }.runTaskTimerAsynchronously(Main.getInstance(), 0,
                        Main.getInstance().getConfig().getInt("Wanted.CompassRefreshInterval"));

                if (!Main.getInstance().getConfig().getBoolean("Wanted.TrackerBossBar.Enable")) return true;

                if (playerBossBarHashMap.containsKey(player)) playerBossBarHashMap.get(player).removePlayer(player);
                playerBossBarHashMap.remove(player);
                BossBar bossBar = Main.getInstance().getServer().createBossBar(
                        Messages.BossBar.TITLE.replace("%distance%",
                                String.valueOf((int) player.getLocation().distance(getTarget.get(player).getLocation()))),
                        BarColor.valueOf(Main.getInstance().getConfig().getString("Wanted.TrackerBossBar.Default.Color")),
                        BarStyle.valueOf(Main.getInstance().getConfig().getString("Wanted.TrackerBossBar.Default.Type")));

                playerBossBarHashMap.put(player, bossBar);
                playerBossBarHashMap.get(player).addPlayer(player);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        BossBar playerBar = playerBossBarHashMap.get(player);
                        if (!target.isOnline()) {
                            if (playerBossBarHashMap.containsKey(player)) playerBar.removePlayer(player);
                            playerBossBarHashMap.remove(player);
                            cancel();
                            return;
                        }
                        if (!playerBossBarHashMap.containsKey(player)) {
                            cancel();
                            return;
                        }

                        double playerDistance = player.getLocation().distance(getTarget.get(player).getLocation());
                        playerBossBarHashMap.get(player).setTitle(Messages.BossBar.TITLE
                                .replace("%distance%", String.valueOf((int) playerDistance)));

                        for (String barID : Main.getInstance().getConfig().getConfigurationSection("Wanted.TrackerBossBar.Custom").getKeys(false)) {
                            int distance = Main.getInstance().getConfig().getInt("Wanted.TrackerBossBar.Custom." + barID + ".Distance");
                            boolean isProgressive = Main.getInstance().getConfig().getBoolean("Wanted.TrackerBossBar.Custom." + barID + ".Progress");
                            BarColor barColor = BarColor.valueOf(
                                    Main.getInstance().getConfig().getString("Wanted.TrackerBossBar.Custom." + barID + ".Color"));
                            BarStyle barStyle = BarStyle.valueOf(
                                    Main.getInstance().getConfig().getString("Wanted.TrackerBossBar.Custom." + barID + ".Type"));

                            if ((int) playerDistance <= distance) {
                                playerBar.setColor(barColor);
                                playerBar.setStyle(barStyle);
                                if (playerDistance <= 100) {
                                    if (isProgressive)
                                        playerBar.setProgress(playerDistance / 100);
                                }
                                return;
                            }
                            if (playerBar.getProgress() != 1) playerBar.setProgress(1);
                        }

                        playerBossBarHashMap.get(player).setColor(BarColor.valueOf(
                                Main.getInstance().getConfig().getString("Wanted.TrackerBossBar.Default.Color")));
                        playerBossBarHashMap.get(player).setStyle(BarStyle.valueOf(
                                Main.getInstance().getConfig().getString("Wanted.TrackerBossBar.Default.Type")));
                    }
                }.runTaskTimerAsynchronously(Main.getInstance(), 0,
                        Main.getInstance().getConfig().getInt("Wanted.TrackerBossBar.RefreshInterval"));

                return true;
            }

            //Arrest Wanted
            if (args[0].equalsIgnoreCase("arrest")) {
                if (!Main.getInstance().getConfig().getBoolean("Wanted.ArrestMode.Enable")) {
                    sender.sendMessage(Messages.Arrest.DISABLED);
                    return true;
                }

                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.CONSOLE_SENDER);
                    return true;
                }

                Player player = (Player) sender;
                if (!Utils.hasPermission(player, true, Permissions.ARREST, Permissions.ADMIN)) return true;

                if (args.length == 1) {
                    player.sendMessage(Messages.Usage.ARREST);
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                if (WantedManager.getInstance().getWanted(player.getName()) >= 0) {
                    if (Main.getInstance().getConfig().getBoolean("Wanted.ArrestMode.PreventSelfArrest")) {
                        if (target == sender) {
                            sender.sendMessage(Messages.Arrest.PREVENT_SELF);
                            return true;
                        }
                    }

                    if (player.getLocation().distance(target.getLocation()) > Main.getInstance().getConfig().getDouble("Wanted.ArrestMode.Distance")) {
                        player.sendMessage(Messages.Arrest.CANT);
                        return true;
                    }

                    player.sendMessage(Messages.Arrest.SUCCESSFULLY.replace("%target%", target.getName()));
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (Utils.hasPermission(player, false, Permissions.ARREST_NOTIFY)) {
                            if (sender == onlinePlayer) continue;
                            onlinePlayer.sendMessage(Messages.TARGET_WARN
                                    .replace("%player%", player.getName())
                                    .replace("%target%", target.getName())
                            );
                        }
                    }

                    Wanted.getInstance().runArrestCommand(player, target);

                    getTarget.remove(player);

                    if (playerBossBarHashMap.containsKey(player)) playerBossBarHashMap.get(player).removePlayer(player);
                    playerBossBarHashMap.remove(player);
                    WantedManager.getInstance().setWanted(target.getName(), 0);
                } else {
                    sender.sendMessage(Messages.Arrest.CANT);
                }

                return true;
            }

            //Maximum command
            if (args[0].equalsIgnoreCase("maximum")) {
                Player player = (Player) sender;
                if (!Utils.hasPermission(player, true, Permissions.ADMIN))
                    return true;

                if (args.length == 1) {
                    sender.sendMessage(Messages.Usage.SET_MAXIMUM);
                    return true;
                }

                int number;
                try {
                    if (Integer.parseInt(args[1]) < 1) {
                        sender.sendMessage(Messages.INVALID_NUMBER);
                        return true;
                    }
                    number = Integer.parseInt(args[1]);
                    Main.getInstance().getConfig().set("Wanted.Maximum", number);
                    Main.getInstance().saveConfig();
                    Main.getInstance().reloadConfig();
                    sender.sendMessage(Messages.MAXIMUM_WANTED_CHANGED);
                    return true;
                } catch (Exception e) {
                    sender.sendMessage(Messages.INVALID_NUMBER);
                    return true;
                }
            }

            //Reload command
            if (args[0].equalsIgnoreCase("reload")) {
                if (!Utils.hasPermission(sender, true, Permissions.ADMIN)) return true;

                Main.getInstance().reloadConfig();

                Main.languageName = Main.getInstance().getConfig().getString("Wanted.LanguageFile");
                new Messages();
                File languageDirectory = new File(Main.getInstance().getDataFolder() + "/language");
                for (File configFile : languageDirectory.listFiles()) {

                    FileConfiguration dataConfig = YamlConfiguration.loadConfiguration(configFile);

                    InputStream defaultStream = Main.getInstance().getResource("language/" + configFile.getName() + ".yml");
                    if (defaultStream != null) {
                        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                        dataConfig.setDefaults(defaultConfig);
                    }
                }

                sender.sendMessage(Messages.PLUGIN_RELOADED);
                return true;
            }

            //ClearWanted command
            if (args[0].equalsIgnoreCase("clear")) {

                if (!Utils.hasPermission(sender, true, Permissions.CLEAR, Permissions.ADMIN)) return true;

                if (args.length == 1) {
                    sender.sendMessage(Messages.CLEAR_OPERATOR);
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                WantedManager.getInstance().setWanted(target.getName(), 0);
                SkullBuilder.getInstance().cache.remove(target);

                sender.sendMessage(Messages.CLEAR_WANTED);
                return true;
            }

            //TakeWanted command
            if (args[0].equalsIgnoreCase("take")) {

                if (!Utils.hasPermission(sender, true, Permissions.TAKE, Permissions.ADMIN)) return true;

                if (args.length < 3) {
                    sender.sendMessage(Messages.OPERATION.replace("%action%", "Take"));
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                int wanted;
                try {
                    wanted = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.INVALID_NUMBER);
                    return true;
                }

                if (WantedManager.getInstance().takeWanted(target, wanted) == 0)
                    SkullBuilder.getInstance().cache.remove(Bukkit.getPlayerExact(args[1]));

                sender.sendMessage(Messages.TAKE_WANTED);
                return true;
            }
            //AddWanted command
            if (args[0].equalsIgnoreCase("add")) {

                if (!Utils.hasPermission(sender, true, Permissions.ADMIN, Permissions.ADD)) return true;

                if (args.length < 3) {
                    sender.sendMessage(Messages.OPERATION.replace("%action%", "add"));
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                int wanted;
                try {
                    wanted = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.INVALID_NUMBER);
                    return true;
                }

                if (WantedManager.getInstance().addWanted(target, wanted) != 0)
                    SkullBuilder.getInstance().saveHead(target);

                sender.sendMessage(Messages.ADD_WANTED);
                return true;
            }

            //SetWanted command
            if (args[0].equalsIgnoreCase("set")) {

                if (!Utils.hasPermission(sender, true, Permissions.SET, Permissions.ADMIN)) return true;

                if (args.length < 3) {
                    sender.sendMessage(Messages.OPERATION.replace("%action%", "set"));
                    return true;
                }

                Player target = Bukkit.getPlayerExact(args[1]);

                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return true;
                }

                int wanted;
                try {
                    wanted = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.INVALID_NUMBER);
                    return true;
                }

                if (WantedManager.getInstance().setWanted(target.getName(), wanted) != 0)
                    SkullBuilder.getInstance().saveHead(target);

                sender.sendMessage(Messages.SET_WANTED);
                return true;
            }

            //TopWanted command
            if (args[0].equalsIgnoreCase("top")) {
                if (!Utils.hasPermission(sender, true, Permissions.TOP, Permissions.ADMIN)) return true;

                if (WantedManager.getInstance().getWanteds().isEmpty()) {
                    sender.sendMessage(Messages.NO_WANTEDS);
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
                    sender.sendMessage(Messages.WANTED_TOP.replace("%wanted%", String.valueOf(numberList.get(i)))
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
                    sender.sendMessage(Messages.CONSOLE_SENDER);
                    return true;
                }

                Player player = (Player) sender;

                if (!Utils.hasPermission(player, true, Permissions.GUI, Permissions.ADMIN)) return true;

                Main.getInstance().requestGUI.open(player);
                return true;
            }

            //Log command
            if (args[0].equalsIgnoreCase("log")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Messages.CONSOLE_SENDER);
                    return true;
                }

                Player player = (Player) sender;

                if (!Utils.hasPermission(player, true, Permissions.LOG, Permissions.ADMIN)) return true;

                if (args.length < 3) {
                    player.sendMessage(Messages.Usage.LOG);
                    return true;
                }

                Pattern datePattern = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)");
                Pattern namePattern = Pattern.compile("(\\w*)[a-z]");
                Pattern locationPattern = Pattern.compile("X:(-\\d|\\d)* Y:(-\\d|\\d)* Z:(-\\d|\\d)*");
                Pattern wantedPattern = Pattern.compile("\\bN\\d*");

                for (File file : Main.getInstance().logDirectory.listFiles()) {
                    if (args[1].equals(file.getName())) {
                        try {
                            Scanner reader = new Scanner(file);
                            int counter = 0;

                            player.sendMessage(Messages.Log.HEADER
                                    .replace("%date%", file.getName())
                                    .replace("%range%", args[2]));
                            while (reader.hasNextLine()) {
                                if (counter == Integer.parseInt(args[2])) break;
                                String data = reader.nextLine()
                                        .replace("[Player] ", "")
                                        .replace("killed ", "")
                                        .replace("in ", "")
                                        .replace("at ", "")
                                        .replace(" New Wanted: ", "")
                                        .replace("|", "N");
                                Matcher dateMatcher = datePattern.matcher(data);
                                Matcher nameMatcher = namePattern.matcher(data);
                                Matcher locationMatcher = locationPattern.matcher(data);
                                Matcher wantedMatcher = wantedPattern.matcher(data);

                                String date = null;
                                String killer = null;
                                String victim = null;
                                String world = null;
                                String location = null;
                                String newWanted = null;

                                while (dateMatcher.find()) {
                                    date = dateMatcher.group(1);
                                }

                                int conditionCounter = 0;
                                while (nameMatcher.find()) {
                                    if (conditionCounter == 0) killer = nameMatcher.group();
                                    if (conditionCounter == 1) victim = nameMatcher.group();
                                    if (conditionCounter == 2) world = nameMatcher.group();
                                    conditionCounter++;
                                }

                                while (locationMatcher.find()) {
                                    location = locationMatcher.group();
                                }

                                while (wantedMatcher.find()) {
                                    newWanted = wantedMatcher.group().replace("N", "");
                                }

                                player.sendMessage(Messages.Log.MESSAGE
                                        .replace("%time%", date)
                                        .replace("%killer%", killer)
                                        .replace("%victim%", victim)
                                        .replace("%world%", world)
                                        .replace("%location%", location)
                                        .replace("%wanted%", newWanted));

                                counter++;
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return true;
            }
            //Help command
            if (args[0].equalsIgnoreCase("help")) {

                if (!Utils.hasPermission(sender, true, Permissions.HELP, Permissions.ADMIN)) return true;

                if (args.length == 2) {
                    if (args[1].equals("2")) {
                        Messages.helpMessage2(sender);
                        return true;
                    }
                }

                Messages.helpMessage1(sender);
                return true;
            }
        }
        //Wanted (default)
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.CONSOLE_SENDER);
            return true;
        }

        Player player = (Player) sender;

        if (!Utils.hasPermission(player, true, Permissions.USE, Permissions.ADMIN)) return true;

        try {
            sender.sendMessage(Messages.PLAYER_WANTED.replace("%wanted%",
                    String.valueOf(WantedManager.getInstance().getWanted(player.getName()))));
        } catch (Exception e) {
            sender.sendMessage(Messages.PLAYER_WANTED.replace("%wanted%", "0"));
        }

        return false;
    }
}