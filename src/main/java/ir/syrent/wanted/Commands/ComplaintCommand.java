package ir.syrent.wanted.Commands;

import ir.syrent.wanted.Events.PlayerDeathListenerComplaint;
import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.Utils;
import ir.syrent.wanted.WantedManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class ComplaintCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.CONSOLE_SENDER);
            return true;
        }

        Player player = (Player) sender;

        if (!Utils.hasPermission(player, true, Permissions.ADMIN, Permissions.COMPLAINT)) return true;

        if (PlayerDeathListenerComplaint.playerComplaintMap.containsKey(player.getName())) {
            if (System.currentTimeMillis() > PlayerDeathListenerComplaint.playerComplaintMap.get(player.getName())) {
                player.sendMessage(Messages.Complaint.ALREADY_EXPIRED);
                return true;
            }

            PlayerDeathListenerComplaint.playerComplaintMap.remove(player.getName());

            int finalWanted;
            String killer = PlayerDeathListenerComplaint.playerComplaintKillerMap.get(player.getName());
            int wanted = WantedManager.getInstance().getWanted(killer);

            String fightStarter = Main.getInstance().playerDamagedMap.containsKey(player.getName()) ?
                    Main.getInstance().playerDamagedMap.get(player.getName()) : Main.getInstance().playerDamagedMap.get(killer);

            for (PermissionAttachmentInfo permissionList : player.getEffectivePermissions()) {
                Player killerPlayer = Bukkit.getPlayerExact(killer);
                if (killerPlayer != null) {
                    if (Utils.hasPermission(killerPlayer, false, Permissions.BYPASS)) break;
                }

                if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.PreventReceiveIfDefender")) {
                    if (!killer.equals(fightStarter)) break;
                }
                String wantedPermission = permissionList.getPermission();

                if (wantedPermission.contains("wanted") && wantedPermission.contains("receive")) {
                    String[] permissionSplit = wantedPermission.split("\\.");
                    int number = Integer.parseInt(permissionSplit[2]);
                    if (WantedManager.getInstance().getWanted(player.getName()) != 0) {
                        WantedManager.getInstance().setWanted(player.getName(), 0);
                    }

                    Main.getInstance().skullBuilder.cache.remove(player);

                    if (killerPlayer != null) {
                        if (!Main.getInstance().skullBuilder.cache.containsKey(killerPlayer)) {
                            Main.getInstance().skullBuilder.cache.put(killerPlayer, killerPlayer.serialize());
                        }
                    }

                    WantedManager.getInstance().setWanted(killer, (wanted + number));
                    finalWanted = wanted + number;
                } else {
                    int defaultReceive = Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill.Player.Receive");

                    if (killerPlayer != null) {
                        if (!Main.getInstance().skullBuilder.cache.containsKey(killerPlayer)) {
                            Main.getInstance().skullBuilder.cache.put(killerPlayer, killerPlayer.serialize());
                        }
                        WantedManager.getInstance().addWanted(killerPlayer, defaultReceive);
                    }

                    finalWanted = defaultReceive;
                }

                if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.KillMessage")) {
                    killerPlayer.sendMessage(Messages.ON_KILL_PLAYER
                            .replace("%player_name%", player.getName()).replace("%wanted%", String.valueOf(finalWanted))
                            .replace("%fight_starter%", fightStarter == null ? "UNKNOWN" : fightStarter)
                            .replace("%region%", !Main.worldGuardFound ?
                                    "UNKNOWN" : Main.worldGuard.getRegionName(player.getLocation()) == null ?
                                    "UNKNOWN" : Main.worldGuard.getRegionName(player.getLocation()))
                    );
                }
                Main.getInstance().playerVictimMap.put(killer, player.getName());

                if (Main.getInstance().playerDamagedMap.containsKey(killerPlayer.getName()))
                    Main.getInstance().playerDamagedMap.remove(killerPlayer.getName());
                else Main.getInstance().playerDamagedMap.remove(player.getName());

                break;
            }

            player.sendMessage(Messages.Complaint.SUBMIT);

            return true;
        }

        player.sendMessage(Messages.Complaint.CANT);

        return false;
    }
}
