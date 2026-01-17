package org.originsascendants.originAscendants;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        // Permission check
        if (!sender.hasPermission("originascendants.setorigin")) {
            sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
            return true;
        }

        // Argument validation
        if (args.length != 2) {
            sender.sendMessage(Component.text("Usage: /setorigin <player> <origin>", NamedTextColor.YELLOW));
            return false;
        }

        String playerName = args[0];
        String originType = args[1].toUpperCase();

        // Get target player
        Player p = Bukkit.getPlayerExact(playerName);
        if (p == null) {
            sender.sendMessage(Component.text("Player '" + playerName + "' not found!", NamedTextColor.RED));
            return true;
        }

        // Get or create PlayerState
        PlayerState state = PlayerRegistry.getPlayerFromUUID(p.getUniqueId());
        if (state == null) {
            state = new PlayerState(p.getUniqueId());
            state.setOrigin(OriginFactory.createOrigin("HUMAN", state));
            PlayerRegistry.registerPlayer(state);
        }

        // Validate and create new origin
        Origin origin;
        try {
            origin = OriginFactory.createOrigin(originType, state);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage(Component.text("Unknown origin type: " + originType, NamedTextColor.RED));
            return true;
        } catch (Exception ex) {
            sender.sendMessage(Component.text("Error creating origin: " + ex.getMessage(), NamedTextColor.RED));
            return true;
        }

        // Remove old origin attributes before switching
        if (state.getOrigin() != null) {
            state.getOrigin().removeAttributes();
        }

        // Set new origin
        state.setOrigin(origin);
        origin.applyAttributes();

        // Notify
        sender.sendMessage(Component.text("Set " + p.getName() + "'s origin to " + originType, NamedTextColor.GREEN));
        p.sendMessage(Component.text("Your origin has been changed to " + originType + "!", NamedTextColor.AQUA));

        return true;
    }
}
