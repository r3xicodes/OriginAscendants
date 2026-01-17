package org.originsascendants.originAscendants.origins;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import java.util.UUID;

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
     * Apply attribute modifiers to the player (simplified for compatibility)
     */
    public void applyAttributes() {
        // Can be overridden in subclasses
    }

    /**
     * Remove all custom attribute modifiers from the player
     */
    public void removeAttributes() {
        // Can be overridden in subclasses
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
        }
    }

    /**
     * Helper method to modify player attributes by percentage
     */
    protected void modifyAttributePercent(Player player, Attribute attribute, double percent) {
        if (player.getAttribute(attribute) != null) {
            AttributeModifier modifier = new AttributeModifier(
                    UUID.randomUUID(),
                    "origin_" + attribute.name().toLowerCase(),
                    percent,
                    AttributeModifier.Operation.MULTIPLY_SCALAR_1
            );
            player.getAttribute(attribute).addModifier(modifier);
        }
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
}
