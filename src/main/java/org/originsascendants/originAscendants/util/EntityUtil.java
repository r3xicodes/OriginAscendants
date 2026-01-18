package org.originsascendants.originAscendants.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for entity operations
 */
public class EntityUtil {
    
    /**
     * Damage entity with knockback
     */
    public static void damageWithKnockback(LivingEntity target, double damage, Vector knockback) {
        if (target == null) return;
        target.damage(damage);
        target.setVelocity(target.getVelocity().add(knockback));
    }
    
    /**
     * Knockback entity away from a direction vector
     */
    public static void knockbackAway(LivingEntity target, Vector direction, double force) {
        if (target == null || direction == null) return;
        target.setVelocity(target.getVelocity().add(direction.clone().normalize().multiply(force)));
    }
    
    /**
     * Pull entity toward source
     */
    public static void pullToward(LivingEntity target, LivingEntity source, double force) {
        if (target == null || source == null) return;
        Vector direction = source.getLocation().toVector().subtract(target.getLocation().toVector()).normalize();
        target.setVelocity(target.getVelocity().add(direction.multiply(force)));
    }
    
    /**
     * Check if entity can see target
     */
    public static boolean canSee(LivingEntity entity, Entity target) {
        if (entity == null || target == null) return false;
        return entity.hasLineOfSight(target);
    }
    
    /**
     * Get nearby entities of type
     */
    public static List<LivingEntity> getNearbyEntities(LivingEntity center, double radius, Class<?> type) {
        if (center == null) return new ArrayList<>();
        return new ArrayList<>(center.getWorld().getNearbyLivingEntities(center.getLocation(), radius));
    }
}

