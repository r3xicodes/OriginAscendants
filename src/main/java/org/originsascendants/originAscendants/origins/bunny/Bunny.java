package org.originsascendants.originAscendants.origins.bunny;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;
import org.bukkit.entity.Player;
import org.bukkit.Material;

@SuppressWarnings("unused")
public class Bunny extends Origin {

    private double chargeAmount = 0;

    public Bunny(PlayerState state) {
        super(state);
        this.primaryCooldown = 15;
        this.secondaryCooldown = 25;
        this.primaryAbilityDoc = new AbilityDoc("Charge Jump", "Hold to charge, release to jump high.");
        this.secondaryAbilityDoc = new AbilityDoc("Swift Escape", "Dash away quickly.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        // Bunnies are smaller, faster, and take reduced fall damage
        setScale(p, 0.85);
        setFallDamageMultiplier(p, 0.85);
        setMovementSpeed(p, 0.15);
        setMaxHealth(p, 12.0);  // 6 hearts
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        chargeAmount = Math.min(3.0, chargeAmount + 0.5);
        org.bukkit.util.Vector up = new org.bukkit.util.Vector(0, chargeAmount, 0);
        p.setVelocity(up);
        AbilityDisplay.showPrimaryAbility(p, "Charge Jump", NamedTextColor.LIGHT_PURPLE);
        BossBarManager.showResourceBar(p, "Jump Charge", (int)(chargeAmount * 100), 300, BossBar.Color.PURPLE);
        p.sendActionBar(Component.text("Charging: ", NamedTextColor.LIGHT_PURPLE).append(Component.text(String.format("%.1f", chargeAmount) + "/3.0", NamedTextColor.YELLOW)));
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        org.bukkit.util.Vector dir = p.getLocation().getDirection().normalize().multiply(1.5);
        p.setVelocity(dir.add(new org.bukkit.util.Vector(0, 0.5, 0)));
        AbilityDisplay.showSecondaryAbility(p, "Swift Escape", NamedTextColor.LIGHT_PURPLE);
        p.sendActionBar(Component.text("Dashing away!", NamedTextColor.LIGHT_PURPLE));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOff() {
        BossBarManager.hideResourceBar(state.toBukkit(), "Jump Charge");
    }

    @Override
    public void crouchOn() {
        AbilityDisplay.showCrouchAbility(state.toBukkit(), "Charge Jump", NamedTextColor.LIGHT_PURPLE);
        // Charge jump preparation
    }

    @Override
    public void onConsume(org.bukkit.event.player.PlayerItemConsumeEvent event) {
        // Bunnies only eat plants: wheat, carrots, golden carrots, beetroots, berries, pumpkin
        org.bukkit.Material item = event.getItem().getType();
        if (!isPlant(item)) {
            event.setCancelled(true);
            state.toBukkit().sendActionBar(Component.text("Bunnies only eat plants!", NamedTextColor.RED));
        }
    }

    private boolean isPlant(org.bukkit.Material material) {
        return material == org.bukkit.Material.WHEAT ||
               material == org.bukkit.Material.CARROT ||
               material == org.bukkit.Material.GOLDEN_CARROT ||
               material == org.bukkit.Material.BEETROOT ||
               material == org.bukkit.Material.SWEET_BERRIES ||
               material == org.bukkit.Material.GLOW_BERRIES ||
               material == org.bukkit.Material.PUMPKIN ||
               material == org.bukkit.Material.MELON;
    }
}

