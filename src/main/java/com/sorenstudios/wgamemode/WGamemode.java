package com.sorenstudios.wgamemode;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.plugin.Plugin;

public class WGamemode extends org.bukkit.plugin.java.JavaPlugin {

    public Map<Player, GameMode> playersChanged = new HashMap<Player, GameMode>();
    public static WGamemode instance;

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public void onDisable() {
        for (Map.Entry<Player, GameMode> entry : this.playersChanged.entrySet()) {
            Player p = entry.getKey();
            if (p.getGameMode() != entry.getValue()) {
                p.setGameMode(entry.getValue());
            }
        }
        
        getLogger().info("Player gamemodes returned to original values");
    }

    public void onEnable() {
        this.instance = this;
        getServer().getPluginManager().registerEvents(new WGListener(this), this);
        
        saveDefaultConfig();
        validateGamemodes();
        
        getCommand("wgadd").setExecutor(new com.sorenstudios.wgamemode.commands.wgadd());
        getCommand("wgremove").setExecutor(new com.sorenstudios.wgamemode.commands.wgremove());
        
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

        ApplicableRegionSet set = regions.getApplicableRegions(BukkitUtil.toVector(player.getLocation()));
        for (ProtectedRegion r : set) {
            if (getConfig().getConfigurationSection("regions").isSet(r.getId())) {
                return r.getId();
            }
        }
        
        return null;
    }
}