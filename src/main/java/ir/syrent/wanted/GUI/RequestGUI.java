package ir.syrent.wanted.GUI;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.Utils;
import ir.syrent.wanted.WantedManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestGUI {

    public List<Inventory> playersGUI;

    public void refresh() {
        playersGUI = new ArrayList<>();
        List<Player> playerList = new ArrayList<>(SkullBuilder.getInstance().cache.keySet());

        if (playerList.size() == 0) return; //No wanteds found

        for (int i=1 ; i <= Math.ceil((float) playerList.size() / 45) ; i++) {
            playersGUI.add(Bukkit.createInventory(null, 54,
                    ChatColor.translateAlternateColorCodes('&',
                            "&4&lWANTED GUI &8- &5Page &n" + i)));
        }
        //Looping and placing heads for each player
        for (int i=1,slot ; i <= playerList.size() ; i++) {
            int page = (int) Math.ceil((float) i / 45);
            slot = i - ((page - 1) * 45) - 1;

            Player player = playerList.get(i - 1);
            ItemStack playerItem = SkullBuilder.getInstance().getHeadFromCache(player);
            ItemMeta meta = playerItem.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.AQUA + player.getName());

            List<String> lore = new ArrayList<>();
            lore.add(Utils.color("&7Wanteds: &b" + WantedManager.getInstance().getWanted(player)));
            lore.add(Utils.color("&7World: &b" + player.getWorld().getName()));
            lore.add(Utils.color("&7Location: " + String.format(
                    "&eX: &b%.0f&7, &eY: &b%.0f&7, &eZ: &b%.0f",
                    player.getLocation().getX(),
                    player.getLocation().getY(),
                    player.getLocation().getZ())));
            meta.setLore(lore);

            playerItem.setItemMeta(meta);

            playersGUI.get(page - 1).setItem(slot, playerItem);
        }
        //Adding Next and Previous Page Bottuns
        if (playersGUI.size() > 1) {
            ItemStack nextPage = SkullBuilder.getInstance().getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5" +
                    "lY3JhZnQubmV0L3RleHR1cmUvYWFiOTVhODc1MWFlYWEzYzY3MWE4ZTkwYjgzZGU3NmEwMjA0ZjFiZTY1NzUyYWMzMWJlMmY5OGZlYjY0YmY3ZiJ9fX0=");
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            assert nextPageMeta != null;
            nextPageMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e» &aNext Page &e»"));
            nextPage.setItemMeta(nextPageMeta);
            ItemStack prevPage = SkullBuilder.getInstance().getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5" +
                    "lY3JhZnQubmV0L3RleHR1cmUvMzJmZjhhYWE0YjJlYzMwYmM1NTQxZDQxYzg3ODIxOTliYWEyNWFlNmQ4NTRjZGE2NTFmMTU5OWU2NTRjZmM3OSJ9fX0=");
            ItemMeta prevPageMeta = prevPage.getItemMeta();
            assert prevPageMeta != null;
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
        ItemStack refresh = getRefreshButtonItem();
        ItemMeta refreshMeta = refresh.getItemMeta();
        assert refreshMeta != null;
        refreshMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&l• &eRefresh &6&l•"));
        refresh.setItemMeta(refreshMeta);
        for (Inventory inv : playersGUI) {
            inv.setItem(49, refresh);
        }
    }

    public void open(Player player) {
        refresh();
        try {
            player.openInventory(playersGUI.get(0));
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Main.getInstance().messages.getLoadingData());
        }
    }

    public ItemStack getRefreshButtonItem() {
        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name)
                .collect(Collectors.toList()).contains("SUNFLOWER");
        Material type = Material.matchMaterial(isNewVersion ? "SUNFLOWER" : "DOUBLE_PLANT");

        assert type != null;
        return new ItemStack(type);
    }

}
