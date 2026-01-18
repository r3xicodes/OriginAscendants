package org.originsascendants.originAscendants.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

/**
 * Utility class for displaying standardized ability messages to players.
 * Provides consistent formatting across all origin abilities.
 */
public class AbilityDisplay {
    
    private static final String SEPARATOR = "━━━━━━━━━━━━━━━━━━━━━━━";
    
    /**
     * Display primary ability activation message
     */
    public static void showPrimaryAbility(Player player, String abilityName, String effect, NamedTextColor color) {
        player.sendMessage(Component.text(SEPARATOR, NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.text("⚔ " + abilityName + " ⚔", color)
                .append(Component.text(" - " + effect, NamedTextColor.YELLOW)));
    }
    
    /**
     * Display secondary ability activation message
     */
    public static void showSecondaryAbility(Player player, String abilityName, String effect, NamedTextColor color) {
        player.sendMessage(Component.text(SEPARATOR, NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.text("✦ " + abilityName + " ✦", color)
                .append(Component.text(" - " + effect, NamedTextColor.YELLOW)));
    }
    
    /**
     * Display crouch ability activation message
     */
    public static void showCrouchAbility(Player player, String abilityName, String effect, NamedTextColor color) {
        player.sendMessage(Component.text(SEPARATOR, NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.text("● " + abilityName + " ●", color)
                .append(Component.text(" - " + effect, NamedTextColor.YELLOW)));
    }
    
    /**
     * Display a stat line (damage, heal, enemies affected, etc.)
     */
    public static void showStat(Player player, String label, Object value, NamedTextColor color) {
        player.sendMessage(Component.text(label + ": ", color)
                .append(Component.text(value.toString(), NamedTextColor.WHITE)));
    }
    
    /**
     * Display multiple stats at once
     */
    public static void showStats(Player player, String... stats) {
        for (String stat : stats) {
            if (stat.contains(":")) {
                String[] parts = stat.split(":", 2);
                player.sendMessage(Component.text(parts[0] + ": ", NamedTextColor.GRAY)
                        .append(Component.text(parts[1].trim(), NamedTextColor.WHITE)));
            }
        }
    }
    
    /**
     * Display cooldown message when ability is on cooldown
     */
    public static void showCooldown(Player player, String abilityName, int secondsRemaining) {
        player.sendActionBar(Component.text(abilityName + " on cooldown: ", NamedTextColor.RED)
                .append(Component.text(secondsRemaining + "s", NamedTextColor.YELLOW)));
    }
    
    /**
     * Display insufficient resource message
     */
    public static void showInsufficientResource(Player player, String resource, int current, int required) {
        player.sendMessage(Component.text(SEPARATOR, NamedTextColor.DARK_GRAY));
        player.sendMessage(Component.text("⚠ INSUFFICIENT " + resource.toUpperCase() + " ⚠", NamedTextColor.RED));
        player.sendMessage(Component.text("Required: " + required + " | Have: " + current, NamedTextColor.YELLOW));
    }
    
    /**
     * Display action bar with color
     */
    public static void showActionBar(Player player, String message, NamedTextColor color) {
        player.sendActionBar(Component.text(message, color));
    }
}
