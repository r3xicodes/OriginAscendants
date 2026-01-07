package org.originsascendants.originAscendants.origins;

import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;

public abstract class Origin {
    protected final PlayerState state;

    // These are used in the selection GUI to hold information about the origin
    public AbilityDoc primaryAbilityDoc;
    public AbilityDoc secondaryAbilityDoc;
    public AbilityDoc crouchAbilityDoc;

    /* Once I figure out all of the stats that each origin should have,
       I'll put uninitialized variables and whatnot here. */

    protected Origin(PlayerState state) {
        this.state = state;
    }

    //tick logic (aka passive logic)
    public void tick(){};

    // Handled automatically
    public void primaryAbility(){};
    public void secondaryAbility(){};

    // Executes on crouch
    public void crouchOn(){};
    // Executes on crouch release
    public void crouchOff(){}
}
