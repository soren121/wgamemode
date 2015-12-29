package com.sorenstudios.wgamemode;

import java.util.HashMap;
import java.util.Map;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

public class WGamemode extends JavaPlugin {

    public Map<Player, GameMode> playersChanged = new HashMap<Player, GameMode>();

    public WorldGuardPlugin getWorldGuard() {
        Plugin worldGuard = getServer().getPluginManager().getPlugin("WorldGuard");

        if (worldGuard == null || !(worldGuard instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin)worldGuard;
    }

    public void onDisable() {
        // Return all players to their original gamemodes to avoid potential issues
        for (Map.Entry<Player, GameMode> entry : this.playersChanged.entrySet()) {
            Player p = entry.getKey();
            if (p.getGameMode() != entry.getValue()) {
                p.setGameMode(entry.getValue());
            }
        }
        
        getLogger().info("Player gamemodes returned to original values");
    }

    public void onEnable() {
        saveDefaultConfig();
        validateGamemodes();
        
        // Set event listeners
        getServer().getPluginManager().registerEvents(new GamemodeListener(this), this);
        getCommand("wgadd").setExecutor(new AddRegion(this));
        getCommand("wgremove").setExecutor(new RemoveRegion(this));
        
        getLogger().info("Loaded successfully!");
    }
    
    private void validateGamemodes() {
        Map<String, Object> regions = getConfig().getConfigurationSection("regions").getValues(false);
        for (Map.Entry<String, Object> entry : regions.entrySet()) {
            GameMode.valueOf(entry.getValue().toString().toUpperCase());
        }
    }

    public String currentRegion(Player player) {
        RegionManager regions = getWorldGuard().getRegionContainer().get(player.getWorld());
        Vector playerLocation = BukkitUtil.toVector(player.getLocation());
        
        for (ProtectedRegion region : regions.getApplicableRegions(playerLocation)) {
            if (getConfig().getConfigurationSection("regions").isSet(region.getId())) {
                // Return the first region ID that matches the player's position
                return region.getId();
            }
        }
        
        // If the player is not in any WGamemode region, return null
        return null;
    }
    
}
