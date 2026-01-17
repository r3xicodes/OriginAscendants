package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Dwarf extends Origin {

    private long prospectStartTime = 0;
    private boolean prospecting = false;

    public Dwarf(PlayerState state) {
        super(state);
        this.primaryCooldown = 20;
        this.secondaryCooldown = 30;
        this.primaryAbilityDoc = new AbilityDoc("Prospecting", "Highlight nearby ores for 60 seconds.");
        this.secondaryAbilityDoc = new AbilityDoc("Rallying Cry", "Grant resistance to nearby allies.");
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        prospecting = true;
        prospectStartTime = System.currentTimeMillis();
        p.sendActionBar(Component.text("Ores highlighted for 60 seconds!"));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        var nearby = p.getWorld().getNearbyPlayers(p.getLocation(), 30);
        for (Player player : nearby) {
            if (player != p) {
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 100, 1, false, false));
            }
        }
        p.sendActionBar(Component.text("Rallying Cry!"));
        resetSecondaryCooldown();
    }

    @Override
    public void tick() {
        if (prospecting && System.currentTimeMillis() - prospectStartTime > 60000) {
            prospecting = false;
        }
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }
}
