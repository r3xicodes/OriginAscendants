package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;

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
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 15);
        for (LivingEntity le : entities) {
            if (le != p && !(le instanceof Player)) {
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 100, 2, false, false));
            }
        }
        p.sendActionBar(Component.text("Verdant grasp!"));
        resetPrimaryCooldown();
    }

    @Override
    public void tick() {
        if (barkskinActive) {
            Player p = state.toBukkit();
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
        p.sendActionBar(Component.text(barkskinActive ? "Barkskin ON" : "Barkskin OFF"));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }
}
