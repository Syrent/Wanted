package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.WantedManager;
import net.citizensnpcs.api.event.NPCDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCDeathListener implements Listener {

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        Bukkit.broadcastMessage("sosis");
        if (!Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.NPC.Enable")) return;

        Player killer = event.getEvent().getEntity().getKiller();
        int wanted = Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill.NPC.Receive");
        WantedManager.getInstance().addWanted(killer, wanted);

        if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.NPC.KillMessage")) {
            killer.sendMessage(Main.getInstance().messages.getMessageOnKillPlayer()
                    .replace("%npc_name%", event.getEvent().getEntity().getName()).replace("%wanted%", String.valueOf(wanted)));
        }

        Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Main.getInstance().messages.npcDeathLogger(event));
    }
}
