package org.sayandev.wanted.Listeners;

import org.sayandev.wanted.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if (!Main.getInstance().playerDamagedMap.containsKey(damaged.getName())) {
                if (Main.getInstance().playerDamagedMap.containsValue(damaged.getName())) return;
                Main.getInstance().playerDamagedMap.put(damaged.getName(), damager.getName());
            }

        }
    }

    @EventHandler
    public void onShoot(ProjectileHitEvent event) {
        try {
            if (event.getEntity().getShooter() instanceof Player && event.getHitEntity() instanceof Player) {
                Player damaged = (Player) event.getHitEntity();
                Player damager = (Player) event.getEntity().getShooter();

                if (!Main.getInstance().playerDamagedMap.containsKey(damaged.getName())) {
                    if (Main.getInstance().playerDamagedMap.containsValue(damaged.getName())) return;
                    Main.getInstance().playerDamagedMap.put(damaged.getName(), damager.getName());
                }

            }
        } catch (Exception ignored) {
            /*ProjectileHitEvent#getHitEntity doesn't exist in 1.8,
            as the Wanted code is discontinued i don't want to waste time on fixing it properly,
            so for now a simple try/catch and ignoring the result should do the job */
        }
    }


}
