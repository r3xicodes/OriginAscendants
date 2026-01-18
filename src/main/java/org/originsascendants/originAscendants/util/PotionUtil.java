package org.originsascendants.originAscendants.util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Utility class for potion effect operations
 */
public class PotionUtil {
    
    /**
     * Apply potion effect to entity
     */
    public static void applyPotion(LivingEntity entity, PotionEffectType type, int durationTicks, int amplifier) {
        if (entity == null) return;
        entity.addPotionEffect(new PotionEffect(type, durationTicks, amplifier, false, false));
    }
    
    /**
     * Apply potion with particles
     */
    public static void applyPotionWithParticles(LivingEntity entity, PotionEffectType type, int durationTicks, int amplifier) {
        if (entity == null) return;
        applyPotion(entity, type, durationTicks, amplifier);
        ParticleUtil.spawnParticlesAroundPlayer((org.bukkit.entity.Player) entity, org.bukkit.Particle.EFFECT, 10);
    }
    
    /**
     * Remove potion effect
     */
    public static void removePotion(LivingEntity entity, PotionEffectType type) {
        if (entity == null) return;
        entity.removePotionEffect(type);
    }
    
    /**
     * Check if entity has potion effect
     */
    public static boolean hasPotion(LivingEntity entity, PotionEffectType type) {
        if (entity == null) return false;
        return entity.hasPotionEffect(type);
    }
}
