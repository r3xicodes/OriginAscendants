package org.originsascendants.originAscendants.origins.huntsman;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Huntsman extends Origin {

    private int arrowTip = 0; // 0=standard, 1=buff, 2=stun

    public Huntsman(PlayerState state) {
        super(state);

        this.primaryCooldown = 30;
        this.secondaryCooldown = 10;
        this.primaryAbilityDoc = new AbilityDoc("Precision", "Turn invisible and shoot arrow.");
        this.secondaryAbilityDoc = new AbilityDoc("Change Tip", "Cycle arrow tip types.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, 40, 0));
        org.bukkit.entity.Arrow arrow = p.getWorld().spawnArrow(p.getEyeLocation(), p.getLocation().getDirection(), 2f, 0);
        arrow.setDamage(10);
        p.sendActionBar(Component.text("Precision shot!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        arrowTip = (arrowTip + 1) % 3;
        String[] tips = {"Standard", "Healing", "Stun"};
        p.sendActionBar(Component.text("Arrow tip: " + tips[arrowTip]));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }

    @Override
    public void tick() {
    }
} 
