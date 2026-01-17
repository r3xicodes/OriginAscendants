package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Sculkborn extends Origin {

    private boolean echolocating = false;

    public Sculkborn(PlayerState state) {
        super(state);
        this.primaryCooldown = 30;
        this.secondaryCooldown = 25;
        this.primaryAbilityDoc = new AbilityDoc("Shriek", "Emit a sonic boom with darkness effect.");
        this.secondaryAbilityDoc = new AbilityDoc("Echolocation", "Toggle entity highlighting.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 20);
        for (LivingEntity le : entities) {
            if (le != p) {
                le.damage(5.0);
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.DARKNESS, 60, 0, false, false));
            }
        }
        p.sendActionBar(Component.text("Shriek!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        echolocating = !echolocating;
        if (echolocating) {
            var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 40);
            for (LivingEntity le : entities) {
                if (le != p) {
                    le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.GLOWING, 100, 0, false, false));
                }
            }
            p.sendActionBar(Component.text("Echolocation ON"));
        } else {
            p.sendActionBar(Component.text("Echolocation OFF"));
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
