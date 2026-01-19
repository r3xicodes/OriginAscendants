package org.originsascendants.originAscendants.origins.phytokin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerItemConsumeEvent;

@SuppressWarnings("unused")
public class Phytokin extends Origin {

    private boolean barkskinActive = false;

    public Phytokin(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 25;
        this.primaryAbilityDoc = new AbilityDoc("Verdant Grasp", "Slow nearby enemies with vines.");
        this.secondaryAbilityDoc = new AbilityDoc("Barkskin", "Toggle resistance shield.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 1.50);
        setMaxHealth(p, 26.0);  // 13 hearts
        setMovementSpeed(p, 0.090); // 90% speed
        setAttackDamage(p, 1.0); // base attack damage
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 15);
        for (LivingEntity le : entities) {
            if (le != p && !(le instanceof Player)) {
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 2, false, false));
            }
        }
        AbilityDisplay.showPrimaryAbility(p, "Verdant Grasp", NamedTextColor.GREEN);
        BossBarManager.showCooldownBar(p, "Verdant Grasp", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        
        // Regenerate from rain and sunlight
        if (p.getWorld().hasStorm() && p.getWorld().getTime() % 10 == 0) {
            // Rain regeneration
            if (p.getLocation().getBlock().getType() == org.bukkit.Material.AIR && 
                p.getWorld().hasStorm()) {
                p.setFoodLevel(Math.min(20, p.getFoodLevel() + 1));
                p.setSaturation(Math.min(20, p.getSaturation() + 0.5f));
            }
        }
        
        // Sunlight regeneration
        if (p.getWorld().getTime() % 20 == 0 && p.getWorld().getTime() > 0 && p.getWorld().getTime() < 12000) {
            if (p.getLocation().getBlock().getType() == org.bukkit.Material.AIR) {
                p.setFoodLevel(Math.min(20, p.getFoodLevel() + 1));
                p.setSaturation(Math.min(20, p.getSaturation() + 0.5f));
            }
        }
        
        if (barkskinActive) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 100, 1, false, false));
            if (p.getWorld().getTime() % 20 == 0 && p.getHealth() < p.getMaxHealth()) {
                p.setHealth(Math.min(p.getHealth() + 0.5, p.getMaxHealth()));
            }
        }
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        barkskinActive = !barkskinActive;
        AbilityDisplay.showSecondaryAbility(p, "Barkskin", NamedTextColor.GREEN);
        BossBarManager.showCooldownBar(p, "Barkskin", secondaryCooldown, secondaryCooldown);
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }

    /**
     * Phytokins don't eat food - they regenerate from rain and sunlight!
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Player p = state.toBukkit();
        p.sendActionBar(Component.text("Phytokins don't eat! They regenerate from nature.").color(NamedTextColor.GREEN));
        event.setCancelled(true);
    }
}

