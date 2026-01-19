package org.originsascendants.originAscendants.origins.shulk;

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
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMovementSpeed(p, 0.070); // 70% speed
        setAttackDamage(p, 1.0); // base attack damage
        // Mining efficiency: slight boost (105% base)
        org.bukkit.potion.PotionEffect haste = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HASTE, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("HASTE", haste);
        p.addPotionEffect(haste);
        // Bulwark passive: Resistance I
        org.bukkit.potion.PotionEffect resistance = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("RESISTANCE", resistance);
        p.addPotionEffect(resistance);
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.openInventory(p.getEnderChest());
        AbilityDisplay.showPrimaryAbility(p, "Ender Chest", NamedTextColor.DARK_PURPLE);
        BossBarManager.showCooldownBar(p, "Ender Chest", primaryCooldown, primaryCooldown);
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
        AbilityDisplay.showSecondaryAbility(p, "Levitation", NamedTextColor.DARK_PURPLE);
        BossBarManager.showCooldownBar(p, "Levitation", secondaryCooldown, secondaryCooldown);
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

