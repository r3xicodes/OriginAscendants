package org.originsascendants.originAscendants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.originsascendants.originAscendants.player.PlayerRegistry;
import org.originsascendants.originAscendants.player.PlayerState;

/**
 * Listens for player events and delegates to origin-specific handlers
 * Handles: attacks, consumption, damage, projectiles
 */
public class OriginEventListener implements Listener {

    /**
     * Called when a player deals damage to an entity
     */
    @EventHandler
    public void onAttackEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            PlayerState pstate = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
            if (pstate != null && pstate.getOrigin() != null) {
                pstate.getOrigin().onAttackEntity(event);
            }
        }
    }

    /**
     * Called when a player consumes food or drink
     */
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        PlayerState pstate = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
        if (pstate != null && pstate.getOrigin() != null) {
            pstate.getOrigin().onConsume(event);
        }
    }

    /**
     * Called when a player takes damage
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            PlayerState pstate = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
            if (pstate != null && pstate.getOrigin() != null) {
                pstate.getOrigin().onDamage(event);
            }
        }
    }

    /**
     * Called when a projectile (arrow, etc.) hits something
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player p = (Player) event.getEntity().getShooter();
            PlayerState pstate = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
            if (pstate != null && pstate.getOrigin() != null) {
                pstate.getOrigin().onProjectileHit(event);
            }
        }
    }
}
