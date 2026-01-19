package org.originsascendants.originAscendants.origins.base;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.BossBarManager;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract base class for all player origins in the OriginAscendants plugin.
 * Each origin provides unique passive abilities and active abilities that can be triggered.
 * 
 * Features:
 * - Cooldown management for abilities
 * - Attribute modifiers for passive effects
 * - Potion effect tracking
 * - Ability documentation for GUI
 */
public abstract class Origin {
    protected final PlayerState state;

    // These are used in the selection GUI to hold information about the origin
    public AbilityDoc primaryAbilityDoc;
    public AbilityDoc secondaryAbilityDoc;
    public AbilityDoc crouchAbilityDoc;

    // Ability cooldown counters
    protected int primaryCooldownCounter = 0;
    protected int secondaryCooldownCounter = 0;
    protected int crouchCooldownCounter = 0;
    
    // Cooldown max values (in ticks, 20 ticks = 1 second)
    protected int primaryCooldown = 10;
    protected int secondaryCooldown = 10;
    protected int crouchCooldown = 10;
    
    // Track applied potion effects for cleanup
    protected Map<String, PotionEffect> appliedEffects = new HashMap<>();
    
    // Track applied attribute modifiers for cleanup
    protected Map<String, AttributeModifier> appliedModifiers = new HashMap<>();

    protected Origin(PlayerState state) {
        this.state = state;
    }

    //tick logic (aka passive logic)
    public void tick(){}

    // Handled automatically
    public void primaryAbility(){}
    public void secondaryAbility(){}

    // Executes on crouch
    public void crouchOn(){}
    // Executes on crouch release
    public void crouchOff(){}

    /**
     * Event handler: Called when player attacks an entity
     * Can be overridden for special attack effects (cobwebs, combos, etc.)
     */
    public void onAttackEntity(org.bukkit.event.entity.EntityDamageByEntityEvent event) {}

    /**
     * Event handler: Called when player consumes food/drink
     * Can be overridden for diet restrictions
     */
    public void onConsume(org.bukkit.event.player.PlayerItemConsumeEvent event) {}

    /**
     * Event handler: Called when player takes damage
     * Can be overridden for reaction abilities (dodge, counter, etc.)
     */
    public void onDamage(org.bukkit.event.entity.EntityDamageEvent event) {}

    /**
     * Event handler: Called when player shoots a projectile
     * Can be overridden for projectile effects
     */
    public void onProjectileHit(org.bukkit.event.entity.ProjectileHitEvent event) {}

    /**
     * Apply attribute modifiers to the player (simplified for compatibility)
     */
    public void applyAttributes() {
        // Can be overridden in subclasses
    }

    /**
     * Remove all custom attribute modifiers from the player
     */
    public void removeAttributes() {
        Player player = state.toBukkit();
        
        // Remove all attribute modifiers
        for (AttributeModifier modifier : appliedModifiers.values()) {
            for (Attribute attr : Attribute.values()) {
                if (player.getAttribute(attr) != null) {
                    player.getAttribute(attr).removeModifier(modifier);
                }
            }
        }
        appliedModifiers.clear();
        
        // Remove all potion effects that were applied
        for (PotionEffect effect : appliedEffects.values()) {
            player.removePotionEffect(effect.getType());
        }
        appliedEffects.clear();
        
        // Hide all boss bars
        BossBarManager.hideAllBars(player);
    }

    /**
     * Helper method to modify player attributes
     */
    protected void modifyAttribute(Player player, Attribute attribute, double amount) {
        if (player.getAttribute(attribute) != null) {
            AttributeModifier modifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "origin_" + attribute.name().toLowerCase(),
                    amount - player.getAttribute(attribute).getBaseValue(),
                    AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(attribute).addModifier(modifier);
            appliedModifiers.put(attribute.name(), modifier);
        }
    }

