package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Wanted;
import ir.syrent.wanted.WantedManager;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.List;

public class PlayerDeathListenerComplaint implements Listener {

    public static HashMap<String, Long> playerComplaintMap = new HashMap<>();
    public static HashMap<String, String> playerComplaintKillerMap = new HashMap<>();

    @EventHandler
    public void getWanted(PlayerDeathEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("Wanted.ComplaintMode.Enable")) return;

        Player victim = event.getEntity();
        if (!victim.hasPermission("wanted.complaint")) return;

        if (Main.getInstance().getConfig().getBoolean("Wanted.WorldGuard.Enable") && Main.getInstance().worldGuard != null) {
            List<String> regions = Main.getInstance().getConfig().getStringList("Wanted.WorldGuard.BlacklistRegions");

            if (Main.getInstance().getConfig().getBoolean("Wanted.WorldGuard.RevertBlacklist")) {
                if (!Main.getInstance().worldGuard.isRegionBlacklisted(regions, victim.getLocation())) return;
            } else if (Main.getInstance().worldGuard.isRegionBlacklisted(regions, victim.getLocation())) return;
        }

        if (Bukkit.getPluginManager().getPlugin("Citizens") != null)
            if (CitizensAPI.getNPCRegistry().isNPC(victim)) return;
        Player killer = victim.getKiller();
        if (killer.hasPermission("wanted.bypass")) return;

        String fightStarter = Main.getInstance().playerDamagedMap.containsKey(victim.getName()) ?
                Main.getInstance().playerDamagedMap.get(victim.getName()) : Main.getInstance().playerDamagedMap.get(killer.getName());
        if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.Player.PreventReceiveIfDefender")) {
            if (Main.getInstance().playerDamagedMap.containsKey(killer.getName())) {
                Main.getInstance().playerDamagedMap.remove(killer.getName());
            } else {
                Main.getInstance().playerDamagedMap.remove(victim.getName());
            }
            if (victim.getName().equals(fightStarter)) return;
        }

        Wanted.getInstance().runCommand(killer, victim, "Player");
        if (killer.hasPermission("wanted.hunter"))
            Wanted.getInstance().runCommand(killer, victim, "Wanted");

        victim.sendMessage(Main.getInstance().messages.getComplaintConfirm()
                .replace("%killer%", killer.getName())
        );

        long expireTime = Main.getInstance().getConfig().getLong("Wanted.ComplaintMode.ExpireTime");
        playerComplaintMap.put(victim.getName(), System.currentTimeMillis() + (expireTime * 1000L));
        playerComplaintKillerMap.put(victim.getName(), killer.getName());

        if (Main.getInstance().getConfig().getBoolean("Wanted.ClearWantedOnDeath")) {
            if (WantedManager.getInstance().getWanted(victim.getName()) != 0)
                WantedManager.getInstance().setWanted(victim.getName(), 0);
            Main.getInstance().skullBuilder.cache.remove(victim);
            return;
        }


        Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Main.getInstance().messages.playerDeathLogger(event));
    }
}