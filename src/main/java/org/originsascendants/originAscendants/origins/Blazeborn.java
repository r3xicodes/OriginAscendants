package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.ParticleUtil;
import org.originsascendants.originAscendants.util.PotionUtil;
import org.originsascendants.originAscendants.util.EntityUtil;

/**
 * Blazeborn Origin - Fire-based warrior with heat mechanics
 * 
 * Passive: Fire resistance and heat generation from combat
 * Primary: Fuel consumption (toggle) - consume fuel items to build heat
 * Secondary: Thermal surge - expel heat for speed boost and knockback nearby enemies
 * Crouch: Heat core - passive heat gain
 */
@SuppressWarnings("unused")
public class Blazeborn extends Origin {

    private static final int MAX_HEAT = 15000;
    private static final int HEAT_DECAY_RATE = 5; // per tick
    private static final int HEAT_DECAY_INTERVAL = 20; // ticks
    
    private int heatLevel = 0;
    private int heatDecayCounter = 0;
    private boolean fuelToggle = false;

    public Blazeborn(PlayerState state) {
        super(state);

        this.primaryCooldown = 10;
        this.secondaryCooldown = 30;
        this.crouchCooldown = 5;
        this.primaryAbilityDoc = new AbilityDoc("Fuel Consumption", "Consume fuel items to increase your heat (toggle). Right-click to toggle.");
        this.secondaryAbilityDoc = new AbilityDoc("Thermal Surge", "Instantly boost heat and knockback nearby enemies. 1.5s cooldown.");
        this.crouchAbilityDoc = new AbilityDoc("Heat Core", "Passive heat generation while crouching.");
    }

    @Override
    public String getDisplayName() {
        return "§cBlaze§fborn";
    }

    @Override
    public String getDescription() {
        return "Master of fire with heat-based mechanics";
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        // Fire immunity is passive via tick
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
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
        
        // Heat decay
        heatDecayCounter++;
        if (heatDecayCounter >= HEAT_DECAY_INTERVAL) {
            heatLevel = Math.max(0, heatLevel - HEAT_DECAY_RATE);
            heatDecayCounter = 0;
        }
        
        // Passive heat generation from crouch
        if (p.isSneaking() && isCrouchReady()) {
            heatLevel = Math.min(MAX_HEAT, heatLevel + 100);
            resetCrouchCooldown();
        }
        
        // Heat-based effects
        if (heatLevel > 0) {
            // Particle effects based on heat level
            if (heatLevel > 5000) {
                ParticleUtil.spawnParticlesAroundPlayer(p, Particle.FLAME, 3);
            }
            if (heatLevel > 10000) {
                ParticleUtil.spawnParticlesAtEyes(p, Particle.LAVA, 1, 0.2, 0.2, 0.2);
            }
        }
        
        updateCooldowns();
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        
        // Toggle fuel consumption mode
        fuelToggle = !fuelToggle;
        
        if (fuelToggle) {
            p.sendMessage(Component.text("Fuel consumption ON", NamedTextColor.RED));
            
            // Try to consume fuel items
            ItemStack mainHand = p.getInventory().getItemInMainHand();
            if (isFuelItem(mainHand)) {
                heatLevel = Math.min(MAX_HEAT, heatLevel + 1000);
                mainHand.setAmount(mainHand.getAmount() - 1);
                ParticleUtil.spawnParticlesAroundPlayer(p, Particle.FLAME, 5);
            }
        } else {
            p.sendMessage(Component.text("Fuel consumption OFF", NamedTextColor.GRAY));
        }
        
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady() || heatLevel < 2000) return;
        
        int heatUsed = Math.min(heatLevel, 5000);
        heatLevel -= heatUsed;
        
        // Thermal surge effects
        PotionUtil.applyPotion(p, PotionEffectType.SPEED, 100, 1);
        ParticleUtil.spawnParticlesAtEyes(p, Particle.FLAME, 15, 0.5, 0.5, 0.5);
        
        // Knockback nearby enemies
        for (org.bukkit.entity.LivingEntity entity : EntityUtil.getNearbyEntities(p, 10, org.bukkit.entity.LivingEntity.class)) {
            if (entity != p && !(entity instanceof Player)) {
                org.bukkit.util.Vector direction = entity.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                EntityUtil.knockbackAway(entity, direction, 1.5);
                EntityUtil.damageWithKnockback(entity, 5.0, direction);
            }
        }
        
        p.sendMessage(Component.text("Thermal Surge! Heat: " + heatLevel, NamedTextColor.YELLOW));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOn() {
        Player p = state.toBukkit();
        p.sendMessage(Component.text("Heat generation started", NamedTextColor.GOLD));
    }

    @Override
    public void crouchOff() {
        Player p = state.toBukkit();
        p.sendMessage(Component.text("Heat generation stopped. Heat: " + heatLevel, NamedTextColor.GOLD));
    }

    /**
     * Check if an ItemStack is a valid fuel item
     */
    private boolean isFuelItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        return item.getType() == Material.COAL || 
               item.getType() == Material.CHARCOAL || 
               item.getType() == Material.BLAZE_ROD;
    }
}