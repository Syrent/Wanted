package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if (!Main.getInstance().playerDamagedMap.containsKey(damaged)) {
                if (Main.getInstance().playerDamagedMap.containsValue(damaged)) return;
                Main.getInstance().playerDamagedMap.put(damaged, damager);
            }

        }

    }


}
