package org.originsascendants.originAscendants.origins.arachnid;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Arachnid extends Origin {

    private boolean climbing = false;

    public Arachnid(PlayerState state) {
        super(state);
        this.primaryCooldown = 10;
        this.secondaryCooldown = 5;
        this.primaryAbilityDoc = new AbilityDoc("Climb", "Toggle climbing.");
        this.secondaryAbilityDoc = new AbilityDoc("Web Sling", "Launch upward.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        climbing = !climbing;
        p.sendActionBar(Component.text("Climb: " + (climbing ? "ON" : "OFF")));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.setVelocity(p.getVelocity().add(new org.bukkit.util.Vector(0, 2, 0)));
        p.sendActionBar(Component.text("Web slung!"));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }
}
