package com.sorenstudios.wgamemode.commands;

import com.sorenstudios.wgamemode.WGamemode;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;

public class wgadd implements CommandExecutor {

    public WGamemode plugin = WGamemode.instance;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (player.hasPermission("wgamemode.add")) {
                if (args.length >= 2) {
                    String regionName = args[0];
                    String regionGamemode = args[1];
                    
                    try {
                        GameMode.valueOf(regionGamemode.toUpperCase());
                    }
                    catch(IllegalArgumentException e) {
                        player.sendMessage(ChatColor.RED + "Invalid gamemode. Try again.");
                        return true;
                    }
                    
                    this.plugin.getConfig().getConfigurationSection("regions").set(regionName, regionGamemode);
                    this.plugin.saveConfig();
                    
                    player.sendMessage(ChatColor.DARK_GREEN + "Added region " + regionName);
                    return true;
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "No permissions!");
                return true;
            }

        }
        else if (args.length >= 2) {
            String regionName = args[0];
            String regionGamemode = args[1];
            
            try {
                GameMode.valueOf(regionGamemode.toUpperCase());
            }
            catch(IllegalArgumentException e) {
                this.plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid gamemode. Try again.");
                return true;
            }
            
            this.plugin.getConfig().getConfigurationSection("regions").set(regionName, regionGamemode);
            this.plugin.saveConfig();
            
            this.plugin.getServer().getConsoleSender().sendMessage("Added region " + regionName);
            return true;
        }

        return false;
    }
}