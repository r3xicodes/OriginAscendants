package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Fairy extends Origin {

    private boolean flying = false;

    public Fairy(PlayerState state) {
        super(state);
        this.primaryCooldown = 10;
        this.secondaryCooldown = 20;
        this.primaryAbilityDoc = new AbilityDoc("Energize", "Buff nearby players.");
        this.secondaryAbilityDoc = new AbilityDoc("Flight", "Toggle flight.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        for (Player nearby : p.getWorld().getPlayers()) {
            if (p.getLocation().distance(nearby.getLocation()) < 20) {
                nearby.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 100, 0));
            }
        }
        p.sendActionBar(Component.text("Energized!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        flying = !flying;
        p.setAllowFlight(flying);
        p.setFlying(flying);
        p.sendActionBar(Component.text("Flight: " + (flying ? "ON" : "OFF")));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }
} 