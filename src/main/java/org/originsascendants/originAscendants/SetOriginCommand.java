package org.originsascendants.originAscendants;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.originsascendants.originAscendants.origins.*;
import org.originsascendants.originAscendants.player.*;

public class SetOriginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("usage: /setorigin <player> <origin>");
            return false;
        }

        String playerName = args[0];
        String originType = args[1];

        Player p = Bukkit.getPlayerExact(playerName);
        if (p == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        PlayerState state = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
        if (state == null) {
            // If the player somehow lacks a PlayerState (e.g., plugin loaded while they were online), create one.
            state = new PlayerState(p.getUniqueId());
            state.setOrigin(OriginFactory.createOrigin("HUMAN", state));
            PlayerRegistry.registerPlayer(state);
            sender.sendMessage("Created PlayerState for " + playerName + " and set default origin.");
        }

        Origin origin;
        try {
            origin = OriginFactory.createOrigin(originType, state);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage("Unknown origin type: "+originType);
            return true;
        }
        
        // Remove old origin attributes before switching
        if (state.getOrigin() != null) {
            state.getOrigin().removeAttributes();
        }
        
        state.setOrigin(origin);
        origin.applyAttributes();
        sender.sendMessage("Set "+playerName+"'s origin to "+originType);
        return true;
    }
}
