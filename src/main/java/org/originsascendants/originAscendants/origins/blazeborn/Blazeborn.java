package org.originsascendants.originAscendants.origins.blazeborn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.ParticleUtil;
import org.originsascendants.originAscendants.util.PotionUtil;
import org.originsascendants.originAscendants.util.EntityUtil;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Blazeborn Origin - Tiered heat-based warrior
 * 
 * Heat System: 0-10000°C in tiers, can overheat to 15000°C
 * Tier colors change as heat increases
 * Health scales from 8 hearts (low heat) to 12 hearts (high heat)
 * Must consume fuel items to maintain heat, lose heat in water/rain/damage
 */
@SuppressWarnings("unused")
public class Blazeborn extends Origin {

    // Heat tiers: each tier is 1000°C with different fuel requirements
    private static final int TIER_SIZE = 1000;
    private static final int MIN_HEAT = 0;
    private static final int MAX_NORMAL_HEAT = 10000;
    private static final int MAX_OVERHEAT = 15000;
    
    private static final double HEAT_DECAY_PER_TICK = 2.0; // Gradual heat loss
    private static final double WATER_HEAT_DRAIN = 100.0; // Per tick in water
    private static final double DAMAGE_HEAT_LOSS_PERCENT = 0.15; // 15% of current heat
    
    private int heatLevel = 100; // Start at 100°C
    private double heatDecayAccumulator = 0;
    private int lastHeatTier = 0;

