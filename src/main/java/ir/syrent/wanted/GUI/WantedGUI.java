package ir.syrent.wanted.GUI;

import ir.syrent.wanted.Core.Main;
import ir.syrent.wanted.GUI.buttons.SGButton;
import ir.syrent.wanted.GUI.item.ItemBuilder;
import ir.syrent.wanted.Messages.Messages;
import ir.syrent.wanted.Utils.SkullBuilder;
import ir.syrent.wanted.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class WantedGUI {

    private static final Main plugin = Main.getPlugin(Main.class);
    Messages messages = new Messages();

    @SuppressWarnings("deprecation")
    public WantedGUI(Player player) {

        if (plugin.getSetWanted().isEmpty()) {
            player.sendMessage(Utils.color(messages.getNoWanteds()));
            return;
        }

        int number = 0;

        for (Map.Entry<String, Integer> wantedPlayer : plugin.getSetWanted().entrySet()) {
            if (Bukkit.getPlayerExact(wantedPlayer.getKey()) != null) number++;
        }
        if (number == 0) {
            player.sendMessage(Utils.color(messages.getNoWanteds()));
            return;
        }
        number = 0;

        int[] BORDER = {
                1, 2, 3, 4, 5, 6, 7,
                9, 17,
                19, 20, 21, 22, 23, 24, 25,
        };

        int slot = 10;
        int page = 0;


        SGMenu wantedGU = Main.getSpiGUI().create("&6&lWANTED LIST &7(&9&lPAGE &9{currentPage}&7/&9{maxPage}&7)", 3);

        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name)
                .collect(Collectors.toList()).contains("PLAYER_HEAD");

        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");

        boolean glassIsNewVersion = Arrays.stream(Material.values()).map(Material::name)
                .collect(Collectors.toList()).contains("BLUE_STAINED_GLASS_PANE");

        Material glassType = Material.matchMaterial(glassIsNewVersion ? "BLUE_STAINED_GLASS_PANE" : "STAINED_GLASS_PANE");
        ItemStack glassItem = new ItemStack(glassType, 1);
        glassItem.setDurability((short) 11);
        ItemMeta glassMeta = glassItem.getItemMeta();
        glassMeta.setDisplayName("§r");

        for (Map.Entry<String, Integer> wantedPlayer : plugin.getSetWanted().entrySet()) {

            ItemStack item;

            Player wanted = Bukkit.getPlayerExact(wantedPlayer.getKey());
            if (wanted == null) continue;

            item = ItemStack.deserialize(Main.getInstance().skullBuilder.cache.get(Bukkit.getPlayerExact(wantedPlayer.getKey())));

            //if (!isNewVersion) item.setDurability((short) 3);
            wantedGU.setButton(page, slot, new SGButton(
                    new ItemBuilder(item)
                            .skullOwner(wantedPlayer.getKey())
                            .name("&b" + wantedPlayer.getKey())
                            .lore(
                                    "&7Wanted: &b" + wantedPlayer.getValue(),
                                    "&7World: &b" + wanted.getWorld().getName(),
                                    "&7Location: " + String.format(
                                            "&eX: &b%.0f&7, &eY: &b%.0f&7, &eZ: &b%.0f",
                                            wanted.getLocation().getX(),
                                            wanted.getLocation().getY(),
                                            wanted.getLocation().getZ()
                                    )
                            )
                            .build()
            ).withListener((InventoryClickEvent event) -> Bukkit.dispatchCommand(player, "wanted find " + wantedPlayer.getKey())));
            for (int i : BORDER) {
                wantedGU.setButton(page, i, new SGButton(new ItemBuilder(glassItem).build()));
            }
            slot++;
            if (slot == 16) {
                slot = 10;
                page++;
            }
        }

        AtomicReference<BukkitTask> borderRunnable = new AtomicReference<>();

        wantedGU.setOnPageChange(inventory -> borderRunnable.set(
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> wantedGU.refreshInventory(player));
                    }
                }.runTaskTimer(plugin, 0, 20)
        ));

        wantedGU.setOnClose(inventory -> {
            if (borderRunnable.get() != null) borderRunnable.get().cancel();
        });

        wantedGU.getOnPageChange().accept(wantedGU);
        player.openInventory(wantedGU.getInventory());
    }
}
