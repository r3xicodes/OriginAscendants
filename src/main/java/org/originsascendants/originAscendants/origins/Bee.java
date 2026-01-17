package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Bee extends Origin {

    public Bee(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Pollinate", "Heal nearby creatures.");
        this.secondaryAbilityDoc = new AbilityDoc("Final Sting", "Sting a target.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        for (org.bukkit.entity.LivingEntity entity : p.getWorld().getEntitiesByClass(org.bukkit.entity.LivingEntity.class)) {
            if (p.getLocation().distance(entity.getLocation()) < 20 && entity != p) {
                entity.setHealth(Math.min(entity.getMaxHealth(), entity.getHealth() + 5));
            }
        }
        p.sendActionBar(Component.text("Pollinated!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        org.bukkit.util.RayTraceResult result = p.getWorld().rayTraceEntities(p.getEyeLocation(), p.getLocation().getDirection(), 30);
        if (result != null && result.getHitEntity() instanceof org.bukkit.entity.LivingEntity) {
            org.bukkit.entity.LivingEntity target = (org.bukkit.entity.LivingEntity) result.getHitEntity();
            target.damage(6);
            p.sendActionBar(Component.text("Stung!"));
            resetSecondaryCooldown();
        }
    }

    @Override
    public void crouchOff() {
        // Buzz away deactivated
    }

    @Override
    public void crouchOn() {
        // Buzz away activated
    }
}