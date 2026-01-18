package org.originsascendants.originAscendants.origins.chicken;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;

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
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) {
            p.sendActionBar(Component.text("Summon Chick on cooldown"));
            return;
        }
        
        // Spawn a chicken at player location
        org.bukkit.entity.Chicken chicken = p.getWorld().spawn(p.getLocation().add(0, 1, 0), org.bukkit.entity.Chicken.class);
        chicken.setAge(-1200); // Baby chicken
        p.sendActionBar(Component.text("Chick summoned!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) {
            p.sendActionBar(Component.text("Egg Toss on cooldown"));
            return;
        }
        
        org.bukkit.entity.Egg egg = p.getWorld().spawn(p.getEyeLocation(), org.bukkit.entity.Egg.class);
        egg.setVelocity(p.getLocation().getDirection().multiply(1.5));
        p.sendActionBar(Component.text("Egg tossed!"));
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
