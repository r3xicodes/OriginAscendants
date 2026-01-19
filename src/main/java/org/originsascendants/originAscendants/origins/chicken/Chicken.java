package org.originsascendants.originAscendants.origins.chicken;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;

@SuppressWarnings("unused")
public class Chicken extends Origin {

    public Chicken(PlayerState state) {
        super(state);
        this.primaryCooldown = 15;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Summon Chick", "Summon a baby chicken to fight for you.");
        this.secondaryAbilityDoc = new AbilityDoc("Egg Toss", "Throw eggs at your enemies.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 0.50);
        setMaxHealth(p, 18.0);  // 9 hearts
        setMovementSpeed(p, 0.150);  // 150% speed
        setAttackDamage(p, 0.9);  // 90% melee multiplier
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) {
            return;
        }
        
        // Spawn a chicken at player location
        org.bukkit.entity.Chicken chicken = p.getWorld().spawn(p.getLocation().add(0, 1, 0), org.bukkit.entity.Chicken.class);
        chicken.setAge(-1200); // Baby chicken
        AbilityDisplay.showPrimaryAbility(p, "Lucky Eggs", NamedTextColor.YELLOW);
        BossBarManager.showCooldownBar(p, "Lucky Eggs", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) {
            return;
        }
        
        org.bukkit.entity.Egg egg = p.getWorld().spawn(p.getEyeLocation(), org.bukkit.entity.Egg.class);
        egg.setVelocity(p.getLocation().getDirection().multiply(1.5));
        AbilityDisplay.showSecondaryAbility(p, "Egg Toss", NamedTextColor.YELLOW);
        BossBarManager.showCooldownBar(p, "Egg Toss", secondaryCooldown, secondaryCooldown);
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
     * Chickens are herbivores - can only eat seeds!
     */
    @Override
    public void onConsume(PlayerItemConsumeEvent event) {
        Material foodType = event.getItem().getType();
        Player p = state.toBukkit();
        
        // Seeds only for chickens
        boolean isSeeds = foodType == Material.WHEAT_SEEDS || 
                         foodType == Material.PUMPKIN_SEEDS ||
                         foodType == Material.MELON_SEEDS ||
                         foodType == Material.BEETROOT_SEEDS;
        
        if (!isSeeds) {
            p.sendActionBar(Component.text("Chickens only eat seeds!").color(NamedTextColor.RED));
            event.setCancelled(true);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CHICKEN_HURT, 1.0f, 0.8f);
        }
    }
}
