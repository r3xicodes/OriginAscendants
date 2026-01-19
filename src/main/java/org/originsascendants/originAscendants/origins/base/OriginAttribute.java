package org.originsascendants.originAscendants.origins.base;

import org.bukkit.attribute.Attribute;

/**
 * Enum for origin attributes with default values
 */
public enum OriginAttribute {
    // Scale and size
    SCALE(Attribute.SCALE, 1.0),
    
    // Health and protection
    MAX_HEALTH(Attribute.MAX_HEALTH, 20.0),
    FALL_DAMAGE_MULTIPLIER(Attribute.FALL_DAMAGE_MULTIPLIER, 1.0),
    KNOCKBACK_RESISTANCE(Attribute.KNOCKBACK_RESISTANCE, 0.0),
    
    // Movement and speed
    MOVEMENT_SPEED(Attribute.MOVEMENT_SPEED, 0.1),
    FLYING_SPEED(Attribute.FLYING_SPEED, 0.05),
    
    // Combat
    ATTACK_DAMAGE(Attribute.ATTACK_DAMAGE, 1.0),
    ATTACK_SPEED(Attribute.ATTACK_SPEED, 4.0);
    
    public final Attribute bukkit;
    public final double defaultValue;
    
    OriginAttribute(Attribute bukkit, double defaultValue) {
        this.bukkit = bukkit;
        this.defaultValue = defaultValue;
    }
}
