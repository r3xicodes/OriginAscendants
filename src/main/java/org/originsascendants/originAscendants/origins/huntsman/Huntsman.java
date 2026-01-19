package org.originsascendants.originAscendants.origins.huntsman;

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
public class Huntsman extends Origin {

    private int arrowTip = 0; // 0=standard, 1=buff, 2=stun
    private static final String[] tips = {"Standard", "Buff", "Stun"};

    public Huntsman(PlayerState state) {
        super(state);

        this.primaryCooldown = 30;
        this.secondaryCooldown = 10;
        this.primaryAbilityDoc = new AbilityDoc("Precision", "Turn invisible and shoot arrow.");
        this.secondaryAbilityDoc = new AbilityDoc("Change Tip", "Cycle arrow tip types.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setMaxHealth(p, 20.0);  // 10 hearts
        setMovementSpeed(p, 0.090); // 90% speed
        setAttackDamage(p, 1.0); // base attack damage
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.INVISIBILITY, 40, 0));
        org.bukkit.entity.Arrow arrow = p.getWorld().spawnArrow(p.getEyeLocation(), p.getLocation().getDirection(), 2f, 0);
        arrow.setDamage(10);
        AbilityDisplay.showPrimaryAbility(p, "Precision", NamedTextColor.GRAY);
        BossBarManager.showCooldownBar(p, "Precision", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        arrowTip = (arrowTip + 1) % 3;
        AbilityDisplay.showSecondaryAbility(p, "Change Tip", NamedTextColor.GRAY);
        BossBarManager.showCooldownBar(p, "Change Tip", secondaryCooldown, secondaryCooldown);
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
