package org.originsascendants.originAscendants;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.originsascendants.originAscendants.player.*;
import org.originsascendants.originAscendants.origins.OriginFactory;

public final class OriginAscendants extends JavaPlugin {

    private BukkitTask tickTask;

    @Override
    public void onLoad() {
        getLogger().info("Loading OriginAscendants v" + getDescription().getVersion());
    }

    @Override
    public void onEnable() {
        getLogger().info("OriginAscendants v" + getDescription().getVersion() + " is enabling...");

        try {
            // Register event listeners
            getServer().getPluginManager().registerEvents(new JoinListener(), this);
            getLogger().info("✓ Registered JoinListener");

            getServer().getPluginManager().registerEvents(new AbilityListener(), this);
            getLogger().info("✓ Registered AbilityListener");

            // Register commands
            if (getCommand("setorigin") != null) {
                getCommand("setorigin").setExecutor(new SetOriginCommand());
                getLogger().info("✓ Registered /setorigin command");
            } else {
                getLogger().warning("⚠ /setorigin command not found in plugin.yml!");
            }

            // Initialize player states for online players
            for (org.bukkit.entity.Player online : Bukkit.getOnlinePlayers()) {
                java.util.UUID uuid = online.getUniqueId();
                if (!PlayerRegistry.exists(uuid)) {
                    PlayerState st = new PlayerState(uuid);
                    st.setOrigin(OriginFactory.createOrigin("HUMAN", st));
                    PlayerRegistry.registerPlayer(st);
                    getLogger().info("Initialized PlayerState for " + online.getName());
                }
            }

            // Start ability tick task (runs every tick = 1 game tick)
            tickTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
                for (PlayerState state : PlayerRegistry.getAllPlayerStates()) {
                    if (state != null && state.getOrigin() != null) {
                        state.getOrigin().tick();
                        state.getOrigin().updateCooldowns();
                    }
                }
            }, 1L, 1L);
            getLogger().info("✓ Started ability tick scheduler");

            getLogger().info("✓ OriginAscendants enabled successfully!");

        } catch (Exception e) {
            getLogger().severe("Failed to enable OriginAscendants!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("OriginAscendants is disabling...");

        // Cancel tick task
        if (tickTask != null) {
            tickTask.cancel();
        }

        // Clean up player registry
        PlayerRegistry.clearAll();

        getLogger().info("✓ OriginAscendants disabled successfully!");
    }

}
