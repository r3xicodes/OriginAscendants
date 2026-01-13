package org.bukkit;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class Bukkit {
    public static Player getPlayerExact(String name) { return null; }
    public static Iterable<Player> getOnlinePlayers() { return java.util.Collections.emptyList(); }
    public static PluginManager getPluginManager() { return null; }
    public static BukkitScheduler getScheduler() { return null; }
}