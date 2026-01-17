package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Pawsworn extends Origin {

    private boolean dashing = false;

    public Pawsworn(PlayerState state) {
        super(state);
        this.primaryCooldown = 15;
        this.secondaryCooldown = 20;
        this.primaryAbilityDoc = new AbilityDoc("Pounce", "Leap forward with damage boost.");
        this.secondaryAbilityDoc = new AbilityDoc("Dash", "Toggle quick dashing.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.setVelocity(p.getLocation().getDirection().multiply(1.8).add(new org.bukkit.util.Vector(0, 0.5, 0)));
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 1, false, false));
        p.sendActionBar(Component.text("Pounce!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        dashing = !dashing;
        if (dashing) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            p.sendActionBar(Component.text("Dash ON"));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
            p.sendActionBar(Component.text("Dash OFF"));
        }
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
