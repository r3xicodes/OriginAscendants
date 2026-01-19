package org.originsascendants.originAscendants.origins.sculkborn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.originsascendants.originAscendants.util.ParticleUtil;
import org.originsascendants.originAscendants.util.PotionUtil;
import org.originsascendants.originAscendants.util.EntityUtil;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * Sculkborn Origin - Warden-like creature with darkness and sonic abilities
 * Passive abilities:
 * - Darkness aura surrounds the player
 * - Enhanced attack damage and knockback resistance
 * - Increased size (1.1x scale)
 */
@SuppressWarnings("unused")
public class Sculkborn extends Origin {

    private int sonicChargeLevel = 0;
    private int echolocationTicks = 0;
    private static final int MAX_SONIC_CHARGE = 100;
    private static final int ECHOLOCATION_DURATION = 300;

    public Sculkborn(PlayerState state) {
        super(state);
        this.primaryCooldown = 30;
        this.secondaryCooldown = 25;
        this.primaryAbilityDoc = new AbilityDoc("Sonic Shriek", "Emit a devastating sonic boom dealing damage and applying blindness/darkness.");
        this.secondaryAbilityDoc = new AbilityDoc("Echolocation Toggle", "Toggle entity highlighting via echolocation.");
    }

    @Override
    public String getDisplayName() {
        return "§8Sculk§fborn";
    }

    @Override
    public String getDescription() {
        return "Warden-like creature with sonic and darkness abilities";
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        
        // Warden-like stats
        setMaxHealth(p, 30.0);  // 15 hearts
        setAttackDamage(p, 1.8);
        setKnockbackResistance(p, 0.25);
        setMovementSpeed(p, 0.10);
        setAttackSpeed(p, 2.0);
        
        // Apply persistent darkness aura
        p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void removeAttributes() {
        super.removeAttributes();
        Player p = state.toBukkit();
        p.removePotionEffect(PotionEffectType.DARKNESS);
        sonicChargeLevel = 0;
        echolocationTicks = 0;
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 25);
        int hits = 0;
        
        for (LivingEntity le : entities) {
            if (le != p) {
                // Apply effects
                PotionUtil.applyPotion(le, PotionEffectType.BLINDNESS, 60, 0);
                PotionUtil.applyPotion(le, PotionEffectType.DARKNESS, 80, 0);
                PotionUtil.applyPotion(le, PotionEffectType.SLOWNESS, 60, 2);
                PotionUtil.applyPotion(le, PotionEffectType.MINING_FATIGUE, 60, 1);
                
                // Knockback
                Vector direction = le.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                EntityUtil.knockbackAway(le, direction, 2.0);
                
                // Damage
                le.damage(5.0);
                
                // Particles
                ParticleUtil.spawnParticles(le.getLocation(), Particle.SCULK_CHARGE, 5, 0.5, 0.5, 0.5, 0);
                
                hits++;
            }
        }
        
        // Sound and feedback
        p.playSound(p.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 2.0f, 1.0f);
        AbilityDisplay.showPrimaryAbility(p, "Sonic Shriek", NamedTextColor.DARK_PURPLE);
        BossBarManager.showCooldownBar(p, "Sonic Shriek", primaryCooldown, primaryCooldown);
        p.sendActionBar(Component.text("Shriek! ", NamedTextColor.DARK_PURPLE).append(Component.text(hits + " entities hit", NamedTextColor.YELLOW)));
        
        resetPrimaryCooldown();
        sonicChargeLevel = 0;
        BossBarManager.hideResourceBar(p, "Sonic Charge");
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        
        // Toggle echolocation
        if (echolocationTicks > 0) {
            echolocationTicks = 0;
            p.sendActionBar(Component.text("Echolocation disabled", NamedTextColor.GRAY));
        } else {
            echolocationTicks = ECHOLOCATION_DURATION;
            var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 40);
            int count = 0;
            for (LivingEntity le : entities) {
                if (le != p) {
                    PotionUtil.applyPotion(le, PotionEffectType.GLOWING, 100, 0);
                    count++;
                }
            }
            AbilityDisplay.showSecondaryAbility(p, "Echolocation", NamedTextColor.DARK_PURPLE);
            BossBarManager.showCooldownBar(p, "Echolocation", secondaryCooldown, secondaryCooldown);
            p.sendActionBar(Component.text("Echolocation! ", NamedTextColor.DARK_PURPLE).append(Component.text(count + " entities highlighted", NamedTextColor.YELLOW)));
        }
        
