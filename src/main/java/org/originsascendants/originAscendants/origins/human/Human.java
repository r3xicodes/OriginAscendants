package org.originsascendants.originAscendants.origins.human;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;

public class Human extends Origin{
    public Human(PlayerState state) {
        super(state);

        // Set up GUI stuff
        AbilityDoc ability = new AbilityDoc(
                "None",
                "You're a human lol"
        );
        // I'm very aware that the below is a reference to ability, not a copy of it. Thanks, Java.
        this.primaryAbilityDoc = ability;
        this.secondaryAbilityDoc = ability;
        this.crouchAbilityDoc = ability;
    }

    private void abilityMessage() {
        Player p=state.toBukkit();
        AbilityDisplay.showPrimaryAbility(p, "No Ability", NamedTextColor.GRAY);
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMaxHealth(p, 20.0);  // 10 hearts
        setMovementSpeed(p, 0.100);  // 100% speed (normal)
        setAttackDamage(p, 1.0);  // 100% melee multiplier (normal)
    }

    @Override
    public void primaryAbility(){
        abilityMessage();
    };

    @Override
    public void secondaryAbility(){
        abilityMessage();
    };
}

