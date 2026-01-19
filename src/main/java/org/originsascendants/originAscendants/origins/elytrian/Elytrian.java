package org.originsascendants.originAscendants.origins.elytrian;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.*;
import org.originsascendants.originAscendants.util.AbilityDisplay;
import org.originsascendants.originAscendants.util.BossBarManager;

public class Elytrian extends Origin {

    private boolean wingsFolded = true;
    private double flapCharge = 0.00;
    private boolean isCharging = false;
    private double boostAmount = 3.0;


    public Elytrian(PlayerState state) {
        super(state);

        this.primaryCooldown = 10;
        this.secondaryCooldown = 20;
        this.crouchCooldown = 5;

        // Set up GUI stuff
        this.primaryAbilityDoc = new AbilityDoc(
                "Boost",
                "Flap your wings for a boost into the sky!"
        );
        this.secondaryAbilityDoc = new AbilityDoc(
                "Fold Wings",
                "Toggle the elytra."
        );
        this.crouchAbilityDoc = new AbilityDoc(
                "Flap",
                "Flap your wings for a minor boost in the sky. Hold to charge."
        );
    }

    @Override
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 1.25);
        setMaxHealth(p, 20.0);  // 10 hearts
        setMovementSpeed(p, 0.090); // 90% speed
        setAttackDamage(p, 1.0); // base attack damage
        // Hollow Bones passive: Slow Falling
        org.bukkit.potion.PotionEffect slowFalling = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("SLOW_FALLING", slowFalling);
        p.addPotionEffect(slowFalling);
    }


    public ItemStack createWings() {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = elytra.getItemMeta();
        if (meta != null) {
            meta.setUnbreakable(true);
            meta.setEnchantmentGlintOverride(true);
            elytra.setItemMeta(meta);
        }
        return elytra;
    }

    @Override
    public void tick() {
        Player p = state.toBukkit();
        if (p.isGliding()) {
            if (isCharging) {
                p.sendActionBar(Component.text("Charge: "+String.valueOf(Math.round(flapCharge*100))+"%"));
                if (flapCharge>1.10) {
                    p.sendActionBar(Component.text("Your wings tear and you begin to fall..."));
                    secondaryAbility();
                    flapCharge=0.00;
                }
                flapCharge+=0.05;
            } else {
                if (flapCharge>0) {
                    Vector look = p.getLocation().getDirection();
                    Vector boost = look.multiply(flapCharge*boostAmount);
                    p.setVelocity(boost);
                    flapCharge=0.00;
                }
            }
        } else {
            // basic bug prevention, ig
            isCharging = false;
            flapCharge = 0.00;
        }
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) {
            return;
        }
        if (p.isGliding()) { // If flying, boost in the look direction
            Vector look = p.getLocation().getDirection();
            Vector boost = look.multiply(boostAmount);
            p.setVelocity(p.getVelocity().add(boost));
        } else { // If standing, boost up
            Vector upward = new Vector(0, boostAmount, 0);
            p.setVelocity(upward);
        }
        AbilityDisplay.showPrimaryAbility(p, "Boost", NamedTextColor.YELLOW);
        BossBarManager.showCooldownBar(p, "Boost", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) {
            return;
        }
        if (wingsFolded) {
            org.bukkit.inventory.ItemStack chest = p.getInventory().getChestplate();
            if (chest != null && chest.getType() != Material.AIR) {
                return;
            } else {
                p.getInventory().setChestplate(createWings());
                wingsFolded = false;
                AbilityDisplay.showSecondaryAbility(p, "Fold Wings", NamedTextColor.YELLOW);
                BossBarManager.showCooldownBar(p, "Fold Wings", secondaryCooldown, secondaryCooldown);
                resetSecondaryCooldown();
            }
        } else {
            p.getInventory().setChestplate(null);
            AbilityDisplay.showSecondaryAbility(p, "Fold Wings", NamedTextColor.YELLOW);
            BossBarManager.showCooldownBar(p, "Fold Wings", secondaryCooldown, secondaryCooldown);
            wingsFolded = true;
            resetSecondaryCooldown();
        }
    }

    @Override
    public void crouchOn() {
        /*Player p = state.toBukkit();
        if (p.isGliding()) {
            p.sendActionBar(Component.text("Charge: "+String.valueOf(flapCharge)));
            if (flapCharge>110) {
                p.sendActionBar(Component.text("Your wings tear and you begin to fall..."));
                secondaryAbility();
                flapCharge=0;
            }
            flapCharge+=1;
        }*/
        isCharging = true;

    }

    @Override
    public void crouchOff() {
        /*
        Player p = state.toBukkit();
        if (p.isGliding() && flapCharge>0) {
            Vector look = p.getLocation().getDirection();
            Vector boost = look.multiply(((double)flapCharge+1/100.00)*boostAmount);
            p.setVelocity(boost);
            flapCharge=0;
        }
        */
        isCharging = false;
    }
}

