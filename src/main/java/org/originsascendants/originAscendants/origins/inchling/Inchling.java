package org.originsascendants.originAscendants.origins.inchling;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Inchling extends Origin {

    private boolean invisible = false;

    public Inchling(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 15;
        this.primaryAbilityDoc = new AbilityDoc("Flee", "Dash backward with invisibility.");
        this.secondaryAbilityDoc = new AbilityDoc("Burrow", "Toggle burrowing passive.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 0.25);
        setMaxHealth(p, 12.0);  // 6 hearts
        setMovementSpeed(p, 0.090); // 90% speed
        setAttackDamage(p, 1.0); // base attack damage
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.setVelocity(p.getLocation().getDirection().multiply(-1.5).add(new org.bukkit.util.Vector(0, 0.3, 0)));
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, 80, 0, false, false));
        AbilityDisplay.showPrimaryAbility(p, "Flee", NamedTextColor.GREEN);
        BossBarManager.showCooldownBar(p, "Flee", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        invisible = !invisible;
        if (invisible) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            AbilityDisplay.showSecondaryAbility(p, "Burrow", NamedTextColor.GREEN);
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
            AbilityDisplay.showSecondaryAbility(p, "Burrow", NamedTextColor.GREEN);
        }
        BossBarManager.showCooldownBar(p, "Burrow", secondaryCooldown, secondaryCooldown);
        resetSecondaryCooldown();
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

