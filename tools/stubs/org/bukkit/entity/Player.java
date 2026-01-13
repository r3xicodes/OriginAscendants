package org.bukkit.entity;

import java.util.UUID;

public interface Player {
    UUID getUniqueId();
    String getName();
    void sendMessage(String msg);
}