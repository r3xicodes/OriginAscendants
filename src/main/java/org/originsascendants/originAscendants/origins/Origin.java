package org.originsascendants.originAscendants.origins;

import org.originsascendants.originAscendants.player.PlayerState;

public abstract class Origin {
    protected final PlayerState state;

    protected Origin(PlayerState state) {
        this.state = state;
    }

    //tick logic (aka passive logic)
    public void tick(){};

    public void primaryAbility(){};
    public void secondaryAbility(){};
    public void crouchAbility(){};
}
