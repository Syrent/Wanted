package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.WantedManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamage(EntityDeathEvent event) {
        if (!Main.getInstance().getConfig().getBoolean("MobWanted.enable")) return;

        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            List<String> list = Main.getInstance().getConfig().getStringList("MobWanted.mobs");
            for (String mob : list) {
                String[] split = mob.split(";");
                if (event.getEntity().getType().equals(EntityType.valueOf(split[0]))) {
                    WantedManager.getInstance().addWanted(player, Integer.parseInt(split[1]));
                }
            }
        }
    }
}
