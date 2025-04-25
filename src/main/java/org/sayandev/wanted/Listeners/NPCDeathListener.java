package org.sayandev.wanted.Listeners;

import org.sayandev.wanted.Main;
import org.sayandev.wanted.Messages.Messages;
import org.sayandev.wanted.Wanted;
import org.sayandev.wanted.WantedManager;
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
            WantedManager.getInstance().addWanted(killer, wanted, null);

            if (Main.getInstance().getConfig().getBoolean("Wanted.ReceiveOnKill.NPC.KillMessage")) {
                killer.sendMessage(Messages.ON_KILL_NPC
                        .replace("%npc_name%", event.getEvent().getEntity().getName()).replace("%wanted%", String.valueOf(wanted)));
            }

            Wanted.getInstance().runCommand(killer, event.getEvent().getEntity(), "NPC");

            if (Main.getInstance().getConfig().getBoolean("Wanted.Log")) {
                Main.getInstance().log.logToFile(Main.getInstance().log.logTime(), Messages.npcDeathLogger(event));
            }
        }
    }
}
