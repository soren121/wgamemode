package com.sorenstudios.wgamemode;

import java.util.ArrayList;
import java.util.List;
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

public class GamemodeListener implements Listener {

    private WGamemode plugin;
    private List<Player> enteredRegion = new ArrayList<Player>();

    public GamemodeListener(WGamemode instance) {
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
                if(!this.enteredRegion.contains(player)) {
                    this.plugin.playersChanged.put(player, player.getGameMode());
                }
                
                player.setGameMode(regionGamemode);
                
                if(this.plugin.getConfig().getBoolean("announceGamemodeChange")) {
                    player.sendMessage(ChatColor.YELLOW + "Entering " + 
                        regionGamemode.name().toLowerCase() + " area");
                }
            }
            
            this.enteredRegion.add(player);
        }
        else if (this.plugin.playersChanged.containsKey(player)) {
            if (this.plugin.getConfig().getBoolean("announceGamemodeChange")) {
                player.sendMessage(ChatColor.YELLOW + "Leaving " + 
                    player.getGameMode().name().toLowerCase() + " area");
            }
            
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
    public void onPlayerOpenInventory(InventoryOpenEvent event) {
        Player player = (Player)event.getPlayer();
        if(this.plugin.getConfig().getBoolean("stopInteract") && 
            this.plugin.playersChanged.containsKey(player)) {

            InventoryType objType = event.getInventory().getType();
            if (objType.equals(InventoryType.BREWING) ||
                objType.equals(InventoryType.CHEST) ||
                objType.equals(InventoryType.CRAFTING) ||
                objType.equals(InventoryType.DISPENSER) ||
                objType.equals(InventoryType.ENCHANTING) ||
                objType.equals(InventoryType.FURNACE) ||
                objType.equals(InventoryType.PLAYER) ||
                objType.equals(InventoryType.WORKBENCH)) {
                        
                event.setCancelled(true);
            }
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
