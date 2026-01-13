package org.bukkit.plugin.java;

import org.bukkit.plugin.Plugin;

public class JavaPlugin implements Plugin {
    public org.bukkit.Server getServer() { return null; }
    public java.util.logging.Logger getLogger() { return java.util.logging.Logger.getGlobal(); }
    public org.bukkit.command.Command getCommand(String name) { return null; }

    // Lifecycle hooks that real JavaPlugin subclasses override
    public void onEnable() {}
    public void onDisable() {}
}