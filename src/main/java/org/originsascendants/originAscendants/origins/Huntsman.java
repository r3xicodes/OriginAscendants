package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Huntsman extends Origin {

    private boolean toggleState = false;
    private double charge = 0.00;
    private boolean isCharging = false;
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;

    public Huntsman(PlayerState state) {
        super(state);
        this.primaryAbilityDoc = new AbilityDoc("Precision", "Turn invisible and shoot a lethal arrow.");
        this.secondaryAbilityDoc = new AbilityDoc("Change Tip", "Change arrow tip behavior (buffing/stun/etc).");
        this.crouchAbilityDoc = new AbilityDoc("Stalk", "Crouching grants invisibility and safe fall.");
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
        abilityMessage("Precision activated (invisibility+shot).");
    }

    @Override
    public void secondaryAbility() {
        abilityMessage("Tip changed.");
    }

    @Override
    public void crouchOn() {
        abilityMessage("Stalk active.");
    }

    @Override
    public void crouchOff() {
        isCharging = false;
    }
} 