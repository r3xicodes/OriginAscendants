package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
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
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.setVelocity(p.getLocation().getDirection().multiply(-1.5).add(new org.bukkit.util.Vector(0, 0.3, 0)));
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, 80, 0, false, false));
        p.sendActionBar(Component.text("Flee!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        invisible = !invisible;
        if (invisible) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
            p.sendActionBar(Component.text("Burrow ON"));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY);
            p.sendActionBar(Component.text("Burrow OFF"));
        }
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
