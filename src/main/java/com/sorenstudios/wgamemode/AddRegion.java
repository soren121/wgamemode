package com.sorenstudios.wgamemode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;

public class AddRegion implements CommandExecutor {

    private WGamemode plugin;
    
    public AddRegion(WGamemode instance) {
        this.plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If a player is calling this command, check their permissions
        // No need to check for console-issued commands
        if (sender instanceof Player && !((Player)sender).hasPermission("wgamemode.add")) {
            sender.sendMessage(ChatColor.RED + "No permissions!");
            return true;
        }
        
        // Verify that argument length is correct
        if (args.length >= 2) {
            String regionName = args[0];
            String regionGamemode = args[1];
            
            // Verify that the gamemode given is valid
            try {
                GameMode.valueOf(regionGamemode.toUpperCase());
            }
            catch(IllegalArgumentException | NullPointerException e) {
                sender.sendMessage(ChatColor.RED + "Invalid gamemode. Try again.");
                return true;
            }
            
            // Add region to config file & save
            ConfigurationSection regions = this.plugin.getConfig().getConfigurationSection("regions");
            if(regions != null) {
                regions.set(regionName, regionGamemode);
                this.plugin.saveConfig();
            }
            else {
                return false;
            }
            
            // Notify player of success
            sender.sendMessage(ChatColor.DARK_GREEN + "Added region " + regionName);
            return true;
        }
        else {
            return false;
        }
    }
    
}
