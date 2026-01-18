package org.originsascendants.originAscendants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.originsascendants.originAscendants.origins.base.OriginFactory;
import org.originsascendants.originAscendants.player.PlayerRegistry;
import org.originsascendants.originAscendants.player.PlayerState;

import java.util.UUID;


public class JoinListener implements Listener {
    // Handles join logic. The below function executes once.
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // set up an origins and playerstate
        UUID uuid=e.getPlayer().getUniqueId();
        if (PlayerRegistry.exists(uuid)) {
            org.bukkit.Bukkit.getLogger().info("Player "+e.getPlayer().getName()+" exists");
            return;
        } else {
            org.bukkit.Bukkit.getLogger().info("Player "+e.getPlayer().getName()+" doesn't have a PlayerState, creating one...");
        }
        PlayerState p = new PlayerState(uuid);
        p.setOrigin(OriginFactory.createOrigin("HUMAN", p));
        PlayerRegistry.registerPlayer(p);
        org.bukkit.Bukkit.getLogger().info("Creating playerstate for "+e.getPlayer().getName());
    }
}
