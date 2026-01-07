package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.player.PlayerRegistry;

public class Human extends Origin{
    protected Human(PlayerState state) {
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
        p.sendActionBar(Component.text("You have no ability lol"));
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
