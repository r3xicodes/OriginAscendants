package org.originsascendants.originAscendants.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

/**
 * Utility class for displaying simplified ability messages to players.
 * Shows only ability names with color coding.
 */
public class AbilityDisplay {
    
    /**
     * Display primary ability activation
     */
    public static void showPrimaryAbility(Player player, String abilityName, NamedTextColor color) {
        player.sendMessage(Component.text("⚔ " + abilityName, color));
    }
    
    /**
     * Display secondary ability activation
     */
    public static void showSecondaryAbility(Player player, String abilityName, NamedTextColor color) {
        player.sendMessage(Component.text("✦ " + abilityName, color));
    }
    
    /**
     * Display crouch ability activation
     */
    public static void showCrouchAbility(Player player, String abilityName, NamedTextColor color) {
        player.sendMessage(Component.text("● " + abilityName, color));
    }
    
    /**
     * Display on action bar
     */
    public static void showActionBar(Player player, String abilityName, NamedTextColor color) {
        player.sendActionBar(Component.text(abilityName, color));
    }
}

