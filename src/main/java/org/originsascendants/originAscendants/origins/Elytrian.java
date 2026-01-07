package org.originsascendants.originAscendants.origins;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.origins.Origin;
import org.originsascendants.originAscendants.player.*;

public class Elytrian extends Origin {

    private boolean wingsFolded = true;
    private double flapCharge = 0.00;
    private boolean isCharging = false;
    private double boostAmount = 3.0;

    //counters count up to the cooldown. When the counter equals the cooldown, abilities are available
    private int primaryCooldown=10;
    private int secondaryCooldown=10;
    private int primaryCooldownCounter=10;
    private int secondaryCooldownCounter=10;


    public Elytrian(PlayerState state) {
        super(state);

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
        if (primaryCooldownCounter < primaryCooldown) {
            primaryCooldownCounter += 1;
        }
        if (secondaryCooldownCounter < secondaryCooldown) {
            secondaryCooldownCounter += 1;
        }
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
        Player p=state.toBukkit();
        if (primaryCooldownCounter < primaryCooldown) {
            p.sendActionBar(Component.text("Boost is on cooldown"));
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
        primaryCooldownCounter=0;
    }

    @Override
    public void secondaryAbility() {
        Player p=state.toBukkit();
        if (secondaryCooldownCounter < secondaryCooldown) {
            p.sendActionBar(Component.text("Fold Wings is on cooldown"));
            return;
        }
        if (wingsFolded) {
            if (p.getInventory().getChestplate().getType()!=Material.AIR) {
                p.sendActionBar(Component.text("Your armor restricts your wings..."));
                return;
            } else {
                p.getInventory().setChestplate(createWings());
                wingsFolded = false;
                secondaryCooldownCounter=0;
                p.sendActionBar(Component.text("You unfold your wings."));
            }
        } else {
            p.getInventory().setChestplate(null);
            secondaryCooldownCounter=0;
            wingsFolded = true;
            p.sendActionBar(Component.text("You fold your wings."));
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
