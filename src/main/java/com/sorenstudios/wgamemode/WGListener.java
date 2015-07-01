package com.sorenstudios.wgamemode;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.GameMode;
import org.bukkit.inventory.Inventory;

public class WGListener implements Listener {

    private WGamemode plugin;
    private Map<Player,String> lastRegion = new HashMap<Player,String>();

    public WGListener(WGamemode instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String currentRegion = this.plugin.currentRegion(player);
        if (currentRegion != null) {
            GameMode regionGamemode = GameMode.valueOf(this.plugin.getConfig().
                getConfigurationSection("regions").getString(currentRegion).toUpperCase());
            
            if (player.getGameMode() != regionGamemode) {
                if(this.lastRegion.get(player) == null) {
                    this.plugin.playersChanged.put(player, player.getGameMode());
                }
                
                player.setGameMode(regionGamemode);
                
                if(this.plugin.getConfig().getBoolean("announceGamemodeChange")) {
                    player.sendMessage(ChatColor.YELLOW + "Entering " + 
                        regionGamemode.name().toLowerCase() + " area");
                }
            }
            
            lastRegion.put(player, currentRegion);
        }
        else if (this.plugin.playersChanged.containsKey(player)) {
            if (this.plugin.getConfig().getBoolean("announceGamemodeChange")) {
                player.sendMessage(ChatColor.YELLOW + "Leaving " + 
                    player.getGameMode().name().toLowerCase() + " area");
            }
            
            player.setGameMode(this.plugin.playersChanged.get(player));
            this.plugin.playersChanged.remove(player);
            
            this.lastRegion.remove(player);
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
    public void onPlayerOpenInventory(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if ((event.getInventory().getType().equals(InventoryType.BREWING))
                || (event.getInventory().getType().equals(InventoryType.CHEST))
                || (event.getInventory().getType().equals(InventoryType.CRAFTING))
                || (event.getInventory().getType().equals(InventoryType.DISPENSER))
                || (event.getInventory().getType().equals(InventoryType.ENCHANTING))
                || (event.getInventory().getType().equals(InventoryType.FURNACE))
                || (event.getInventory().getType().equals(InventoryType.PLAYER))
                || (event.getInventory().getType().equals(InventoryType.WORKBENCH))) {
            if (this.plugin.getConfig().getBoolean("stopInteract")) {
                if (this.plugin.playersChanged.containsKey(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getConfig().getBoolean("stopItemDrop")) {
            if (this.plugin.playersChanged.containsKey(player)) {
                event.setCancelled(true);
            }
        }
    }
}