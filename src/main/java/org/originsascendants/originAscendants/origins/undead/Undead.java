package org.originsascendants.originAscendants.origins.undead;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
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
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMaxHealth(p, 20.0);  // 10 hearts
        setMovementSpeed(p, 0.100); // 100% speed
        setAttackDamage(p, 1.0); // normal melee damage
        
        // Passive: Undead Fortitude - Resistance I at all times
        org.bukkit.potion.PotionEffect resistance = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("RESISTANCE", resistance);
        p.addPotionEffect(resistance);
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
                AbilityDisplay.showPrimaryAbility(p, "Infectious Bite", NamedTextColor.RED);
                BossBarManager.showCooldownBar(p, "Infectious Bite", primaryCooldown, primaryCooldown);
                resetPrimaryCooldown();
                hit = true;
                break;
            }
        }
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION, 200, 1, false, false));
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 200, 1, false, false));
        AbilityDisplay.showSecondaryAbility(p, "Tenacious", NamedTextColor.RED);
        BossBarManager.showCooldownBar(p, "Tenacious", secondaryCooldown, secondaryCooldown);
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

