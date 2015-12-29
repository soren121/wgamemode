# WGamemode 2

[![Codacy Badge](https://img.shields.io/codacy/0b0f3d64549a41a2a2c2373a9a24eb45.svg?style=flat-square)](https://www.codacy.com/public/soren121/wgamemode)
[![Github Downloads](https://img.shields.io/github/downloads/soren121/wgamemode/total.svg?style=flat-square)](https://github.com/soren121/wgamemode/releases)

This is a plugin for the [Spigot MC server](https://www.spigotmc.org/) (1.8+) 
that allows server admins to set automatic gamemode rules for WorldGuard regions. 

When a player enters a WGamemode-managed region, their gamemode will be automatically 
updated to the region's set gamemode, and when they leave, they will return to their 
original gamemode.

This is a rewritten implementation of the 
[WGamemode plugin](http://dev.bukkit.org/bukkit-plugins/wgamemode/) written by Sinnoh, 
who appears to have left the MC scene in 2012.

Licensed under the LGPLv3 (or any later version.) See LICENSE.txt for more information.

## Usage

This plugin requires that you install WorldEdit 6+ and WorldGuard 6.1+ on your server.  
The only supported MC server is Spigot 1.8+. Tested with 1.8.8 only, though it should 
work with earlier 1.8 versions too.

### config.yml

You don't have to manually configure any settings in the plugin's `config.yml` 
file, but you can if you want.

This plugin has three settings:

 * *regions*: This is an associative array of WorldGuard regions that WGamemode 
   should manage. The key is the region name, and the value is the gamemode that 
   region should have.
   
   For example:
   ```
   regions:
     townsquare: "adventure"
     cathedral: "creative"
   ```
 * *stopItemDrop*: Boolean (true/false). If true, prevents item drops in 
   WGamemode-managed regions. Default is false.
 * *announceGamemodeChange*: Boolean (true/false). The plugin will tell players 
   when they are entering or exiting a WGamemode-managed region. Default is true.
   
An example is provided [here](https://github.com/soren121/wgamemode/blob/master/src/main/resources/config.yml).
That same example is also automatically generated in your server's `plugins` 
directory when the plugin is run for the first time.

### Commands

And it has two in-game commands that do what you expect:

| **Command** | **Permission** | **Description** |
|-----------|------------------|------------------------------------------------|
| /wgadd [region] [gamemode] | wgamemode.add | Adds a region to WGamemode's region list. |
| /wgremove [region] | wgamemode.remove | Removes a region from WGamemode's region list. |
 
## Building from source

To build this plugin from source, you'll need to install JDK 7+ and Maven 3.0+.

WGamemode uses the standard [Maven lifecycle commands](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html).

#### To compile:  
 
    $ mvn compile
    
#### To build a JAR (for use with Spigot):  
 
    $ mvn package

Your JAR file will be in the `target` directory.