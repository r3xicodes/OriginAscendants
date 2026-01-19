package org.originsascendants.originAscendants.origins.vampire;

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
public class Vampire extends Origin {

    public Vampire(PlayerState state) {
        super(state);
        this.primaryCooldown = 30;
        this.secondaryCooldown = 20;
        this.primaryAbilityDoc = new AbilityDoc("Drain", "Steal health from target.");
        this.secondaryAbilityDoc = new AbilityDoc("Night Form", "Toggle strength at night.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        // Vampires are agile nocturnal hunters with enhanced attack capabilities
        setMovementSpeed(p, 0.12);
        setAttackDamage(p, 1.75);
        setAttackSpeed(p, 5.0);
        setFallDamageMultiplier(p, 0.75);
        setMaxHealth(p, 22.0);  // 11 hearts
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        var targets = p.getWorld().getNearbyLivingEntities(p.getLocation(), 40);
        boolean hit = false;
        for (LivingEntity target : targets) {
            if (!(target instanceof Player) && target != p) {
                double damage = 4.0;
                target.damage(damage);
                p.setHealth(Math.min(p.getHealth() + damage, p.getMaxHealth()));
                AbilityDisplay.showPrimaryAbility(p, "Drain", NamedTextColor.RED);
                p.sendActionBar(Component.text("Drained! ", NamedTextColor.RED).append(Component.text(String.format("%.1f", p.getHealth()) + " HP", NamedTextColor.YELLOW)));
                resetPrimaryCooldown();
                hit = true;
                break;
            }
        }
        if (!hit) {
            p.sendActionBar(Component.text("No target in range", NamedTextColor.RED));
        }
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        long time = p.getWorld().getTime();
        if (time > 12500 && time < 23500) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 1, false, false));
            AbilityDisplay.showSecondaryAbility(p, "Night Form", NamedTextColor.DARK_PURPLE);
            p.sendActionBar(Component.text("Night form activated!", NamedTextColor.DARK_PURPLE));
        } else {
            p.sendActionBar(Component.text("Only works at night!", NamedTextColor.YELLOW));
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

