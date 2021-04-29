package ir.syrent.wanted.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ir.syrent.wanted.Core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class SkullBuilder {

    public Map<Player, Map<String, Object>> cache = new HashMap<>();

    public void reloadCache() {
        cache.clear();
        new BukkitRunnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    cache.put(player, getHead(player).serialize());
                }
                //Main.getInstance().requestGUI.refresh();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    /*private final String serverVersion;

    public SkullBuilder(String serverVersion) {
        this.serverVersion = serverVersion;
    }*/

    /**
     * Get a head skin of a value.
     * @param value Value of the skin
     * @return The requested head
     */
    public ItemStack getHead(String value) {
        ItemStack skull = getSkull();
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }

    /**
     * Get player's head skin. NOTE: This method should be executed async, otherwise it will cause lag.
     * @param player The player you want to get their head
     * @return The requested Player's head
     */
    public ItemStack getHead(Player player) {
        ItemStack skull = getSkull();
        String value = getHeadValue(player.getName());
        UUID hashAsId;
        try {
            hashAsId = new UUID(value.hashCode(), value.hashCode());
        } catch (NullPointerException e) {
            Main.getInstance().getLogger().info(player.getName() + " is not premium"); //TODO: Debug Message
            return getSkull();
        }
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }

    public ItemStack getHeadFromCache(Player player) {
        return ItemStack.deserialize(cache.get(player));
    }

    public ItemStack getHeadByName(String playerName) {
        ItemStack skull = getSkull();
        String value = getHeadValue(playerName);
        UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );
    }

    public String getHeadValue(String name) {
        try {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            Gson g = new Gson();
            JsonObject obj = g.fromJson(result, JsonObject.class);
            String uid = obj.get("id").toString().replace("\"","");
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
            obj = g.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = g.fromJson(decoded,JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        } catch (Exception ignored){}
        return null;
    }

    private String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String str;
            while((str = in.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception ignored) {}
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch(IOException ignored) {}
        }
        return sb.toString();
    }

    private ItemStack getSkull() {
        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name)
                .collect(Collectors.toList()).contains("PLAYER_HEAD");
        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
        ItemStack item = new ItemStack(type, 1, (short) 3);

        return item;
    }

}
