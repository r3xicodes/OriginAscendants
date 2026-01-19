package org.originsascendants.originAscendants.origins.breezeborn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Breezeborn extends Origin {

    private boolean isGliding = false;

    public Breezeborn(PlayerState state) {
        super(state);
        this.primaryCooldown = 25;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Breeze Ball", "Launch a wind projectile.");
        this.secondaryAbilityDoc = new AbilityDoc("Wind Launch", "Launch yourself upward.");
        this.crouchAbilityDoc = new AbilityDoc("Glide", "Hold crouch to glide on the wind.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMaxHealth(p, 18.0);  // 9 hearts
        setMovementSpeed(p, 0.120); // 120% speed
        setAttackDamage(p, 0.95); // 95% base attack damage
        
        // Passive: Light as Air - permanent Slow Falling
        org.bukkit.potion.PotionEffect slowFalling = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("SLOW_FALLING", slowFalling);
        p.addPotionEffect(slowFalling);
    }

    @Override
    public void removeAttributes() {
        super.removeAttributes();
        Player p = state.toBukkit();
        isGliding = false;
        p.setAllowFlight(false);
        p.setFlying(false);
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        org.bukkit.entity.Projectile projectile = p.getWorld().spawn(p.getEyeLocation(), org.bukkit.entity.Fireball.class, fb -> {
            fb.setVelocity(p.getLocation().getDirection().multiply(2.0));
            fb.setShooter(p);
            fb.setYield(0.1f);
        });
        AbilityDisplay.showPrimaryAbility(p, "Breeze Ball", NamedTextColor.AQUA);
        BossBarManager.showCooldownBar(p, "Breeze Ball", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.setVelocity(new org.bukkit.util.Vector(0, 2.0, 0));
        AbilityDisplay.showSecondaryAbility(p, "Wind Launch", NamedTextColor.AQUA);
        BossBarManager.showCooldownBar(p, "Wind Launch", secondaryCooldown, secondaryCooldown);
        resetSecondaryCooldown();
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        
        // Maintain gliding state
        if (isGliding) {
            p.setAllowFlight(true);
            if (!p.isFlying()) {
                p.setFlying(true);
            }
            
            // Slow descent while gliding
            if (p.getVelocity().getY() < -0.5) {
                org.bukkit.util.Vector vel = p.getVelocity();
                vel.setY(-0.3); // Terminal velocity while gliding
                p.setVelocity(vel);
            }
            
            // Apply wind resistance
            org.bukkit.util.Vector vel = p.getVelocity();
            vel.multiply(0.98); // Air drag
            p.setVelocity(vel);
        } else {
            p.setAllowFlight(false);
            p.setFlying(false);
        }
        
        updateCooldowns();
    }

    @Override
    public void crouchOff() {
        Player p = state.toBukkit();
        isGliding = false;
        p.setAllowFlight(false);
        p.setFlying(false);
        AbilityDisplay.showCrouchAbility(p, "Glide", NamedTextColor.GRAY);
        p.sendActionBar(Component.text("Glide deactivated", NamedTextColor.GRAY));
    }

    @Override
    public void crouchOn() {
        Player p = state.toBukkit();
        isGliding = true;
        p.setAllowFlight(true);
        AbilityDisplay.showCrouchAbility(p, "Glide", NamedTextColor.AQUA);
        p.sendActionBar(Component.text("Gliding on the breeze!", NamedTextColor.AQUA));
    }
}

