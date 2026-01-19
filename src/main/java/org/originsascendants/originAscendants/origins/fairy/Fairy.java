package org.originsascendants.originAscendants.origins.fairy;

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
public class Fairy extends Origin {

    private boolean flying = false;

    public Fairy(PlayerState state) {
        super(state);
        this.primaryCooldown = 10;
        this.secondaryCooldown = 20;
        this.primaryAbilityDoc = new AbilityDoc("Energize", "Buff nearby players.");
        this.secondaryAbilityDoc = new AbilityDoc("Flight", "Toggle flight.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 0.30);
        setMaxHealth(p, 8.0);  // 4 hearts
        setMovementSpeed(p, 0.090); // 90% speed
        setAttackDamage(p, 1.0); // normal damage
        // Mining efficiency: small boost (110% base)
        org.bukkit.potion.PotionEffect haste = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HASTE, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("HASTE", haste);
        p.addPotionEffect(haste);
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        for (Player nearby : p.getWorld().getPlayers()) {
            if (p.getLocation().distance(nearby.getLocation()) < 20) {
                nearby.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 100, 0));
            }
        }
        AbilityDisplay.showPrimaryAbility(p, "Energize", NamedTextColor.LIGHT_PURPLE);
        BossBarManager.showCooldownBar(p, "Energize", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        flying = !flying;
        p.setAllowFlight(flying);
        AbilityDisplay.showSecondaryAbility(p, "Flight", NamedTextColor.LIGHT_PURPLE);
        BossBarManager.showCooldownBar(p, "Flight", secondaryCooldown, secondaryCooldown);
        p.sendActionBar(Component.text("Flight: " + (flying ? "ON" : "OFF")));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
        Player p = state.toBukkit();
        flying = !flying;
        p.setAllowFlight(flying);
        if (flying) {
            AbilityDisplay.showCrouchAbility(p, "Creative Flight", NamedTextColor.LIGHT_PURPLE);
            p.sendActionBar(Component.text("Flight: ON").color(NamedTextColor.LIGHT_PURPLE));
        } else {
            p.sendActionBar(Component.text("Flight: OFF").color(NamedTextColor.GRAY));
        }
    }

    /**
     * Fairies only eat sweets: cake, pie, honey, berries, cookies, golden carrots, golden apples
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Material foodType = event.getItem().getType();
        Player p = state.toBukkit();
        
        // Sweet foods only
        boolean isSweet = foodType == Material.CAKE ||
                         foodType == Material.PUMPKIN_PIE ||
                         foodType == Material.HONEY_BOTTLE ||
                         foodType == Material.SWEET_BERRIES ||
                         foodType == Material.GLOW_BERRIES ||
                         foodType == Material.COOKIE ||
                         foodType == Material.GOLDEN_CARROT ||
                         foodType == Material.GOLDEN_APPLE;
        
        if (!isSweet) {
            p.sendActionBar(Component.text("Fairies only eat sweets!").color(NamedTextColor.RED));
            event.setCancelled(true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_HURT, 1.0f, 0.8f);
        } else {
            p.sendActionBar(Component.text("Sweet treat!").color(NamedTextColor.LIGHT_PURPLE));
            p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5f, 1.0f);
        }
    }
} 