    /**
     * Helper method to set attribute to a specific value
     */
    protected void setAttribute(Player player, Attribute attribute, double value) {
        if (player.getAttribute(attribute) != null) {
            double current = player.getAttribute(attribute).getBaseValue();
            double difference = value - current;
            
            AttributeModifier modifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "origin_" + attribute.name().toLowerCase(),
                    difference,
                    AttributeModifier.Operation.ADD_NUMBER
            );
            player.getAttribute(attribute).addModifier(modifier);
            appliedModifiers.put(attribute.name(), modifier);
        }
    }

    /**
     * Helper method to modify player attributes by percentage
     */
    protected void modifyAttributePercent(Player player, Attribute attribute, double percent) {
        if (player.getAttribute(attribute) != null) {
            AttributeModifier modifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "origin_" + attribute.name().toLowerCase() + "_percent",
                    percent,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1
            );
            player.getAttribute(attribute).addModifier(modifier);
            appliedModifiers.put(attribute.name() + "_percent", modifier);
        }
    }

    /**
     * Set scale for the origin
     */
    protected void setScale(Player player, double scale) {
        setAttribute(player, Attribute.SCALE, scale);
    }

    /**
     * Set max health for the origin
     */
    protected void setMaxHealth(Player player, double health) {
        setAttribute(player, Attribute.MAX_HEALTH, health);
    }

    /**
     * Set fall damage multiplier (1.0 = normal, 0.5 = half damage, 0.0 = no damage)
     */
    protected void setFallDamageMultiplier(Player player, double multiplier) {
        setAttribute(player, Attribute.FALL_DAMAGE_MULTIPLIER, multiplier);
    }

    /**
     * Set knockback resistance (0.0 to 1.0, where 1.0 is immune)
     */
    protected void setKnockbackResistance(Player player, double resistance) {
        setAttribute(player, Attribute.KNOCKBACK_RESISTANCE, Math.min(1.0, Math.max(0.0, resistance)));
    }

    /**
     * Set movement speed multiplier (0.1 = normal, 0.2 = double speed)
     */
    protected void setMovementSpeed(Player player, double speed) {
        setAttribute(player, Attribute.MOVEMENT_SPEED, speed);
    }

    /**
     * Set flying speed multiplier (0.05 = normal, 0.1 = double speed)
     */
    protected void setFlyingSpeed(Player player, double speed) {
        setAttribute(player, Attribute.FLYING_SPEED, speed);
    }

    /**
     * Set attack damage (1.0 = normal, 2.0 = double damage)
     */
    protected void setAttackDamage(Player player, double damage) {
        setAttribute(player, Attribute.ATTACK_DAMAGE, damage);
    }

    /**
     * Set attack speed (4.0 = normal, 8.0 = double speed)
     */
    protected void setAttackSpeed(Player player, double speed) {
        setAttribute(player, Attribute.ATTACK_SPEED, speed);
    }

    /**
     * Modify scale by percentage
     */
    protected void modifyScalePercent(Player player, double percent) {
        modifyAttributePercent(player, Attribute.SCALE, percent);
    }

    /**
     * Modify movement speed by percentage
     */
    protected void modifyMovementSpeedPercent(Player player, double percent) {
        modifyAttributePercent(player, Attribute.MOVEMENT_SPEED, percent);
    }

    /**
     * Modify attack damage by percentage
     */
    protected void modifyAttackDamagePercent(Player player, double percent) {
        modifyAttributePercent(player, Attribute.ATTACK_DAMAGE, percent);
    }

    /**
     * Update cooldown timers (called every tick)
     */
    public void updateCooldowns() {
        if (primaryCooldownCounter < primaryCooldown) primaryCooldownCounter++;
        if (secondaryCooldownCounter < secondaryCooldown) secondaryCooldownCounter++;
        if (crouchCooldownCounter < crouchCooldown) crouchCooldownCounter++;
    }

    /**
     * Check if primary ability is ready
     */
    protected boolean isPrimaryReady() {
        return primaryCooldownCounter >= primaryCooldown;
    }

    /**
     * Check if secondary ability is ready
     */
    protected boolean isSecondaryReady() {
        return secondaryCooldownCounter >= secondaryCooldown;
    }

    /**
     * Check if crouch ability is ready
     */
    protected boolean isCrouchReady() {
        return crouchCooldownCounter >= crouchCooldown;
    }

    /**
     * Reset primary ability cooldown
     */
    protected void resetPrimaryCooldown() {
        primaryCooldownCounter = 0;
    }

    /**
     * Reset secondary ability cooldown
     */
    protected void resetSecondaryCooldown() {
        secondaryCooldownCounter = 0;
    }

    /**
     * Reset crouch ability cooldown
     */
    protected void resetCrouchCooldown() {
        crouchCooldownCounter = 0;
    }
    
    /**
     * Safely add a potion effect and track it for removal when origin is switched
     */
    protected void addTrackedPotionEffect(Player player, PotionEffectType type, int duration, int amplifier) {
        player.addPotionEffect(new PotionEffect(type, duration, amplifier, false, false));
        appliedEffects.put(type.getName(), new PotionEffect(type, duration, amplifier));
    }
    
    /**
     * Get the display name of this origin
     */
    public String getDisplayName() {
        return this.getClass().getSimpleName();
    }
    
    /**
     * Get the description of this origin
     */
    public String getDescription() {
        return "An origin with unique abilities";
    }
    
    /**
     * Get the cooldown in seconds for display (converted from ticks)
     */
    protected int getCooldownSeconds(int ticks) {
        return ticks / 20;
    }
}
