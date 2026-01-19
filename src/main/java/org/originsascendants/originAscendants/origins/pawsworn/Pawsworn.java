package org.originsascendants.originAscendants.origins.pawsworn;

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
public class Pawsworn extends Origin {

    private boolean dashing = false;

    public Pawsworn(PlayerState state) {
        super(state);
        this.primaryCooldown = 15;
        this.secondaryCooldown = 20;
        this.primaryAbilityDoc = new AbilityDoc("Pounce", "Leap forward with damage boost.");
        this.secondaryAbilityDoc = new AbilityDoc("Dash", "Toggle quick dashing.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 0.65);
        setMaxHealth(p, 18.0);  // 9 hearts
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.setVelocity(p.getLocation().getDirection().multiply(1.8).add(new org.bukkit.util.Vector(0, 0.5, 0)));
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 1, false, false));
        AbilityDisplay.showPrimaryAbility(p, "Pounce", NamedTextColor.GOLD);
        BossBarManager.showCooldownBar(p, "Pounce", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        dashing = !dashing;
        if (dashing) {
            AbilityDisplay.showSecondaryAbility(p, "Dash", NamedTextColor.GOLD);
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 200, 1, false, false));
            p.sendActionBar(Component.text("Dash ON"));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
            p.sendActionBar(Component.text("Dash OFF"));
        }
        BossBarManager.showCooldownBar(p, "Dash", secondaryCooldown, secondaryCooldown);
        resetSecondaryCooldown();
    }

    @Override
    public void tick() {
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }

    /**
     * Pawsworn are carnivores - meat only!
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Material foodType = event.getItem().getType();
        Player p = state.toBukkit();
        
        // Meat only
        boolean isMeat = foodType == Material.BEEF || foodType == Material.COOKED_BEEF ||
                        foodType == Material.PORKCHOP || foodType == Material.COOKED_PORKCHOP ||
                        foodType == Material.CHICKEN || foodType == Material.COOKED_CHICKEN ||
                        foodType == Material.MUTTON || foodType == Material.COOKED_MUTTON ||
                        foodType == Material.RABBIT || foodType == Material.COOKED_RABBIT ||
                        foodType == Material.COD || foodType == Material.COOKED_COD ||
                        foodType == Material.SALMON || foodType == Material.COOKED_SALMON;
        
        if (!isMeat) {
            p.sendActionBar(Component.text("Pawsworn only eat meat!").color(NamedTextColor.RED));
            event.setCancelled(true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_HURT, 1.0f, 0.8f);
        }
    }
}

