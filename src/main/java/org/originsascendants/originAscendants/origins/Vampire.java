package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
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
                p.sendActionBar(Component.text("Drained!"));
                resetPrimaryCooldown();
                hit = true;
                break;
            }
        }
        if (!hit) {
            p.sendActionBar(Component.text("No target"));
        }
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        long time = p.getWorld().getTime();
        if (time > 12500 && time < 23500) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 1, false, false));
            p.sendActionBar(Component.text("Night form active!"));
        } else {
            p.sendActionBar(Component.text("Daytime only!"));
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