    public Blazeborn(PlayerState state) {
        super(state);
        this.primaryCooldown = 5;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Fuel Consumption", "Consume fuel to increase heat. Use furnace-tier items for current heat level.");
        this.secondaryAbilityDoc = new AbilityDoc("Thermal Purge", "Instantly boost heat and purge negative effects (requires 3000°C).");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMaxHealth(p, 20.0);  // 10 hearts - will increase with heat
        setFallDamageMultiplier(p, 0.5);
        setMovementSpeed(p, 0.12);
        setAttackSpeed(p, 5.0);
        // Fire resistance (lava immunity granted in tick)
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public String getDescription() {
        return "Master of tiered heat with fuel-based power";
    }

    @Override
    public void removeAttributes() {
        super.removeAttributes();
        Player p = state.toBukkit();
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        
        // Lava immunity
        if (p.getFireTicks() > 0) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 40, 0, false, false));
        }
        
        // Heat decay over time
        heatDecayAccumulator += HEAT_DECAY_PER_TICK;
        if (heatDecayAccumulator >= 1.0) {
            heatLevel = Math.max(0, (int) (heatLevel - heatDecayAccumulator));
            heatDecayAccumulator = 0;
        }
        
        // Water heat drain (very fast)
        if (p.isInWater() || p.getWorld().hasStorm() && p.getLocation().getBlock().getType() == Material.AIR) {
            double drainAmount = WATER_HEAT_DRAIN;
            heatLevel = Math.max(0, (int) (heatLevel - drainAmount));
            if (heatLevel == 0) {
                p.sendActionBar(Component.text("Heat depleted! Respawning at 100°C").color(NamedTextColor.DARK_RED));
                // Teleport player to spawn or cause death
                p.setHealth(0);
            }
        }
        
        // Update health based on heat level
        updateHealthFromHeat(p);
        
        // Display heat bar with tier color
        displayHeatBar(p);
        
        updateCooldowns();
    }

    /**
     * Scale health from 8 hearts (low) to 12 hearts (high)
     */
    private void updateHealthFromHeat(Player p) {
        double healthScale = 8.0 + (heatLevel / (double) MAX_NORMAL_HEAT) * 4.0;
        if (heatLevel > MAX_NORMAL_HEAT) {
            // Overheat bonus up to 24 health (12 hearts)
            double overHeatBonus = ((heatLevel - MAX_NORMAL_HEAT) / (double) (MAX_OVERHEAT - MAX_NORMAL_HEAT)) * 2.0;
            healthScale += overHeatBonus;
        }
        healthScale = Math.min(24, healthScale);
        setMaxHealth(p, healthScale);
    }

    /**
     * Display heat bar with tier-based color
     */
    private void displayHeatBar(Player p) {
        int currentTier = heatLevel / TIER_SIZE;
        int tierProgress = heatLevel % TIER_SIZE;
        
        // Color based on tier
        BossBar.Color color = getTierColor(currentTier);
        String tierName = getTierName(currentTier);
        
        BossBarManager.showResourceBar(p, "Heat Tier " + tierName, tierProgress, TIER_SIZE, color);
        
        // Particle effects increase with heat
        if (heatLevel > 5000) {
            ParticleUtil.spawnParticlesAroundPlayer(p, Particle.FLAME, 2);
        }
        if (heatLevel > 10000) {
            ParticleUtil.spawnParticlesAroundPlayer(p, Particle.LAVA, 1);
        }
    }

    /**
     * Get boss bar color based on heat tier
     */
    private BossBar.Color getTierColor(int tier) {
        return switch (tier) {
            case 0 -> BossBar.Color.GREEN;      // 0-1000°C
            case 1 -> BossBar.Color.YELLOW;     // 1000-2000°C
            case 2 -> BossBar.Color.BLUE;       // 2000-3000°C
            case 3 -> BossBar.Color.PURPLE;     // 3000-4000°C
            case 4 -> BossBar.Color.WHITE;      // 4000-5000°C
            case 5 -> BossBar.Color.RED;        // 5000-6000°C
            case 6 -> BossBar.Color.RED;        // 6000-7000°C
            case 7 -> BossBar.Color.RED;        // 7000-8000°C
            case 8 -> BossBar.Color.RED;        // 8000-9000°C
            case 9 -> BossBar.Color.RED;        // 9000-10000°C
            default -> BossBar.Color.PURPLE;    // 10000+°C (overheat)
        };
    }

    /**
     * Get tier name for display
     */
    private String getTierName(int tier) {
        return switch (tier) {
            case 0 -> "Ember";
            case 1 -> "Flame";
            case 2 -> "Inferno";
            case 3 -> "Blaze";
            case 4 -> "Scorching";
            case 5 -> "Incandescent";
            case 6 -> "Blazing";
            case 7 -> "Stellar";
            case 8 -> "Solar";
            case 9 -> "Supernova";
            default -> "Overheat";
        };
    }

    /**
     * Get fuel value for an item based on furnace burn times
     */
    private int getFuelValue(Material material) {
        return switch (material) {
            // Low tier (0-2000°C)
            case OAK_LOG, BIRCH_LOG, SPRUCE_LOG, DARK_OAK_LOG, ACACIA_LOG, JUNGLE_LOG,
                 MANGROVE_LOG, CHERRY_LOG, OAK_WOOD, BIRCH_WOOD, SPRUCE_WOOD, DARK_OAK_WOOD,
                 ACACIA_WOOD, JUNGLE_WOOD, MANGROVE_WOOD, CHERRY_WOOD -> 150;
            
            case STICK, OAK_PLANKS, BIRCH_PLANKS, SPRUCE_PLANKS, DARK_OAK_PLANKS,
                 ACACIA_PLANKS, JUNGLE_PLANKS, MANGROVE_PLANKS, CHERRY_PLANKS -> 50;
            
            // Mid tier (2000-6000°C)
            case COAL -> 800;
            case CHARCOAL -> 800;
            
            // High tier (6000+°C)
            case BLAZE_ROD -> 1200;
            case LAVA_BUCKET -> 2000;
            
            default -> 0;
        };
    }

    /**
     * Check if fuel item can be used at current heat tier
     */
    private boolean canUseFuelAtTier(Material material, int tier) {
        int fuelValue = getFuelValue(material);
        if (fuelValue == 0) return false;
        
        // Different tiers require different fuel quality
        if (tier <= 2 && fuelValue >= 50) return true;      // Tier 0-2: any fuel
        if (tier <= 6 && fuelValue >= 150) return true;     // Tier 3-6: mid+ fuel
        if (tier <= 10 && fuelValue >= 800) return true;    // Tier 7-10: coal+ fuel
        
        return fuelValue >= 1200; // Overheat: only best fuel
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        int currentTier = heatLevel / TIER_SIZE;
        
        if (mainHand.getType() == Material.AIR) {
            p.sendActionBar(Component.text("No fuel in hand!").color(NamedTextColor.RED));
            return;
        }
        
        if (!canUseFuelAtTier(mainHand.getType(), currentTier)) {
            p.sendActionBar(Component.text("Fuel too weak for this heat tier!").color(NamedTextColor.RED));
            return;
        }
        
        int fuelValue = getFuelValue(mainHand.getType());
        if (fuelValue == 0) {
            p.sendActionBar(Component.text("Not a fuel item!").color(NamedTextColor.RED));
            return;
        }
        
        // Consume fuel and add heat
        mainHand.setAmount(mainHand.getAmount() - 1);
        heatLevel = Math.min(MAX_OVERHEAT, heatLevel + fuelValue);
        
        ParticleUtil.spawnParticlesAroundPlayer(p, Particle.FLAME, 5);
        AbilityDisplay.showPrimaryAbility(p, "Fuel Consumed", NamedTextColor.GOLD);
        p.sendActionBar(Component.text("Heat: " + heatLevel + "/" + MAX_NORMAL_HEAT + "°C").color(NamedTextColor.GOLD));
        
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        
        if (heatLevel < 3000) {
            p.sendActionBar(Component.text("Need 3000°C! Have: " + heatLevel).color(NamedTextColor.RED));
            return;
        }
        
        // Consume heat
        heatLevel -= 3000;
        
        // Purge negative effects
        p.removePotionEffect(PotionEffectType.POISON);
        p.removePotionEffect(PotionEffectType.WITHER);
        p.removePotionEffect(PotionEffectType.SLOWNESS);
        p.removePotionEffect(PotionEffectType.WEAKNESS);
        p.removePotionEffect(PotionEffectType.NAUSEA);
        
        // Boost effects
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 100, 0, false, false));
        
        ParticleUtil.spawnParticlesAtEyes(p, Particle.LAVA, 20, 0.5, 0.5, 0.5);
        AbilityDisplay.showSecondaryAbility(p, "Thermal Purge", NamedTextColor.RED);
        p.sendActionBar(Component.text("Purged! Negative effects cleansed").color(NamedTextColor.GOLD));
        
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOn() {
        Player p = state.toBukkit();
        
        // Can only fly if on fire
        if (p.getFireTicks() <= 0) {
            p.sendActionBar(Component.text("Must be on fire to fly!").color(NamedTextColor.RED));
            return;
        }
        
        p.setAllowFlight(true);
        p.setFlying(true);
        AbilityDisplay.showCrouchAbility(p, "Inferno Flight", NamedTextColor.GOLD);
    }

    @Override
    public void crouchOff() {
        Player p = state.toBukkit();
        p.setAllowFlight(false);
        p.setFlying(false);
    }

    /**
     * Handle fire/lava damage - drain heat instead
     */
    @Override
    public void onDamage(EntityDamageEvent event) {
        Player p = state.toBukkit();
        
        // Cancel fire damage, drain heat instead
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
            event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
            event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            
            event.setCancelled(true);
            double heatDrain = heatLevel * DAMAGE_HEAT_LOSS_PERCENT;
            heatLevel = Math.max(0, (int) (heatLevel - heatDrain));
            return;
        }
        
        // Other damage drains heat based on percentage
        double heatDrain = heatLevel * DAMAGE_HEAT_LOSS_PERCENT;
        heatLevel = Math.max(0, (int) (heatLevel - heatDrain));
    }

    /**
     * Fuel consumption gives heat bonus
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        int fuelValue = getFuelValue(event.getItem().getType());
        if (fuelValue > 0) {
            heatLevel = Math.min(MAX_OVERHEAT, heatLevel + (fuelValue / 2));
        }
    }
}
