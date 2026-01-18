package org.originsascendants.originAscendants.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.player.PlayerState;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

public final class PlayerRegistry {
    // Stores all player UUIDs as values to reference playerstates.
    private static final Map<UUID, PlayerState> players = new HashMap<>();

    public static void registerPlayer(PlayerState player) {
        players.put(player.getUUID(), player);
    }

    public static PlayerState getPlayerFromUUID(UUID uuid) {
        return players.get(uuid);
    }

    public static Collection<PlayerState> getAllPlayerStates() {
        return players.values();
    }

    public static boolean exists(UUID uuid) {
        return players.containsKey(uuid);
    }

    public static void clearAll() {
        players.clear();
    }
}