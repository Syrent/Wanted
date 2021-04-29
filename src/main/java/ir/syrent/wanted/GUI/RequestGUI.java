package ir.syrent.wanted.GUI;

import ir.syrent.wanted.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class RequestGUI {

    public RequestGUI() {
        start();
    }

    public boolean needRefresh = false;

    public void refresh() {
        List<Inventory> playersGUI = new ArrayList<>();
        List<Player> playerList = new ArrayList<>(Main.getInstance().skullBuilder.cache.keySet());

        for (int i=1 ; i <= Math.ceil((float) playerList.size() / 45) ; i++) {
            playersGUI.add(Bukkit.createInventory(null, 54,
                    ChatColor.translateAlternateColorCodes('&',
                            "&4Battle Requester &8- &5Page &n" + i)));
        }
        //Looping and placing heads for each player
        for (int i=1,slot ; i <= playerList.size() ; i++) {
            int page = (int) Math.ceil((float) i / 45);
            slot = i - ((page - 1) * 45) - 1;

            Player player = playerList.get(i - 1);
            ItemStack playerItem = Main.getInstance().skullBuilder.getHeadFromCache(player);
            ItemMeta meta = playerItem.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + player.getName());
            ArrayList<String> lore = new ArrayList<>();

            if (Main.getInstance().wantedMap.get(player.getName()) == null) continue;

            lore.add("&7Wanted: &b" + Main.getInstance().wantedMap.get(player.getName()));
            lore.add("&7World: &b" + player.getWorld().getName());
            lore.add("&7Location: " + String.format(
                    "&eX: &b%.0f&7, &eY: &b%.0f&7, &eZ: &b%.0f",
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ()));
            playerItem.setItemMeta(meta);

            playersGUI.get(page - 1).setItem(slot, playerItem);
        }
        //Adding Next and Previous Page Bottuns
        if (playersGUI.size() > 1) {
            ItemStack nextPage = new ItemStack(Material.PAPER);
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e» &aNext Page &e»"));
            nextPage.setItemMeta(nextPageMeta);
            ItemStack prevPage = new ItemStack(Material.PAPER);
            ItemMeta prevPageMeta = prevPage.getItemMeta();
            prevPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e« &aPrevious Page &e«"));
            prevPage.setItemMeta(prevPageMeta);

            playersGUI.get(0).setItem(52, nextPage);
            playersGUI.get(playersGUI.size() - 1).setItem(46, prevPage);
            if (playersGUI.size() != 2) {
                for (int i = 0; i <= playersGUI.size(); i++) {
                    if (i == 0 || i == playersGUI.size() - 1) continue;
                    playersGUI.get(i).setItem(46, prevPage);
                    playersGUI.get(i).setItem(52, nextPage);
                }
            }
        }
        //Adding Refresh button
        ItemStack refresh = new ItemStack(Material.STONE);
        ItemMeta refreshMeta = refresh.getItemMeta();
        refreshMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5• &6Refresh &5•"));
        refresh.setItemMeta(refreshMeta);
        for (Inventory inv : playersGUI) {
            inv.setItem(49, refresh);
        }

        Main.getInstance().playersGUI.clear();
        Main.getInstance().playersGUI = playersGUI;
    }

    public void start() {
        new BukkitRunnable() {
            public void run() {
                refresh();
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 100);
    }

    public void open(Player player) {
        try {
            player.openInventory(Main.getInstance().playersGUI.get(0));
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage("Exception");
        }
    }
}
