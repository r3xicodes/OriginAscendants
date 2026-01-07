package org.originsascendants.originAscendants;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;
import org.originsascendants.originAscendants.origins.*;
import org.originsascendants.originAscendants.player.*;
public final class OriginAscendants extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Origins Ascendants plugin loaded.");

        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        getServer().getPluginManager().registerEvents(new AbilityListener(), this);
        getLogger().info("Loaded the ability listener");

        this.getCommand("setorigin").setExecutor(new SetOriginCommand());
        getLogger().info("Loaded setorigin command");

        Bukkit.getScheduler().runTaskTimer(this, ()->{
            for(PlayerState state : PlayerRegistry.getAllPlayerStates()) {
                if (state != null) {
                    state.getOrigin().tick();
                }
            }
        },1L,1L);
    }

    @Override
    public void onDisable() {
        getLogger().info("Origins Ascendants plugin unloaded.");
    }

}
