package org.originsascendants.originAscendants.origins.werewolf;

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
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMaxHealth(p, 20.0);  // 10 hearts
        setMovementSpeed(p, 0.105); // 105% speed
        setAttackDamage(p, 1.05); // base 1.05
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
        AbilityDisplay.showPrimaryAbility(p, "Howl", NamedTextColor.DARK_GRAY);
        BossBarManager.showCooldownBar(p, "Howl", primaryCooldown, primaryCooldown);
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
            AbilityDisplay.showSecondaryAbility(p, "Transform", NamedTextColor.DARK_GRAY);
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.SPEED);
            AbilityDisplay.showSecondaryAbility(p, "Transform", NamedTextColor.DARK_GRAY);
        }
        BossBarManager.showCooldownBar(p, "Transform", secondaryCooldown, secondaryCooldown);
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

    @Override
    public void onConsume(org.bukkit.event.player.PlayerItemConsumeEvent event) {
        // Werewolves only eat meat
        Material item = event.getItem().getType();
        if (!isMeat(item)) {
            event.setCancelled(true);
            state.toBukkit().sendActionBar(Component.text("Werewolves only eat meat!", NamedTextColor.RED));
        }
    }

    private boolean isMeat(Material material) {
        return material == Material.COOKED_BEEF || material == Material.BEEF ||
               material == Material.COOKED_PORKCHOP || material == Material.PORKCHOP ||
               material == Material.COOKED_CHICKEN || material == Material.CHICKEN ||
               material == Material.COOKED_MUTTON || material == Material.MUTTON ||
               material == Material.COOKED_SALMON || material == Material.SALMON ||
               material == Material.COOKED_COD || material == Material.COD;
    }
}

