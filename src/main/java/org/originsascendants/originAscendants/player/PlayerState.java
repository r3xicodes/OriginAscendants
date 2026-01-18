package org.originsascendants.originAscendants.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.originsascendants.originAscendants.origins.base.Origin;

public class PlayerState {
    // This class is for origin-related data in reference to a player.
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

    // Access the PlayerState as a Bukkit Player in order to perform calculations.
    public Player toBukkit() {
        return Bukkit.getPlayer(this.uuid);
    }
}
