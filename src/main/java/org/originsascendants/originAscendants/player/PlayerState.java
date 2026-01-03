package org.originsascendants.originAscendants.player;

import java.util.UUID;
import org.originsascendants.originAscendants.origins.Origin;

public class PlayerState {
    private final UUID uuid;
    private Origin origin;

    public PlayerState(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Origin getOrigin() {
        return this.origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
}
