package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Breezeborn extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Breezeborn(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("Gale Dash", "Short burst of wind speed to dash forward.");
        this.secondaryAbilityDoc = new AbilityDoc("Updraft", "Create a small updraft to gain height.");
        this.crouchAbilityDoc = new AbilityDoc("Featherfall", "Reduce fall damage and descend slowly.");
    }

    private void abilityMessage(String msg) {
        Player p = state.toBukkit();
        p.sendActionBar(Component.text(msg));
    }

    @Override
    public void tick() {
        if (primaryCooldownCounter < primaryCooldown) primaryCooldownCounter += 1;
        if (secondaryCooldownCounter < secondaryCooldown) secondaryCooldownCounter += 1;
    }

    @Override
    public void primaryAbility() {
        abilityMessage("Gale Dash used.");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }

    @Override
    public void secondaryAbility() {
        abilityMessage("Updraft activated.");
    }

    @Override
    public void crouchOn() {
        abilityMessage("Featherfall engaged.");
    }
}
