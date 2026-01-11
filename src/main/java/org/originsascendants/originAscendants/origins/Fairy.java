package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Fairy extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Fairy(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("Energize", "Energize nearby players and grant minor buffs.");
        this.secondaryAbilityDoc = new AbilityDoc("None", "No secondary ability.");
        this.crouchAbilityDoc = new AbilityDoc("Small Wings", "Can fly with creative-like flight (passive).");
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
        abilityMessage("Energize activated.");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }
} 