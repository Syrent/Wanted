package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

public class InventoryClickListener implements Listener {

    HashSet<Player> playerCooldown = new HashSet<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().contains("WANTED GUI")) {
            event.setCancelled(true);

            //if (!(event.getSlot() == 46 || event.getSlot() == 52 || event.getSlot() == 49)) return;

            int currentPage = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle())
                    .substring(ChatColor.stripColor(event.getView().getTitle()).length() - 1));
            int maximumPage = Main.getInstance().playersGUI.size();

            if (event.getClickedInventory() == null) return;
            if (playerCooldown.contains(player)) {
                player.sendMessage(Main.getInstance().messages.getItemCooldown());
                return;
            }

            playerCooldown.add(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerCooldown.remove(player);
                }
            }.runTaskLaterAsynchronously(Main.getInstance(), 60L);

            if (event.getSlot() == 46 && event.getClickedInventory().getItem(46) != null) {
                //Previous Page
                if (currentPage != 1) {
                    event.getWhoClicked().openInventory(Main.getInstance().playersGUI.get(currentPage - 2));
                }
            } else if (event.getSlot() == 52 && event.getClickedInventory().getItem(52) != null) {
                //Next Page
                if (currentPage != maximumPage) {
                    event.getWhoClicked().openInventory(Main.getInstance().playersGUI.get(currentPage));
                }
            } else if (event.getSlot() == 49) {
                //Refresh
                event.getWhoClicked().openInventory(Main.getInstance().playersGUI.get(0));
            } else {
                boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name)
                        .collect(Collectors.toList()).contains("PLAYER_HEAD");
                Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

                if (event.getCurrentItem().getType().equals(type)) {
                    player.closeInventory();
                    Bukkit.dispatchCommand(player, "wanted find " + ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                }
            }
        }
    }

}
