package org.originsascendants.originAscendants.origins.enderian;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;

public class Enderian extends Origin {

    public Enderian(PlayerState state) {
        super(state);

        this.primaryCooldown = 20;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Blink", "Teleport short distance in the direction you're looking.");
        this.secondaryAbilityDoc = new AbilityDoc("Endport", "Teleport to a block you're looking at.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) {
            p.sendActionBar(Component.text("Blink on cooldown"));
            return;
        }
        org.bukkit.Location target = p.getLocation().add(p.getLocation().getDirection().multiply(16));
        target.setY(p.getWorld().getHighestBlockYAt(target) + 1);
        p.teleport(target);
        p.sendActionBar(Component.text("Blinked!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) {
            p.sendActionBar(Component.text("Endport on cooldown"));
            return;
        }
        RayTraceResult result = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getLocation().getDirection(), 128);
        if (result != null && result.getHitBlock() != null) {
            org.bukkit.Location target = result.getHitBlock().getLocation().add(0.5, 1, 0.5);
            p.teleport(target);
            p.sendActionBar(Component.text("Endported!"));
            resetSecondaryCooldown();
        }
    }

    @Override
    public void tick() {
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }
}
