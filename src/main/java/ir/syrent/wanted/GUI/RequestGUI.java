package ir.syrent.wanted.GUI;

import ir.syrent.wanted.Main;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.Utils;
import ir.syrent.wanted.WantedManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class RequestGUI implements Listener {

    public List<Inventory> playersGUI;

    @Deprecated
    public void refresh() {
        playersGUI = new ArrayList<>();
        List<Player> playerList = new ArrayList<>(SkullBuilder.getInstance().cache.keySet());

        if (playerList.size() == 0) return; //No wanteds found

        for (int i=1 ; i <= Math.ceil((float) playerList.size() / 45) ; i++) {
            playersGUI.add(Bukkit.createInventory(null, 54, Messages.GUI.TITLE.replace("%page%", String.valueOf(i))));
        }

        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name)
                .collect(Collectors.toList()).contains("PLAYER_HEAD");
        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

        //Looping and placing heads for each player
        for (int i=1,slot ; i <= playerList.size() ; i++) {
            int page = (int) Math.ceil((float) i / 45);
            slot = i - ((page - 1) * 45) - 1;

            Player player = playerList.get(i - 1);
            ItemStack playerItem;
            try {
                playerItem = SkullBuilder.getInstance().getHeadFromCache(player);
            } catch (NullPointerException | IllegalArgumentException e) {
                playerItem = new ItemStack(type, 1, (short) 3);
            }
            ItemMeta meta = playerItem.getItemMeta();
            assert meta != null;
            meta.setDisplayName(Messages.GUI.Player.TITLE.replace("%player_name%", player.getName()));

            List<String> lore = new ArrayList<>();
            String victim = Main.getInstance().playerVictimMap.get(player.getName());

            for (String line : Messages.GUI.Player.LORE) {
                lore.add(line.replace("%wanted%", String.valueOf(WantedManager.getInstance().getWanted(player.getName())))
                .replace("%world%", player.getWorld().getName())
                .replace("%location%",
                        Utils.colorize(String.format(
                                "&eX: &b%.0f&7, &eY: &b%.0f&7, &eZ: &b%.0f",
                                player.getLocation().getX(),
                                player.getLocation().getY(),
                                player.getLocation().getZ())))
                .replace("%last_victim%", victim == null ? "Unknown" : victim));
            }
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
            nextPageMeta.setDisplayName(Messages.GUI.Button.NEXT_PAGE);
            nextPage.setItemMeta(nextPageMeta);
            ItemStack prevPage = SkullBuilder.getInstance().getHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5" +
                    "lY3JhZnQubmV0L3RleHR1cmUvMzJmZjhhYWE0YjJlYzMwYmM1NTQxZDQxYzg3ODIxOTliYWEyNWFlNmQ4NTRjZGE2NTFmMTU5OWU2NTRjZmM3OSJ9fX0=");
            ItemMeta prevPageMeta = prevPage.getItemMeta();
            assert prevPageMeta != null;
            prevPageMeta.setDisplayName(Messages.GUI.Button.PREV_PAGE);
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
        refreshMeta.setDisplayName(Messages.GUI.Button.REFRESH);
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
            player.sendMessage(Messages.LOADING_DATA);
        }
    }

    public ItemStack getRefreshButtonItem() {
        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name)
                .collect(Collectors.toList()).contains("SUNFLOWER");
        Material type = Material.matchMaterial(isNewVersion ? "SUNFLOWER" : "DOUBLE_PLANT");

        assert type != null;
        return new ItemStack(type);
    }

    HashSet<Player> playerCooldown = new HashSet<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (isWantedGUI(event.getPlayer().getOpenInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerDropItemEvent event) {
        if (isWantedGUI(event.getPlayer().getOpenInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();

        if (isWantedGUI(event.getView())) {
            event.setCancelled(true);

            //if (!(event.getSlot() == 46 || event.getSlot() == 52 || event.getSlot() == 49)) return;

            int currentPage = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle())
                    .substring(ChatColor.stripColor(event.getView().getTitle()).length() - 1));
            int maximumPage = Main.getInstance().requestGUI.playersGUI.size();

            if (playerCooldown.contains(player)) {
                player.sendMessage(Messages.GUI.Button.REFRESH);
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
                    event.getWhoClicked().openInventory(Main.getInstance().requestGUI.playersGUI.get(currentPage - 2));
                }
            } else if (event.getSlot() == 52 && event.getClickedInventory().getItem(52) != null) {
                //Next Page
                if (currentPage != maximumPage) {
                    event.getWhoClicked().openInventory(Main.getInstance().requestGUI.playersGUI.get(currentPage));
                }
            } else if (event.getSlot() == 49) {
                //Refresh
                event.getWhoClicked().openInventory(Main.getInstance().requestGUI.playersGUI.get(0));
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

    private boolean isWantedGUI(InventoryView inventory) {
        return inventory.getTitle().contains(Messages.GUI.TITLE.replace("%page%", ""));
    }

}
