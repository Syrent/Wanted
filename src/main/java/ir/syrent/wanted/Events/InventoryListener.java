package ir.syrent.wanted.Events;

import ir.syrent.wanted.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Wanteds List")) {
            event.setCancelled(true);
            //TODO: Remove Debug messages
            Bukkit.broadcastMessage("Event is cancelled");
            Bukkit.broadcastMessage("Slot" + event.getSlot());

            if (!(event.getSlot() == 46 || event.getSlot() == 52 || event.getSlot() == 49)) return;
            Bukkit.broadcastMessage("Slots checked.");

            int currentPage = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle())
                    .substring(ChatColor.stripColor(event.getView().getTitle()).length() - 1));
            int maximumPage = Main.getInstance().playersGUI.size();

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
                Bukkit.broadcastMessage("Processing a page refresh for a player.");
                //TODO: Add one second cooldown to refresh
                //TODO: Add smart page opening to refresh
                event.getWhoClicked().openInventory(Main.getInstance().playersGUI.get(0));
            }
        }
    }

}
