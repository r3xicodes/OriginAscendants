package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Alchemist extends Origin {

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
        var nearby = p.getWorld().getNearbyPlayers(p.getLocation(), 20);
        for (Player player : nearby) {
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SPEED, 200, 1, false, false));
            player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 200, 0, false, false));
        }
        p.sendActionBar(Component.text("Brew activated!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        var nearby = p.getWorld().getNearbyLivingEntities(p.getLocation(), 20, entity -> entity instanceof Player || entity instanceof org.bukkit.entity.LivingEntity);
        for (org.bukkit.entity.LivingEntity le : nearby) {
            le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.REGENERATION, 100, 1, false, false));
        }
        p.sendActionBar(Component.text("Catalyst cast!"));
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