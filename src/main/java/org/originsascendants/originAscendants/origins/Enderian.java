package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Enderian extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Enderian(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("Blink", "Teleport directly where you are looking.");
        this.secondaryAbilityDoc = new AbilityDoc("Distortion Dodging", "Teleport when taking damage with a chance to dodge.");
        this.crouchAbilityDoc = new AbilityDoc("None", "No crouch ability.");
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
        abilityMessage("Blink attempted (not implemented).");
    }

    @Override
    public void secondaryAbility() {
        abilityMessage("Distortion dodge toggled (not implemented).");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }
}