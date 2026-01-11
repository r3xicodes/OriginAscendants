package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.player.*;

public class OriginFactory {
    public static Origin createOrigin(String typeName, PlayerState state) {
        switch (typeName) {
            case "HUMAN":
            case "Human":
                state.toBukkit().sendMessage(Component.text("Creating Human origin..."));
                return new Human(state);
            case "ELYTRIAN":
            case "Elytrian":
                state.toBukkit().sendMessage(Component.text("Creating Elytrian origin..."));
                return new Elytrian(state);
            case "ALCHEMIST":
            case "Alchemist":
                state.toBukkit().sendMessage(Component.text("Creating Alchemist origin..."));
                return new Alchemist(state);
            case "ARACHNID":
            case "Arachnid":
                state.toBukkit().sendMessage(Component.text("Creating Arachnid origin..."));
                return new Arachnid(state);
            case "BEE":
            case "Bee":
                state.toBukkit().sendMessage(Component.text("Creating Bee origin..."));
                return new Bee(state);
            case "BLAZEBORN":
            case "Blazeborn":
                state.toBukkit().sendMessage(Component.text("Creating Blazeborn origin..."));
                return new Blazeborn(state);
            case "CHICKEN":
            case "Chicken":
                state.toBukkit().sendMessage(Component.text("Creating Chicken origin..."));
                return new Chicken(state);
            case "ENDERIAN":
            case "Enderian":
                state.toBukkit().sendMessage(Component.text("Creating Enderian origin..."));
                return new Enderian(state);
            case "FAIRY":
            case "Fairy":
                state.toBukkit().sendMessage(Component.text("Creating Fairy origin..."));
                return new Fairy(state);
            case "GIANT":
            case "Giant":
                state.toBukkit().sendMessage(Component.text("Creating Giant origin..."));
                return new Giant(state);
            case "HUNTSMAN":
            case "Huntsman":
                state.toBukkit().sendMessage(Component.text("Creating Huntsman origin..."));
                return new Huntsman(state);
            case "INCHLING":
            case "Inchling":
                state.toBukkit().sendMessage(Component.text("Creating Inchling origin..."));
                return new Inchling(state);
            case "BUNNY":
            case "Bunny":
                state.toBukkit().sendMessage(Component.text("Creating Bunny origin..."));
                return new Bunny(state);
            case "SCULKBORN":
            case "Sculkborn":
                state.toBukkit().sendMessage(Component.text("Creating Sculkborn origin..."));
                return new Sculkborn(state);
            case "DWARF":
            case "Dwarf":
                state.toBukkit().sendMessage(Component.text("Creating Dwarf origin..."));
                return new Dwarf(state);
            case "PHYTOKIN":
            case "Phytokin":
                state.toBukkit().sendMessage(Component.text("Creating Phytokin origin..."));
                return new Phytokin(state);
            case "SHULK":
            case "Shulk":
                state.toBukkit().sendMessage(Component.text("Creating Shulk origin..."));
                return new Shulk(state);
            case "MERLING":
            case "Merling":
                state.toBukkit().sendMessage(Component.text("Creating Merling origin..."));
                return new Merling(state);
            case "PAWSWORN":
            case "Pawsworn":
                state.toBukkit().sendMessage(Component.text("Creating Pawsworn origin..."));
                return new Pawsworn(state);
            case "PHANTOM":
            case "Phantom":
                state.toBukkit().sendMessage(Component.text("Creating Phantom origin..."));
                return new Phantom(state);
            case "WEREWOLF":
            case "Werewolf":
                state.toBukkit().sendMessage(Component.text("Creating Werewolf origin..."));
                return new Werewolf(state);
            case "VAMPIRE":
            case "Vampire":
                state.toBukkit().sendMessage(Component.text("Creating Vampire origin..."));
                return new Vampire(state);
            case "BREEZEBORN":
            case "Breezeborn":
                state.toBukkit().sendMessage(Component.text("Creating Breezeborn origin..."));
                return new Breezeborn(state);
            default: throw new IllegalArgumentException("Type \""+typeName+"\" doesn't exist.");
        }
    }
}
