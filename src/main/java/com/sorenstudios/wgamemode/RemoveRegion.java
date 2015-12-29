package com.sorenstudios.wgamemode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class RemoveRegion implements CommandExecutor {

    private WGamemode plugin;
    
    public RemoveRegion(WGamemode instance) {
        this.plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If a player is calling this command, check their permissions
        // No need to check for console-issued commands
        if (sender instanceof Player && !((Player)sender).hasPermission("wgamemode.remove")) {
            sender.sendMessage(ChatColor.RED + "No permissions!");
            return true;
        }
        
        // Verify that argument length is correct
        if (args.length >= 1) {
            String regionName = args[0];
            ConfigurationSection regions = this.plugin.getConfig().getConfigurationSection("regions");
            
            // Verify that region is in the config file
            if (regions != null && regions.isSet(regionName)) {
                regions.set(regionName, null);
                this.plugin.saveConfig();
            }
            else if(!regions.isSet(regionName)) {
                sender.sendMessage(ChatColor.RED + "This region is not listed!");
                return true;
            }
            else {
                return false;
            }
            
            // Notify user of success
            sender.sendMessage(ChatColor.DARK_GREEN + "Removed region " + regionName);
            return true;
        }
        else {
            return false;
        }
    }
    
}
