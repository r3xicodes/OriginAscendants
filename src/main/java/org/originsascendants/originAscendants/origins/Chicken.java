package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Chicken extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Chicken(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("Lucky Eggs", "Spawn a random mob (may be dangerous).");
        this.secondaryAbilityDoc = new AbilityDoc("None", "No secondary ability.");
        this.crouchAbilityDoc = new AbilityDoc("Flutter", "Descend slower by fluttering your wings.");
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
        abilityMessage("Lucky Eggs used. (spawn not implemented)");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }

    @Override
    public void crouchOn() {
        abilityMessage("Flutter active.");
    }
}