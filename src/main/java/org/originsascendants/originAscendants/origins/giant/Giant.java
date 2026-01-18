package org.originsascendants.originAscendants.origins.giant;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.originsascendants.originAscendants.origins.base.Origin;
import org.originsascendants.originAscendants.gui.AbilityDoc;
import org.originsascendants.originAscendants.player.PlayerState;
import org.bukkit.entity.Player;

/**
 * Giant Origin - Massive strength and crowd control
 * 
 * Abilities:
 * - Roar (Primary): Stuns enemies, buffs allies
 * - Enlarge (Secondary): Toggle larger form with strength
 */
@SuppressWarnings("unused")
public class Giant extends Origin {

    private boolean enlarged = false;

    public Giant(PlayerState state) {
        super(state);
        this.primaryCooldown = 25;
        this.secondaryCooldown = 40;
        this.primaryAbilityDoc = new AbilityDoc("Roar", "Stun nearby enemies and buff allies within 20 blocks. Cooldown: 1.25s");
        this.secondaryAbilityDoc = new AbilityDoc("Enlarge", "Toggle larger form with Strength III and Resistance. Cooldown: 2s");
    }

    @Override
    public String getDisplayName() {
        return "§6Giant";
    }

    @Override
    public String getDescription() {
        return "Massive warrior with crowd control abilities";
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        
        var entities = p.getWorld().getNearbyLivingEntities(p.getLocation(), 20);
        int affectedEnemies = 0;
        int affectedAllies = 0;
        
        for (org.bukkit.entity.LivingEntity le : entities) {
            if (le != p && !(le instanceof Player)) {
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOWNESS, 80, 3, false, false));
                affectedEnemies++;
            } else if (le instanceof Player && le != p) {
                le.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, 100, 1, false, false));
                affectedAllies++;
            }
        }
        
        p.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
        p.sendMessage(Component.text("☆ ROAR ☆", NamedTextColor.GOLD).append(Component.text(" - Crowd Control", NamedTextColor.YELLOW)));
        p.sendMessage(Component.text("Enemies Stunned: " + affectedEnemies, NamedTextColor.RED));
        p.sendMessage(Component.text("Allies Buffed: " + affectedAllies, NamedTextColor.GREEN));
        p.sendActionBar(Component.text("ROAR! ", NamedTextColor.GOLD).append(Component.text("Enemies stunned!", NamedTextColor.RED)));
        
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        
        enlarged = !enlarged;
        if (enlarged) {
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2, false, false));
            p.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            
            p.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
            p.sendMessage(Component.text("↑ ENLARGE ↑", NamedTextColor.GOLD).append(Component.text(" - Form Changed", NamedTextColor.YELLOW)));
            p.sendMessage(Component.text("Strength III + Resistance I", NamedTextColor.GREEN));
            p.sendActionBar(Component.text("You have ENLARGED!", NamedTextColor.GOLD));
        } else {
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.STRENGTH);
            p.removePotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE);
            
            p.sendMessage(Component.text("━━━━━━━━━━━━━━━━━━━━━━━", NamedTextColor.DARK_GRAY));
            p.sendMessage(Component.text("↓ SHRINK ↓", NamedTextColor.GRAY).append(Component.text(" - Form Changed", NamedTextColor.YELLOW)));
            p.sendMessage(Component.text("Effects Removed", NamedTextColor.RED));
            p.sendActionBar(Component.text("You have SHRUNK", NamedTextColor.GRAY));
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
