package org.originsascendants.originAscendants.origins.merling;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerItemConsumeEvent;

@SuppressWarnings("unused")
public class Merling extends Origin {

    private boolean waterBreathing = false;
    private int hydrationLevel = 100; // 0-100
    private static final int MAX_HYDRATION = 100;

    public Merling(PlayerState state) {
        super(state);
        this.primaryCooldown = 15;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Swift Swim", "Swim extremely fast.");
        this.secondaryAbilityDoc = new AbilityDoc("Mermaids Grace", "Toggle water buffs.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        // Merlings are aquatic, slightly slower on land, more vulnerable to fall damage
        setMovementSpeed(p, 0.08);
        setFallDamageMultiplier(p, 1.5);
        setMaxHealth(p, 19.0);
        setAttackDamage(p, 1.0); // normal melee damage
    }

    @Override
    public void removeAttributes() {
        super.removeAttributes();
        // Clear hydration bar
        BossBarManager.hideResourceBar(state.toBukkit(), "Hydration");
        hydrationLevel = 100;
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        if (p.isInWater()) {
            p.setVelocity(p.getVelocity().multiply(2.0));
            AbilityDisplay.showPrimaryAbility(p, "Swift Swim", NamedTextColor.AQUA);
            p.sendActionBar(Component.text("Swimming at full speed!", NamedTextColor.AQUA));
            resetPrimaryCooldown();
        } else {
            p.sendActionBar(Component.text("Must be in water!", NamedTextColor.RED));
        }
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        waterBreathing = !waterBreathing;
        if (waterBreathing) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            AbilityDisplay.showSecondaryAbility(p, "Mermaids Grace", NamedTextColor.AQUA);
            p.sendActionBar(Component.text("Water buffs activated!", NamedTextColor.AQUA));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.WATER_BREATHING);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
            p.sendActionBar(Component.text("Water buffs deactivated", NamedTextColor.GRAY));
        }
        resetSecondaryCooldown();
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        
        // Update hydration based on water proximity
        if (p.isInWater()) {
            // Restore hydration when in water
            hydrationLevel = Math.min(MAX_HYDRATION, hydrationLevel + 2);
        } else if (p.getWorld().hasStorm() && p.getWorld().isThundering()) {
            // Slight hydration gain during rain/thunder
            hydrationLevel = Math.min(MAX_HYDRATION, hydrationLevel + 1);
        } else {
            // Lose hydration over time when not in water
            hydrationLevel = Math.max(0, hydrationLevel - 1);
        }
        
        // Display hydration bar
        BossBar.Color color;
        if (hydrationLevel > 66) {
            color = BossBar.Color.BLUE;
        } else if (hydrationLevel > 33) {
            color = BossBar.Color.PURPLE;
        } else {
            color = BossBar.Color.WHITE;
        }
        
        BossBarManager.showResourceBar(p, "Hydration", hydrationLevel, MAX_HYDRATION, color);
        
        // Apply slowness debuff when severely dehydrated
        if (hydrationLevel < 20) {
            if (!p.hasPotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS)) {
                p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 40, 0, false, false));
            }
        }
        
        updateCooldowns();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }

    /**
     * Merlings eat only cookies, bread, meat, and fish
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Material foodType = event.getItem().getType();
        Player p = state.toBukkit();
        
        // Allowed foods: cookies, bread, and meat+fish
        boolean isAllowed = foodType == Material.COOKIE ||
                           foodType == Material.BREAD ||
                           foodType == Material.BEEF || foodType == Material.COOKED_BEEF ||
                           foodType == Material.PORKCHOP || foodType == Material.COOKED_PORKCHOP ||
                           foodType == Material.CHICKEN || foodType == Material.COOKED_CHICKEN ||
                           foodType == Material.MUTTON || foodType == Material.COOKED_MUTTON ||
                           foodType == Material.RABBIT || foodType == Material.COOKED_RABBIT ||
                           foodType == Material.COD || foodType == Material.COOKED_COD ||
                           foodType == Material.SALMON || foodType == Material.COOKED_SALMON ||
                           foodType == Material.TROPICAL_FISH || 
                           foodType == Material.PUFFERFISH;
        
        if (!isAllowed) {
            p.sendActionBar(Component.text("Merlings only eat cookies, bread, meat, or fish!").color(NamedTextColor.RED));
            event.setCancelled(true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_HURT, 1.0f, 0.8f);
        }
    }
}

