package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Arachnid extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Arachnid(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("None", "No primary ability.");
        this.secondaryAbilityDoc = new AbilityDoc("Toggle Climb", "Toggle climbing on walls.");
        this.crouchAbilityDoc = new AbilityDoc("Predator", "Passives: you only eat meat; nocturnal bonuses.");
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
    public void secondaryAbility() {
        abilityMessage("Toggled climbing (not implemented beyond message). If you want wall-climb, implement in the GUI or movement listeners.");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }
}