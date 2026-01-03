package org.originsascendants.originAscendants;

import org.bukkit.plugin.java.JavaPlugin;

public final class OriginAscendants extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Origins Ascendants plugin loaded.");

    }

    @Override
    public void onDisable() {
        getLogger().info("Origins Ascendants plugin unloaded.");
    }
}
