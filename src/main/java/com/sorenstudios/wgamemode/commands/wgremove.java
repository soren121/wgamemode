package com.sorenstudios.wgamemode.commands;

import com.sorenstudios.wgamemode.WGamemode;
import java.util.List;
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
                    String rs = args[0];
                    List<String> list = this.plugin.getConfig().getStringList("regions");
                    
                    if (list.contains(rs)) {
                        list.remove(rs);
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "This region is not listed!");
                        return true;
                    }
                    
                    this.plugin.getConfig().set("regions", list);
                    this.plugin.saveConfig();
                    
                    player.sendMessage(ChatColor.DARK_GREEN + "Removed region " + rs);
                    return true;
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "No permissions!");
                return true;
            }

        }
        else if (args.length >= 1) {
            String rs = args[0];
            List<String> list = this.plugin.getConfig().getStringList("regions");
            
            if (list.contains(rs)) {
                list.remove(rs);
            }
            else {
                this.plugin.getServer().getConsoleSender().sendMessage("This region is not listed!");
                return true;
            }
            
            this.plugin.getConfig().set("regions", list);
            this.plugin.saveConfig();
            
            this.plugin.getServer().getConsoleSender().sendMessage("Removed region " + rs);
            return true;
        }

        return false;
    }
}