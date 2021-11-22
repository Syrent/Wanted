package ir.syrent.wanted.Commands;

import ir.syrent.wanted.Events.PlayerDeathListenerComplaint;
import ir.syrent.wanted.Main;
import ir.syrent.wanted.WantedManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;

public class ComplaintCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Main.getInstance().messages.getConsoleSender());
            return true;
        }

        Player player = (Player) commandSender;
        if (!player.hasPermission("wanted.admin") && !player.hasPermission("wanted.complaint")) {
            player.sendMessage(Main.getInstance().messages.getNeedPermission());
            return true;
        }

        if (PlayerDeathListenerComplaint.playerComplaintMap.containsKey(player.getName())) {
            if (System.currentTimeMillis() > PlayerDeathListenerComplaint.playerComplaintMap.get(player.getName())) {
                player.sendMessage(Main.getInstance().messages.getComplaintAlreadyExpire());
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
                    if (killerPlayer.hasPermission("wanted.bypass")) break;
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
                    if (killerPlayer != null) {
                        if (!Main.getInstance().skullBuilder.cache.containsKey(killerPlayer)) {
                            Main.getInstance().skullBuilder.cache.put(killerPlayer, killerPlayer.serialize());
                        }
                    }

                    int defaultReceive = Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill.Player.Receive");
                    WantedManager.getInstance().addWanted(killerPlayer, defaultReceive);
                    finalWanted = defaultReceive;
                }

                if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.KillMessage")) {
                    killerPlayer.sendMessage(Main.getInstance().messages.getMessageOnKillPlayer()
                            .replace("%player_name%", player.getName()).replace("%wanted%", String.valueOf(finalWanted))
                            .replace("%fight_starter%", fightStarter == null ? "UNKNOWN" : fightStarter)
                            .replace("%region%", Main.getInstance().worldGuard == null ?
                                    "UNKNOWN" : Main.getInstance().worldGuard.getRegionName(player.getLocation()) == null ?
                                    "UNKNOWN" : Main.getInstance().worldGuard.getRegionName(player.getLocation()))
                    );
                }
                Main.getInstance().playerVictimMap.put(killer, player.getName());

                if (Main.getInstance().playerDamagedMap.containsKey(killerPlayer.getName()))
                    Main.getInstance().playerDamagedMap.remove(killerPlayer.getName());
                else Main.getInstance().playerDamagedMap.remove(player.getName());

                break;
            }

            player.sendMessage(Main.getInstance().messages.getComplaintSubmit());

            return true;
        }

        player.sendMessage(Main.getInstance().messages.getCantComplaint());

        return false;
    }
}
