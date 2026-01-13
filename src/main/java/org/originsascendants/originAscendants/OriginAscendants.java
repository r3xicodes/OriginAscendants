package org.originsascendants.originAscendants;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.originsascendants.originAscendants.player.*;
import org.originsascendants.originAscendants.origins.OriginFactory;
public final class OriginAscendants extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Origins Ascendants plugin loaded.");

        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        getServer().getPluginManager().registerEvents(new AbilityListener(), this);
        getLogger().info("Loaded the ability listener");

        this.getCommand("setorigin").setExecutor(new SetOriginCommand());
        getLogger().info("Loaded setorigin command");

        // Ensure all players currently online have a PlayerState (useful when plugin reloads)
        for (org.bukkit.entity.Player online : Bukkit.getOnlinePlayers()) {
            java.util.UUID uuid = online.getUniqueId();
            if (!PlayerRegistry.exists(uuid)) {
                PlayerState st = new PlayerState(uuid);
                st.setOrigin(OriginFactory.createOrigin("HUMAN", st));
                PlayerRegistry.registerPlayer(st);
                getLogger().info("Registered PlayerState for online player " + online.getName());
            }
        }

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
