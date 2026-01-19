package org.originsascendants.originAscendants.origins.enderian;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;

public class Enderian extends Origin {

    public Enderian(PlayerState state) {
        super(state);

        this.primaryCooldown = 20;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Blink", "Teleport short distance in the direction you're looking.");
        this.secondaryAbilityDoc = new AbilityDoc("Endport", "Teleport to a block you're looking at.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 1.50);
        setMaxHealth(p, 20.0);  // 10 hearts
        setMovementSpeed(p, 0.103); // 103% speed
        setAttackDamage(p, 1.0); // base damage
        setFallDamageMultiplier(p, 0.0);  // 0% fall damage - Enderians are immune to fall damage
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) {
            return;
        }
        org.bukkit.Location target = p.getLocation().add(p.getLocation().getDirection().multiply(16));
        target.setY(p.getWorld().getHighestBlockYAt(target) + 1);
        p.teleport(target);
        AbilityDisplay.showPrimaryAbility(p, "Blink", NamedTextColor.DARK_PURPLE);
        BossBarManager.showCooldownBar(p, "Blink", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) {
            return;
        }
        RayTraceResult result = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getLocation().getDirection(), 128);
        if (result != null && result.getHitBlock() != null) {
            org.bukkit.Location target = result.getHitBlock().getLocation().add(0.5, 1, 0.5);
            p.teleport(target);
            AbilityDisplay.showSecondaryAbility(p, "Endport", NamedTextColor.DARK_PURPLE);
            BossBarManager.showCooldownBar(p, "Endport", secondaryCooldown, secondaryCooldown);
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
