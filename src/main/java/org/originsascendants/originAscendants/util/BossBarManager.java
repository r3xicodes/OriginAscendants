package org.originsascendants.originAscendants.util;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing boss bars for ability resources and cooldowns.
 * Displays visual progress bars for:
 * - Heat levels (Blazeborn)
 * - Mana/energy levels
 * - Cooldown timers
 * - Status effects
 */
public class BossBarManager {
    
    private static final Map<String, BossBar> activeBars = new HashMap<>();
    
    /**
     * Show a resource level boss bar (heat, mana, energy, etc.)
     */
    public static void showResourceBar(Player player, String resourceName, int current, int max, BossBar.Color color) {
        String key = player.getUniqueId() + ":" + resourceName;
        
        // Remove old bar if exists
        BossBar existingBar = activeBars.get(key);
        if (existingBar != null) {
            player.hideBossBar(existingBar);
        }
        
        // Calculate progress (0.0 to 1.0)
        double progress = Math.max(0, Math.min(1.0, (double) current / max));
        
        // Create new bar
        BossBar bar = BossBar.bossBar(
                Component.text(resourceName + ": " + current + "/" + max, NamedTextColor.WHITE),
                (float) progress,
                color,
                BossBar.Overlay.NOTCHED_20
        );
        
        player.showBossBar(bar);
        activeBars.put(key, bar);
    }
    
    /**
     * Show a cooldown boss bar
     */
    public static void showCooldownBar(Player player, String abilityName, int secondsRemaining, int totalSeconds) {
        String key = player.getUniqueId() + ":cooldown:" + abilityName;
        
        BossBar existingBar = activeBars.get(key);
        if (existingBar != null) {
            player.hideBossBar(existingBar);
        }
        
        double progress = Math.max(0, Math.min(1.0, (double) secondsRemaining / totalSeconds));
        
        BossBar bar = BossBar.bossBar(
                Component.text(abilityName + " - " + secondsRemaining + "s", NamedTextColor.YELLOW),
                (float) progress,
                BossBar.Color.YELLOW,
                BossBar.Overlay.PROGRESS
        );
        
        player.showBossBar(bar);
        activeBars.put(key, bar);
    }
    
    /**
     * Hide a specific resource bar
     */
    public static void hideResourceBar(Player player, String resourceName) {
        String key = player.getUniqueId() + ":" + resourceName;
        BossBar bar = activeBars.remove(key);
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }
    
    /**
     * Hide a cooldown bar
     */
    public static void hideCooldownBar(Player player, String abilityName) {
        String key = player.getUniqueId() + ":cooldown:" + abilityName;
        BossBar bar = activeBars.remove(key);
        if (bar != null) {
            player.hideBossBar(bar);
        }
    }
    
    /**
     * Hide all bars for a player
     */
    public static void hideAllBars(Player player) {
        String playerUUID = player.getUniqueId().toString();
        activeBars.entrySet().removeIf(entry -> {
            if (entry.getKey().startsWith(playerUUID)) {
                player.hideBossBar(entry.getValue());
                return true;
            }
            return false;
        });
    }
}
