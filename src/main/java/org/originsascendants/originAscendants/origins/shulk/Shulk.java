package org.originsascendants.originAscendants.origins.shulk;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;

@SuppressWarnings("unused")
public class Shulk extends Origin {

    public Shulk(PlayerState state) {
        super(state);
        this.primaryCooldown = 40;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Ender Chest", "Open your ender chest.");
        this.secondaryAbilityDoc = new AbilityDoc("Levitation", "Launch nearby mobs upward.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.openInventory(p.getEnderChest());
        p.sendActionBar(Component.text("Ender Chest opened!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 20);
        for (LivingEntity le : entities) {
            if (le != p && !(le instanceof Player)) {
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.LEVITATION, 60, 2, false, false));
            }
        }
        p.sendActionBar(Component.text("Levitation cast!"));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void tick() {
    }

    @Override
    public void crouchOn() {
    }
}

