package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Vampire extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Vampire(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("Night Bite", "Gain life from damaging enemies at night.");
        this.secondaryAbilityDoc = new AbilityDoc("Blood Cloak", "Become harder to detect in darkness.");
        this.crouchAbilityDoc = new AbilityDoc("Nocturnal", "Passive: bonuses at night.");
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
        abilityMessage("Night Bite used.");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }

    @Override
    public void secondaryAbility() {
        abilityMessage("Blood Cloak toggled.");
    }

    @Override
    public void crouchOn() {
        abilityMessage("Nocturnal passive engaged.");
    }
}
