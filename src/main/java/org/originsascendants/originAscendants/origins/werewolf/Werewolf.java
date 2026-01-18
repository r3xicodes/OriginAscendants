package org.originsascendants.originAscendants.origins.werewolf;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Werewolf extends Origin {

    private boolean wolfForm = false;

    public Werewolf(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Howl", "Buff nearby wolves and allies.");
        this.secondaryAbilityDoc = new AbilityDoc("Transform", "Toggle wolf form for stat changes.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        var wolves = p.getWorld().getNearbyLivingEntities(p.getLocation(), 30, entity -> entity instanceof org.bukkit.entity.Wolf);
        for (org.bukkit.entity.LivingEntity le : wolves) {
            le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 200, 1, false, false));
            le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 200, 1, false, false));
        }
        p.sendActionBar(Component.text("Howl! Wolves buffed."));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        wolfForm = !wolfForm;
        if (wolfForm) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, false, false));
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
            p.sendActionBar(Component.text("Wolf Form ON"));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
            p.sendActionBar(Component.text("Wolf Form OFF"));
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

