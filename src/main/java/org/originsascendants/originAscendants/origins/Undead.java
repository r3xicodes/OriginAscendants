package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;

@SuppressWarnings("unused")
public class Undead extends Origin {

    public Undead(PlayerState state) {
        super(state);
        this.primaryCooldown = 15;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Infectious Bite", "Poison melee attacks.");
        this.secondaryAbilityDoc = new AbilityDoc("Tenacious", "Grant regeneration and resistance.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        var targets = p.getWorld().getNearbyLivingEntities(p.getLocation(), 8);
        boolean hit = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) && target != p) {
                target.damage(3.0);
                target.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.POISON, 80, 1, false, false));
                p.sendActionBar(Component.text("Infectious bite!"));
                resetPrimaryCooldown();
                hit = true;
                break;
            }
        }
        if (!hit) {
            p.sendActionBar(Component.text("No target"));
        }
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION, 200, 1, false, false));
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 200, 1, false, false));
        p.sendActionBar(Component.text("Tenacious!"));
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
