package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
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
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        invisible = !invisible;
        if (invisible) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            p.sendActionBar(Component.text("Invisible!"));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
            p.sendActionBar(Component.text("Visible!"));
        }
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        p.setVelocity(p.getVelocity().add(new org.bukkit.util.Vector(0, 0.5, 0)));
        p.sendActionBar(Component.text("Gliding!"));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }
}
