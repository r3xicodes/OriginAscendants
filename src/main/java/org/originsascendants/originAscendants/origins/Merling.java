package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Merling extends Origin {

    private boolean waterBreathing = false;

    public Merling(PlayerState state) {
        super(state);
        this.primaryCooldown = 15;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Swift Swim", "Swim extremely fast.");
        this.secondaryAbilityDoc = new AbilityDoc("Mermaids Grace", "Toggle water buffs.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        if (p.isInWater()) {
            p.setVelocity(p.getVelocity().multiply(2.0));
            p.sendActionBar(Component.text("Swift swim!"));
            resetPrimaryCooldown();
        } else {
            p.sendActionBar(Component.text("Must be in water!"));
        }
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        waterBreathing = !waterBreathing;
        if (waterBreathing) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            p.sendActionBar(Component.text("Mermaids Grace ON"));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.WATER_BREATHING);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
            p.sendActionBar(Component.text("Mermaids Grace OFF"));
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
