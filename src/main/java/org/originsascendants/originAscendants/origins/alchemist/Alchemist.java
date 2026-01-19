package org.originsascendants.originAscendants.origins.alchemist;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.entity.Player;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@SuppressWarnings("unused")
public class Alchemist extends Origin {
    private int particleTickCounter = 0;

    public Alchemist(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Brew Buff", "Grant speed and strength to nearby players.");
        this.secondaryAbilityDoc = new AbilityDoc("Catalyst", "Grant regeneration to self and nearby allies.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        
        // Burst explosion of magic particles around caster
        spawnMagicBurstParticles(p.getLocation(), 30);
        
        var nearby = p.getWorld().getNearbyPlayers(p.getLocation(), 20);
        for (Player player : nearby) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 200, 1, false, false));
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 200, 0, false, false));
            
            // Particle trail to affected players
            spawnMagicTrailParticles(p.getLocation(), player.getLocation());
        }
        AbilityDisplay.showPrimaryAbility(p, "Brew Buff", NamedTextColor.GOLD);
        BossBarManager.showCooldownBar(p, "Brew Buff", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        
        // Radiant magic pulse
        spawnMagicPulseParticles(p.getLocation(), 25);
        
        var nearby = p.getWorld().getNearbyLivingEntities(p.getLocation(), 20, entity -> entity instanceof Player || entity instanceof org.bukkit.entity.LivingEntity);
        for (org.bukkit.entity.LivingEntity le : nearby) {
            le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION, 100, 1, false, false));
        }
        AbilityDisplay.showSecondaryAbility(p, "Catalyst", NamedTextColor.GOLD);
        BossBarManager.showCooldownBar(p, "Catalyst", secondaryCooldown, secondaryCooldown);
        resetSecondaryCooldown();
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMaxHealth(p, 18.0);  // 9 hearts (18.0 health)
        setMovementSpeed(p, 0.105);  // 105% speed
        setAttackDamage(p, 0.7);  // 70% melee multiplier
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        particleTickCounter++;
        
        // Ambient magical aura around player every 5 ticks
        if (particleTickCounter % 5 == 0) {
            spawnAmbientMagicAura(p.getLocation());
        }
    }

    /**
     * Creates a burst of magic particles radiating outward from center
     */
    private void spawnMagicBurstParticles(Location center, int particleCount) {
        for (int i = 0; i < particleCount; i++) {
            // Random direction
            double angle = Math.random() * Math.PI * 2;
            double elevation = Math.random() * Math.PI - Math.PI / 2;
            
            double vx = Math.cos(elevation) * Math.cos(angle) * 0.5;
            double vy = Math.sin(elevation) * 0.5;
            double vz = Math.cos(elevation) * Math.sin(angle) * 0.5;
            
            Location particleLocation = center.clone().add(0, 0.5, 0);
            center.getWorld().spawnParticle(Particle.EFFECT, particleLocation, 0, vx, vy, vz, 0.3);
            center.getWorld().spawnParticle(Particle.ENCHANT, particleLocation, 0, vx * 0.5, vy * 0.5, vz * 0.5, 0.2);
        }
    }

    /**
     * Creates a circular pulse of magic particles expanding outward
     */
    private void spawnMagicPulseParticles(Location center, int particleCount) {
        double radius = 2.0;
        for (int i = 0; i < particleCount; i++) {
            double angle = (i / (double) particleCount) * Math.PI * 2;
            double x = center.getX() + Math.cos(angle) * radius;
            double z = center.getZ() + Math.sin(angle) * radius;
            
            Location particleLocation = new Location(center.getWorld(), x, center.getY() + 0.5, z);
            center.getWorld().spawnParticle(Particle.GLOW, particleLocation, 0, 0, 0.1, 0, 0.1);
            center.getWorld().spawnParticle(Particle.EFFECT, particleLocation, 0, 0, 0.2, 0, 0.2);
        }
    }

    /**
     * Creates a magic trail from source to destination
     */
    private void spawnMagicTrailParticles(Location from, Location to) {
        Vector direction = to.clone().subtract(from).toVector().normalize();
        double distance = from.distance(to);
        
        for (double d = 0; d < distance; d += 0.3) {
            Location particleLocation = from.clone().add(direction.clone().multiply(d));
            from.getWorld().spawnParticle(Particle.ENCHANT, particleLocation, 1, 0.1, 0.1, 0.1, 0.1);
            from.getWorld().spawnParticle(Particle.GLOW, particleLocation, 0, 0, 0, 0, 0.05);
        }
    }

    /**
     * Creates ambient magical aura around player
     */
    private void spawnAmbientMagicAura(Location playerLocation) {
        double radius = 1.5;
        for (int i = 0; i < 8; i++) {
            double angle = (i / 8.0) * Math.PI * 2 + (System.currentTimeMillis() % 2000) / 2000.0 * Math.PI * 2;
            double x = playerLocation.getX() + Math.cos(angle) * radius;
            double z = playerLocation.getZ() + Math.sin(angle) * radius;
            double y = playerLocation.getY() + 0.5 + Math.sin(System.currentTimeMillis() / 500.0 + i) * 0.3;
            
            Location particleLocation = new Location(playerLocation.getWorld(), x, y, z);
            playerLocation.getWorld().spawnParticle(Particle.GLOW, particleLocation, 0, 0, 0, 0, 0.08);
        }
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }
}
