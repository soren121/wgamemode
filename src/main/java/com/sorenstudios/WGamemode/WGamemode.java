package com.sorenstudios.WGamemode;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class WGamemode extends org.bukkit.plugin.java.JavaPlugin {

    public List<Player> waschanged = new ArrayList();

    public static WGamemode instance;

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public void onDisable() {
        for (Player p : this.waschanged) {
            if (p.getGameMode().equals(GameMode.CREATIVE)) {
                p.setGameMode(GameMode.SURVIVAL);
            }
        }
        getLogger().info("Disabled successfully.");
    }

    public void onEnable() {
        this.instance = this;
        
        getServer().getPluginManager().registerEvents(new WGListener(this), this);
        loadConfig();
        
        getCommand("wgadd").setExecutor(new com.sorenstudios.WGamemode.commands.wgadd());
        getCommand("wgremove").setExecutor(new com.sorenstudios.WGamemode.commands.wgremove());
        
        getLogger().info("Loaded successfully!");
    }

    public void loadConfig() {
        List<String> list = new ArrayList();
        list.add("Gamemoderegion");
        
        FileConfiguration config = getConfig();
        config.addDefault("Regions", list);
        config.addDefault("StopItemDrop", true);
        config.addDefault("StopInteract", true);
        config.options().copyDefaults(true);
        saveConfig();
    }

    public boolean isInRegion(Player player) {
        RegionManager regions = getWorldGuard().getRegionContainer().get(player.getWorld());

        ApplicableRegionSet set = regions.getApplicableRegions(BukkitUtil.toVector(player.getLocation()));
        for (ProtectedRegion r : set) {
            if (getConfig().getList("Regions").contains(r.getId())) {
                return true;
            }
        }
        return false;
    }
}