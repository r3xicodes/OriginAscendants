package org.originsascendants.originAscendants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.originsascendants.originAscendants.player.PlayerRegistry;
import org.originsascendants.originAscendants.player.PlayerState;

public class AbilityListener implements Listener {

    public boolean sneak = false;
    public boolean swap = false;

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();

        PlayerState pstate = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
        if (pstate==null || pstate.getOrigin()==null) { return; }

        e.setCancelled(true);

        if (p.isSneaking()) {
            pstate.getOrigin().secondaryAbility();
        } else {
            pstate.getOrigin().primaryAbility();
        }
    }

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        PlayerState pstate = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
        if (pstate==null || pstate.getOrigin()==null) { return; }
        if (e.isSneaking()) {
            pstate.getOrigin().crouchOn();
        } else {
            pstate.getOrigin().crouchOff();
        }
    }
}