        resetSecondaryCooldown();
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        
        // Maintain darkness aura
        if (!p.hasPotionEffect(PotionEffectType.DARKNESS)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, Integer.MAX_VALUE, 0, false, false));
        }
        
        // Update echolocation
        if (echolocationTicks > 0) {
            echolocationTicks--;
            
            // Spawn particle effects
            ParticleUtil.spawnParticles(p.getLocation(), Particle.SCULK_CHARGE, 3, 0.2, 0.2, 0.2, 0);
            
            // Refresh glowing on nearby entities
            if (echolocationTicks % 20 == 0) {
                var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 40);
                for (LivingEntity le : entities) {
                    if (le != p && !le.hasPotionEffect(PotionEffectType.GLOWING)) {
                        PotionUtil.applyPotion(le, PotionEffectType.GLOWING, 100, 0);
                    }
                }
            }
        } else {
            // Hide echolocation bar when not active
            BossBarManager.hideResourceBar(p, "Echolocation");
        }
        
        // Update Sonic Shriek charge display
        if (sonicChargeLevel > 0) {
            sonicChargeLevel++;
            if (sonicChargeLevel >= MAX_SONIC_CHARGE) {
                sonicChargeLevel = 0;
            }
            // Show charge bar as resource bar with PURPLE color for better visibility
            BossBarManager.showResourceBar(p, "Sonic Charge", sonicChargeLevel, MAX_SONIC_CHARGE, net.kyori.adventure.bossbar.BossBar.Color.PURPLE);
        } else {
            // Hide charge bar when not charging
            BossBarManager.hideResourceBar(p, "Sonic Charge");
        }
    }

    @Override
    public void crouchOn() {
        Player p = state.toBukkit();
        
        // Sculk Spread: Teleport to nearest nearby entity
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 30);
        LivingEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (LivingEntity le : entities) {
            if (le != p) {
                double distance = le.getLocation().distance(p.getLocation());
                if (distance < nearestDistance) {
                    nearest = le;
                    nearestDistance = distance;
                }
            }
        }
        
        if (nearest != null) {
            ParticleUtil.spawnParticles(p.getLocation(), Particle.SCULK_CHARGE, 10, 0.5, 0.5, 0.5, 0.1);
            p.teleport(nearest.getLocation());
            ParticleUtil.spawnParticles(p.getLocation(), Particle.SCULK_CHARGE, 10, 0.5, 0.5, 0.5, 0.1);
            AbilityDisplay.showCrouchAbility(p, "Sculk Spread", NamedTextColor.DARK_PURPLE);
        } else {
            p.sendActionBar(Component.text("No entities nearby to spread to", NamedTextColor.GRAY));
        }
    }

    @Override
    public void crouchOff() {
        // No action on crouch off
    }

    @Override
    public void onAttackEntity(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();
            
            // Apply darkness and slowness on hit
            PotionUtil.applyPotion(target, PotionEffectType.DARKNESS, 40, 0);
            PotionUtil.applyPotion(target, PotionEffectType.SLOWNESS, 40, 1);
            
            // Particles on hit
            ParticleUtil.spawnParticles(target.getLocation(), Particle.SCULK_CHARGE, 3, 0.2, 0.2, 0.2, 0);
        }
    }

    @Override
    public void onDamage(org.bukkit.event.entity.EntityDamageEvent event) {
        // Reduce fall damage, increase magic damage
        if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL) {
            event.setDamage(event.getDamage() * 0.6);
        } else if (event.getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.MAGIC) {
            event.setDamage(event.getDamage() * 1.3);
        }
    }
}
