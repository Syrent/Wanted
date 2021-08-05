package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Wanted;
import ir.syrent.wanted.WantedManager;
import net.citizensnpcs.api.event.NPCDeathEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCDeathListener implements Listener {

    @EventHandler
    public void onNPCDeath(NPCDeathEvent event) {
        if (event.getEvent().getEntityType().equals(EntityType.PLAYER)) {
            if (!Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.NPC.Enable")) return;

            Player killer = event.getEvent().getEntity().getKiller();
            int wanted = Main.getInstance().getConfig().getInt("Wanted.ReceiveOnKill.NPC.Receive");
            WantedManager.getInstance().addWanted(killer, wanted);

            if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.NPC.KillMessage")) {
                killer.sendMessage(Main.getInstance().messages.getMessageOnKillNPC()
                        .replace("%npc_name%", event.getEvent().getEntity().getName()).replace("%wanted%", String.valueOf(wanted)));
            }

            Wanted.getInstance().runCommand(killer, event.getEvent().getEntity(), "NPC");

            Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Main.getInstance().messages.npcDeathLogger(event));
        }
    }
}
