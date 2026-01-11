package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Blazeborn extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Blazeborn(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("Fuel Consumption", "Consume fuel items to increase your Heat Bar (toggle).");
        this.secondaryAbilityDoc = new AbilityDoc("Thermal Surge", "Instantly boost heat and purge negative effects.");
        this.crouchAbilityDoc = new AbilityDoc("Heat Core", "Passive heat core mechanics (see docs).");
    }

    private void abilityMessage(String msg) {
        Player p = state.toBukkit();
        p.sendActionBar(Component.text(msg));
    }

    @Override
    public void tick() {
        // Use fields to avoid unused warnings until abilities are implemented
        if (toggleState || isCharging || charge > 0) { /* no-op */ }
        if (primaryCooldownCounter < primaryCooldown) primaryCooldownCounter += 1;
        if (secondaryCooldownCounter < secondaryCooldown) secondaryCooldownCounter += 1;
    }

    @Override
    public void primaryAbility() {
        abilityMessage("Fuel Consumption toggled.");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }

    @Override
    public void secondaryAbility() {
        abilityMessage("Thermal Surge activated.");
    }
}