package org.originsascendants.originAscendants.origins.bee;

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
public class Bee extends Origin {

    private boolean isFlying = false;

    public Bee(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Pollinate", "Heal nearby creatures.");
        this.secondaryAbilityDoc = new AbilityDoc("Final Sting", "Sting a target.");
        this.crouchAbilityDoc = new AbilityDoc("Buzz Flight", "Hold crouch to fly.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 0.50);
        setMaxHealth(p, 14.0);  // 7 hearts
        setMovementSpeed(p, 0.100); // 100% speed
        setAttackDamage(p, 1.0); // base damage
    }

    @Override
    public void removeAttributes() {
        super.removeAttributes();
        Player p = state.toBukkit();
        isFlying = false;
        p.setAllowFlight(false);
        p.setFlying(false);
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        
        // Maintain flight state
        if (isFlying) {
            p.setAllowFlight(true);
            if (!p.isFlying()) {
                p.setFlying(true);
            }
            
            // Slow descent while flying
            if (p.getVelocity().getY() < -0.5) {
                org.bukkit.util.Vector vel = p.getVelocity();
                vel.setY(-0.3); // Terminal velocity while flying
                p.setVelocity(vel);
            }
            
            // Apply air resistance
            org.bukkit.util.Vector vel = p.getVelocity();
            vel.multiply(0.97); // Slight air drag
            p.setVelocity(vel);
        } else {
            p.setAllowFlight(false);
            p.setFlying(false);
        }
        
        updateCooldowns();
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        for (org.bukkit.entity.LivingEntity entity : p.getWorld().getEntitiesByClass(org.bukkit.entity.LivingEntity.class)) {
            if (p.getLocation().distance(entity.getLocation()) < 20 && entity != p) {
                entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + 5));
            }
        }
        AbilityDisplay.showPrimaryAbility(p, "Pollinate", NamedTextColor.GOLD);
        BossBarManager.showCooldownBar(p, "Pollinate", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        org.bukkit.util.RayTraceResult result = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getLocation().getDirection(), 30);
        if (result != null && result.getHitEntity() instanceof org.bukkit.entity.LivingEntity) {
            org.bukkit.entity.LivingEntity target = (org.bukkit.entity.LivingEntity) result.getHitEntity();
            AbilityDisplay.showSecondaryAbility(p, "Final Sting", NamedTextColor.GOLD);
            BossBarManager.showCooldownBar(p, "Final Sting", secondaryCooldown, secondaryCooldown);
            p.sendActionBar(Component.text("Stung!"));
            resetSecondaryCooldown();
        }
    }

    @Override
    public void crouchOff() {
        Player p = state.toBukkit();
        isFlying = false;
        p.setAllowFlight(false);
        p.setFlying(false);
        AbilityDisplay.showCrouchAbility(p, "Buzz Flight", NamedTextColor.GRAY);
        p.sendActionBar(Component.text("Buzz flight deactivated", NamedTextColor.GRAY));
    }

    @Override
    public void crouchOn() {
        Player p = state.toBukkit();
        isFlying = true;
        p.setAllowFlight(true);
        AbilityDisplay.showCrouchAbility(p, "Buzz Flight", NamedTextColor.GOLD);
        p.sendActionBar(Component.text("Buzzing into the air!", NamedTextColor.GOLD));
    }

    /**
     * Bees only eat honey!
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Material foodType = event.getItem().getType();
        Player p = state.toBukkit();
        
        // Bees only eat honey
        boolean isHoney = foodType == Material.HONEY_BOTTLE;
        
        if (!isHoney) {
            p.sendActionBar(Component.text("Bees only eat honey!").color(NamedTextColor.RED));
            event.setCancelled(true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BEE_HURT, 1.0f, 0.8f);
        } else {
            p.sendActionBar(Component.text("Sweet nectar!").color(NamedTextColor.GOLD));
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BEE_LOOP, 0.5f, 1.0f);
        }
    }
}
