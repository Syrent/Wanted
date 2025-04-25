package org.sayandev.wanted.Dependencies;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.sayandev.wanted.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class WorldGuard {

    public String getRegionName(Location location) {
        String id = null;
        int priority = 0;
        RegionManager regionManager = getRegionManager(location.getWorld());
        Iterable<ProtectedRegion> applicableRegions = regionManager.getApplicableRegions(toVector(location));
        for (ProtectedRegion region : applicableRegions) {
            if (id != null) {
                if (region.getPriority() > priority) {
                    id = region.getId().toLowerCase();
                    priority = region.getPriority();
                }
            } else {
                id = region.getId().toLowerCase();
                priority = region.getPriority();
            }
        }
        return id;
    }

    public boolean isRegionWhitelisted(List<String> enabledRegions, Location location) {
        RegionManager regionManager = getRegionManager(location.getWorld());
        Iterable<ProtectedRegion> applicableRegions = regionManager.getApplicableRegions(toVector(location));
        for (ProtectedRegion region : applicableRegions) {
            String id = region.getId().toLowerCase();
            if (enabledRegions.contains(id))
                return true;
        }
        return false;
    }

    public boolean isRegionBlacklisted(List<String> disabledRegions, Location location) {
        RegionManager regionManager = getRegionManager(location.getWorld());
        Iterable<ProtectedRegion> applicableRegions = regionManager.getApplicableRegions(toVector(location));
        for (ProtectedRegion region : applicableRegions) {
            String id = region.getId().toLowerCase();
            if (disabledRegions.contains(id)) {
                return true;
            }
        }
        return false;
    }

    private static BlockVector3 toVector(Location location) {
        return BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public WorldGuardPlatform getWorldGuard() {
        final Plugin worldGuard = Main.getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
        if (!(worldGuard instanceof WorldGuardPlugin)) {
            return null;
        }
        return com.sk89q.worldguard.WorldGuard.getInstance().getPlatform();
    }

    public RegionManager getRegionManager(World world) {
        return getWorldGuard().getRegionContainer().get(new BukkitWorld(world));
    }
}
