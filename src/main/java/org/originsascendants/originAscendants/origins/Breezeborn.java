package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Breezeborn extends Origin {

    public Breezeborn(PlayerState state) {
        super(state);
        this.primaryCooldown = 25;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Breeze Ball", "Launch a wind projectile.");
        this.secondaryAbilityDoc = new AbilityDoc("Wind Launch", "Launch yourself upward.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        org.bukkit.entity.Projectile projectile = p.getWorld().spawn(p.getEyeLocation(), org.bukkit.entity.Fireball.class, fb -> {
            fb.setVelocity(p.getDirection().multiply(2.0));
            fb.setShooter(p);
            fb.setYield(0.1f);
        });
        p.sendActionBar(Component.text("Breeze ball launched!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.setVelocity(new org.bukkit.util.Vector(0, 2.0, 0));
        p.sendActionBar(Component.text("Wind launch!"));
        resetSecondaryCooldown();
    }

    @Override
    public void tick() {
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }
}
