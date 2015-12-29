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

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.GameMode;

public class GamemodeListener implements Listener {

    private WGamemode plugin;
    private ArrayList<Player> enteredRegion = new ArrayList<Player>();

    public GamemodeListener(WGamemode instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // Check if the player is currently in a region we're managing
        String currentRegion = this.plugin.currentRegion(player);
        if (currentRegion != null) {
            GameMode regionGamemode = GameMode.valueOf(this.plugin.getConfig().
                getConfigurationSection("regions").getString(currentRegion).toUpperCase());
            
            // If their gamemode doesn't match the region's gamemode, change it!
            if (player.getGameMode() != regionGamemode) {
                // Add player to the list of mutated players
                if(!this.enteredRegion.contains(player)) {
                    this.plugin.playersChanged.put(player, player.getGameMode());
                }
                
                player.setGameMode(regionGamemode);
                
                if(this.plugin.getConfig().getBoolean("announceGamemodeChange")) {
                    player.sendMessage(ChatColor.YELLOW + "Entering " + 
                        regionGamemode.name().toLowerCase() + " area");
                }
            }
            
            // Mark this player as having entered a managed region
            this.enteredRegion.add(player);
        }
        // If the user isn't in a region we manage, see if we've updated their status yet
        else if (this.plugin.playersChanged.containsKey(player)) {
            if (this.plugin.getConfig().getBoolean("announceGamemodeChange")) {
                player.sendMessage(ChatColor.YELLOW + "Leaving " + 
                    player.getGameMode().name().toLowerCase() + " area");
            }
            
            // We haven't, so do that now
            player.setGameMode(this.plugin.playersChanged.get(player));
            this.plugin.playersChanged.remove(player);
            
            this.enteredRegion.remove(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.playersChanged.containsKey(player)) {
            player.setGameMode(this.plugin.playersChanged.get(player));
            this.plugin.playersChanged.remove(player);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.playersChanged.containsKey(player)) {
            player.setGameMode(this.plugin.playersChanged.get(player));
            this.plugin.playersChanged.remove(player);
        }
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getConfig().getBoolean("stopItemDrop") && 
            this.plugin.playersChanged.containsKey(player)) {
                
            event.setCancelled(true);
        }
    }
    
}
