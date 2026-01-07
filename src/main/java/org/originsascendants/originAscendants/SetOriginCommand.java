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
            sender.sendMessage("Player has no PlayerState.");
            return true;
        }

        Origin origin = OriginFactory.createOrigin(originType, state);
        if (origin == null) {
            sender.sendMessage("Unknown origin type.");
            return true;
        }
        state.setOrigin(origin);
        sender.sendMessage("Set "+playerName+"'s origin to "+originType);
        return true;
    }
}
