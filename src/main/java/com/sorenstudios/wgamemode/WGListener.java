package com.sorenstudios.wgamemode;

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

    public WGListener(WGamemode instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.isInRegion(player)) {
            if (!(player.getGameMode() == this.plugin.regionGamemode) || 
                !this.plugin.playersChanged.containsKey(player)) {
                    
                this.plugin.playersChanged.put(player, player.getGameMode());
                player.setGameMode(this.plugin.regionGamemode);
            }
        }
        else if (this.plugin.playersChanged.containsKey(player)) {
            player.setGameMode(this.plugin.playersChanged.get(player));
            this.plugin.playersChanged.remove(player);
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