package com.sorenstudios.wgamemode.commands;

import com.sorenstudios.wgamemode.WGamemode;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class wgremove implements CommandExecutor {

    public WGamemode plugin = WGamemode.instance;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (player.hasPermission("wgamemode.remove")) {
                if (args.length >= 1) {
                    String regionName = args[0];
                    Map<String,Object> regions = this.plugin.getConfig().getConfigurationSection("regions").getValues(false);
                    
                    if (regions.containsKey(regionName)) {
                        regions.remove(regionName);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "This region is not listed!");
                        return true;
                    }
                    
                    this.plugin.getConfig().createSection("regions", regions);
                    this.plugin.saveConfig();
                    
                    player.sendMessage(ChatColor.DARK_GREEN + "Removed region " + regionName);
                    return true;
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "No permissions!");
                return true;
            }

        }
        else if (args.length >= 1) {  
            String regionName = args[0];
            Map<String,Object> regions = this.plugin.getConfig().getConfigurationSection("regions").getValues(false);
            
            if (regions.containsKey(regionName)) {
                regions.remove(regionName);
            }
            else {
                this.plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "This region is not listed!");
                return true;
            }
            
            this.plugin.getConfig().createSection("regions", regions);
            this.plugin.saveConfig();
            
            this.plugin.getServer().getConsoleSender().sendMessage("Removed region " + regionName);
            return true;
        }

        return false;
    }
}