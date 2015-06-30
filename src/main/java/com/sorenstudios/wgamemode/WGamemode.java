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
    public static GameMode regionGamemode;
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
            if (p.getGameMode() == this.regionGamemode) {
                p.setGameMode(entry.getValue());
            }
        }
        
        getLogger().info("Player gamemodes returned to original values");
    }

    public void onEnable() {
        this.instance = this;
        
        getServer().getPluginManager().registerEvents(new WGListener(this), this);
        
        saveDefaultConfig();
        this.regionGamemode = GameMode.valueOf(getConfig().getString("gamemode").toUpperCase());
        
        getCommand("wgadd").setExecutor(new com.sorenstudios.wgamemode.commands.wgadd());
        getCommand("wgremove").setExecutor(new com.sorenstudios.wgamemode.commands.wgremove());
        
        getLogger().info("Loaded successfully!");
    }

    public boolean isInRegion(Player player) {
        RegionManager regions = getWorldGuard().getRegionContainer().get(player.getWorld());

        ApplicableRegionSet set = regions.getApplicableRegions(BukkitUtil.toVector(player.getLocation()));
        for (ProtectedRegion r : set) {
            if (getConfig().getList("regions").contains(r.getId())) {
                return true;
            }
        }
        
        return false;
    }
}