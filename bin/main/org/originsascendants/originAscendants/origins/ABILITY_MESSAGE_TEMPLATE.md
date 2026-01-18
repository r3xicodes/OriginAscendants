/**
 * ABILITY MESSAGE TEMPLATE FOR ALL ORIGINS
 * 
 * Copy this template into each origin class to add consistent ability messages.
 * Replace placeholders with actual ability information.
 */

// In primaryAbility() method:
@Override
public void primaryAbility() {
    Player p = state.toBukkit();
    if (!isPrimaryReady()) {
        AbilityDisplay.showCooldown(p, "ABILITY_NAME", getCooldownSeconds(primaryCooldown - primaryCooldownCounter));
        return;
    }
    
    // Perform ability logic here
    // ... your ability code ...
    
    // Display ability activation
    AbilityDisplay.showPrimaryAbility(p, "ABILITY_NAME", "Effect Description", NamedTextColor.COLOR);
    AbilityDisplay.showStat(p, "Damage", "X", NamedTextColor.RED);
    AbilityDisplay.showStat(p, "Radius", "X blocks", NamedTextColor.YELLOW);
    
    // Display action bar
    AbilityDisplay.showActionBar(p, "ABILITY_NAME activated!", NamedTextColor.COLOR);
    
    resetPrimaryCooldown();
}

// In secondaryAbility() method:
@Override
public void secondaryAbility() {
    Player p = state.toBukkit();
    if (!isSecondaryReady()) {
        AbilityDisplay.showCooldown(p, "ABILITY_NAME", getCooldownSeconds(secondaryCooldown - secondaryCooldownCounter));
        return;
    }
    
    // Perform ability logic here
    // ... your ability code ...
    
    // Display ability activation
    AbilityDisplay.showSecondaryAbility(p, "ABILITY_NAME", "Effect Description", NamedTextColor.COLOR);
    AbilityDisplay.showStats(p, "Affected:1", "Status", "Buff Applied");
    
    // Display action bar
    AbilityDisplay.showActionBar(p, "ABILITY_NAME activated!", NamedTextColor.COLOR);
    
    resetSecondaryCooldown();
}

// In crouchAbility() method:
@Override
public void crouchOn() {
    Player p = state.toBukkit();
    if (!isCrouchReady()) return;
    
    // Perform ability logic
    
    AbilityDisplay.showCrouchAbility(p, "ABILITY_NAME", "Effect Description", NamedTextColor.COLOR);
    resetCrouchCooldown();
}

/**
 * COLOR SUGGESTIONS BY ORIGIN TYPE:
 * 
 * Fire-based (Blazeborn, Phytokin, Alchemist): NamedTextColor.RED, GOLD
 * Water/Ice-based (Merling, Breezeborn): NamedTextColor.AQUA, BLUE
 * Undead/Dark (Undead, Vampire, Phantom): NamedTextColor.DARK_PURPLE, DARK_GRAY
 * Nature-based (Phytokin, Fairy): NamedTextColor.GREEN, DARK_GREEN
 * Strength-based (Giant, Werewolf): NamedTextColor.RED, DARK_RED
 * Magic-based (Shulk, Ender): NamedTextColor.LIGHT_PURPLE, DARK_PURPLE
 * Speed-based (Chicken, Bunny): NamedTextColor.YELLOW, GOLD
 * Combat-based (Huntsman, Arachnid): NamedTextColor.DARK_RED, RED
 * Misc: NamedTextColor.GOLD, YELLOW
 */

/**
 * REQUIRED IMPORTS:
 * 
 * import net.kyori.adventure.text.format.NamedTextColor;
 * import org.originsascendants.originAscendants.util.AbilityDisplay;
 * import org.bukkit.entity.Player;
 */
