/*
    WGamemode 2, an automatic gamemode switching plugin for Spigot 1.8+
    Copyright (C) 2015 Nicholas Narsing <soren121@sorenstudios.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published 
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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

        if (worldGuard instanceof WorldGuardPlugin) {
            return (WorldGuardPlugin)worldGuard;
        }
        else {
            return null;
        }
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
