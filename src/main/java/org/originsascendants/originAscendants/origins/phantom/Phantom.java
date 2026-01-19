package org.originsascendants.originAscendants.origins.phantom;

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
public class Phantom extends Origin {

    private boolean invisible = false;

    public Phantom(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Veilwalk", "Toggle invisibility.");
        this.secondaryAbilityDoc = new AbilityDoc("Glide", "Glide through air.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        // Phantoms are ethereal, lighter, and take reduced fall damage
        setScale(p, 0.85);
        setMaxHealth(p, 16.0);  // 8 hearts
        setFallDamageMultiplier(p, 0.25);
        setFlyingSpeed(p, 0.08);
        setMovementSpeed(p, 0.070); // 70% speed
        setAttackDamage(p, 1.0); // base attack damage
        
        // Passive: Dark Refuge - strength in darkness (handled via tick/events)
        // Passive: Ethereal - already handled via reduced gravity and scale
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        invisible = !invisible;
        if (invisible) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            AbilityDisplay.showPrimaryAbility(p, "Veilwalk", NamedTextColor.DARK_GRAY);
            p.sendActionBar(Component.text("You are invisible", NamedTextColor.DARK_GRAY));
            BossBarManager.showResourceBar(p, "Veilwalk", 1, 1, BossBar.Color.WHITE);
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
            p.sendActionBar(Component.text("You are visible", NamedTextColor.GRAY));
            BossBarManager.hideResourceBar(p, "Veilwalk");
        }
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.setVelocity(p.getVelocity().add(new org.bukkit.util.Vector(0, 0.5, 0)));
        AbilityDisplay.showSecondaryAbility(p, "Glide", NamedTextColor.DARK_GRAY);
        p.sendActionBar(Component.text("Gliding gracefully", NamedTextColor.DARK_GRAY));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }
}

