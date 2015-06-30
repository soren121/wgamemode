package com.sorenstudios.WGamemode;

import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class WGListener implements Listener {

    private WGamemode plugin;

    public WGListener(WGamemode instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.isinregion(player).booleanValue()) {
            if ((player.getGameMode().equals(GameMode.SURVIVAL))
                    || (!this.plugin.waschanged.contains(player))) {
                this.plugin.waschanged.add(player);
                player.setGameMode(GameMode.CREATIVE);
            }

        }
        else if (this.plugin.waschanged.contains(player)) {
            this.plugin.waschanged.remove(player);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.waschanged.contains(player)) {
            this.plugin.waschanged.remove(player);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.waschanged.contains(player)) {
            this.plugin.waschanged.remove(player);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onPlayerOpenInventory(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();

        if ((event.getInventory().getType().equals(InventoryType.BREWING))
                || (event.getInventory().getType().equals(InventoryType.CHEST))
                || (event.getInventory().getType()
                        .equals(InventoryType.CRAFTING))
                || (event.getInventory().getType()
                        .equals(InventoryType.DISPENSER))
                || (event.getInventory().getType()
                        .equals(InventoryType.ENCHANTING))
                || (event.getInventory().getType()
                        .equals(InventoryType.FURNACE))
                || (event.getInventory().getType().equals(InventoryType.PLAYER))
                || (event.getInventory().getType()
                        .equals(InventoryType.WORKBENCH))) {
            if (this.plugin.getConfig().getBoolean("StopInteract")) {
                if (this.plugin.waschanged.contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getConfig().getBoolean("StopItemDrop")) {
            if (this.plugin.waschanged.contains(player)) {
                event.setCancelled(true);
            }
        }
    }
}