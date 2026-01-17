package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Bunny extends Origin {

    private double chargeAmount = 0;

    public Bunny(PlayerState state) {
        super(state);
        this.health = 18;
        this.primaryCooldown = 15;
        this.secondaryCooldown = 25;
        this.primaryAbilityDoc = new AbilityDoc("Charge Jump", "Hold to charge, release to jump high.");
        this.secondaryAbilityDoc = new AbilityDoc("Swift Escape", "Dash away quickly.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        chargeAmount = Math.min(3.0, chargeAmount + 0.5);
        org.bukkit.util.Vector up = new org.bukkit.util.Vector(0, chargeAmount, 0);
        p.setVelocity(up);
        p.sendActionBar(Component.text("Jump: " + String.format("%.1f", chargeAmount)));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        org.bukkit.util.Vector dir = p.getLocation().getDirection().normalize().multiply(1.5);
        p.setVelocity(dir.add(new org.bukkit.util.Vector(0, 0.5, 0)));
        p.sendActionBar(Component.text("Dashed away!"));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
        chargeAmount = 0;
    }

    @Override
    public void crouchOn() {
        // Charge jump preparation
    }
}
