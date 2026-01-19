package org.originsascendants.originAscendants.origins.arachnid;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Arachnid extends Origin {

    private boolean climbing = false;

    public Arachnid(PlayerState state) {
        super(state);
        this.primaryCooldown = 10;
        this.secondaryCooldown = 5;
        this.primaryAbilityDoc = new AbilityDoc("Climb", "Toggle climbing.");
        this.secondaryAbilityDoc = new AbilityDoc("Web Sling", "Launch upward.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        // Arachnids: 95% speed, 125% jump, normal size, tough chitin (fire resistance)
        setMovementSpeed(p, 0.095);  // 95% speed
        setFallDamageMultiplier(p, 0.80);  // 80% fall damage
        setMaxHealth(p, 18.0);  // 9 hearts
        setAttackDamage(p, 1.0);  // base damage
        setAttackSpeed(p, 3.6);  // 90% of normal (4.0)
        // Tough Chitin passive: resistance to fire (Resistance II while in fire handled via onDamage)
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        climbing = !climbing;
        AbilityDisplay.showPrimaryAbility(p, "Climb", NamedTextColor.DARK_AQUA);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.setVelocity(p.getVelocity().add(new org.bukkit.util.Vector(0, 2, 0)));
        AbilityDisplay.showSecondaryAbility(p, "Web Sling", NamedTextColor.DARK_AQUA);
        BossBarManager.showCooldownBar(p, "Web Sling", secondaryCooldown, secondaryCooldown);
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    /**
     * Arachnids are carnivorous - only eat meat!
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Material foodType = event.getItem().getType();
        Player p = state.toBukkit();
        
        // List of meat items
        boolean isMeat = foodType == Material.BEEF || foodType == Material.COOKED_BEEF ||
                        foodType == Material.PORKCHOP || foodType == Material.COOKED_PORKCHOP ||
                        foodType == Material.CHICKEN || foodType == Material.COOKED_CHICKEN ||
                        foodType == Material.MUTTON || foodType == Material.COOKED_MUTTON ||
                        foodType == Material.RABBIT || foodType == Material.COOKED_RABBIT ||
                        foodType == Material.COD || foodType == Material.COOKED_COD ||
                        foodType == Material.SALMON || foodType == Material.COOKED_SALMON;
        
        if (!isMeat) {
            p.sendActionBar(Component.text("Yuck! Arachnids only eat meat!").color(NamedTextColor.RED));
            event.setCancelled(true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SPIDER_HURT, 1.0f, 0.8f);
        }
    }
}
