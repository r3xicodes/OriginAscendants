package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;

@SuppressWarnings("unused")
public class Blazeborn extends Origin {

    private int heatLevel = 0; // 0-15000
    private int heatDecayCounter = 0;
    private boolean fuelToggle = false;

    public Blazeborn(PlayerState state) {
        super(state);

        this.primaryCooldown = 10;
        this.secondaryCooldown = 30;
        this.crouchCooldown = 5;
        this.primaryAbilityDoc = new AbilityDoc("Fuel Consumption", "Consume fuel items to increase your heat (toggle).");
        this.secondaryAbilityDoc = new AbilityDoc("Thermal Surge", "Instantly boost heat and purge negative effects.");
        this.crouchAbilityDoc = new AbilityDoc("Heat Core", "Passive heat core mechanics.");
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        // Fire immunity is passive via tick
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void removeAttributes() {
        super.removeAttributes();
        Player p = state.toBukkit();
        p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        
        // Heat decay
        heatDecayCounter++;
        if (heatDecayCounter >= 20) { // Decay every 20 ticks (1 second)
            heatLevel = Math.max(0, heatLevel - 50);
            heatDecayCounter = 0;
        }

        // Fire immunity passive
        if (!p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }

        // Heat bar display
        int heatPercent = (heatLevel / 150);
        p.sendActionBar(Component.text("Heat: " + heatPercent + "%"));

        // Heat-based effects
        if (heatLevel > 10000) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 1, 0, false, false));
        }
        if (heatLevel > 14000) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, 1, false, false));
        }
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) {
            p.sendActionBar(Component.text("Fuel Consumption on cooldown"));
            return;
        }

        fuelToggle = !fuelToggle;
        if (fuelToggle) {
            // Consume fuel items from inventory
            if (consumeFuel(p)) {
                heatLevel = Math.min(15000, heatLevel + 2000);
                p.sendActionBar(Component.text("Fuel consumed. Heat: " + (heatLevel / 150) + "%"));
                resetPrimaryCooldown();
            } else {
                p.sendActionBar(Component.text("No fuel found!"));
                fuelToggle = false;
            }
        }
    }

    private boolean consumeFuel(Player p) {
        ItemStack[] contents = p.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null && (item.getType() == Material.COAL || 
                item.getType() == Material.CHARCOAL || 
                item.getType() == Material.BLAZE_ROD)) {
                item.setAmount(item.getAmount() - 1);
                if (item.getAmount() <= 0) {
                    contents[i] = null;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) {
            p.sendActionBar(Component.text("Thermal Surge on cooldown"));
            return;
        }

        // Boost heat and remove negative effects
        heatLevel = Math.min(15000, heatLevel + 5000);
        p.removePotionEffect(PotionEffectType.WITHER);
        p.removePotionEffect(PotionEffectType.POISON);
        p.removePotionEffect(PotionEffectType.HUNGER);
        p.sendActionBar(Component.text("Thermal Surge activated!"));
        resetSecondaryCooldown();
    }

    @Override
    public void crouchOn() {
        // Heat core passive activation
    }

    @Override
    public void crouchOff() {
        // Heat core passive deactivation
    }
}