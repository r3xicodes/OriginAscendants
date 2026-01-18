package org.originsascendants.originAscendants.origins.giant;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Giant extends Origin {

    private boolean enlarged = false;

    public Giant(PlayerState state) {
        super(state);
        this.primaryCooldown = 25;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Roar", "Stun nearby enemies and buff allies.");
        this.secondaryAbilityDoc = new AbilityDoc("Enlarge", "Grow larger and stronger.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 20);
        for (org.bukkit.entity.LivingEntity le : entities) {
            if (le != p && !(le instanceof Player)) {
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 80, 3, false, false));
            } else if (le instanceof Player && le != p) {
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 1, false, false));
            }
        }
        p.sendActionBar(Component.text("Roar!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        enlarged = !enlarged;
        if (enlarged) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2, false, false));
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            p.sendActionBar(Component.text("ENLARGED!"));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE);
            p.sendActionBar(Component.text("Shrunk"));
        }
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
} 
