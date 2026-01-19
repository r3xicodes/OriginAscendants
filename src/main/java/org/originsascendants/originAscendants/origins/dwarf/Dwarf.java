package org.originsascendants.originAscendants.origins.dwarf;

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
import org.bukkit.block.Block;
import org.bukkit.Particle;

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
    public void applyAttributes() {
        super.applyAttributes();
        Player p = state.toBukkit();
        setScale(p, 0.75);
        setMaxHealth(p, 22.0);  // 11 hearts
        setMovementSpeed(p, 0.090); // 90% speed
        setAttackDamage(p, 1.0); // base damage
        // Mining efficiency - give Haste I for faster mining (125% base)
        org.bukkit.potion.PotionEffect haste = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.HASTE, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("HASTE", haste);
        p.addPotionEffect(haste);
        // Born Underground passive: Night Vision
        org.bukkit.potion.PotionEffect nightVision = new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
        appliedEffects.put("NIGHT_VISION", nightVision);
        p.addPotionEffect(nightVision);
    }

    @Override
    public void removeAttributes() {
        super.removeAttributes();
        prospecting = false;
    }

    @Override
    public void primaryAbility() {
        Player p = state.toBukkit();
        if (!isPrimaryReady()) return;
        prospecting = true;
        prospectStartTime = System.currentTimeMillis();
        AbilityDisplay.showPrimaryAbility(p, "Prospecting", NamedTextColor.GOLD);
        BossBarManager.showCooldownBar(p, "Prospecting", primaryCooldown, primaryCooldown);
        resetPrimaryCooldown();
    }

    @Override
    public void secondaryAbility() {
        Player p = state.toBukkit();
        if (!isSecondaryReady()) return;
        
        var nearby = p.getWorld().getNearbyPlayers(p.getLocation(), 30);
        int buffedCount = 0;
        
        for (Player player : nearby) {
            if (player != p) {
                player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.RESISTANCE, 100, 1, false, false));
                buffedCount++;
            }
        }
        
        AbilityDisplay.showSecondaryAbility(p, "Rallying Cry", NamedTextColor.GOLD);
        BossBarManager.showCooldownBar(p, "Rallying Cry", secondaryCooldown, secondaryCooldown);
        p.sendActionBar(Component.text("Rallying Cry! ", NamedTextColor.GOLD).append(Component.text(buffedCount + " allies buffed", NamedTextColor.YELLOW)));
        
        // Play sound effect
        p.getWorld().playSound(p.getLocation(), org.bukkit.Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.0f, 1.0f);
        
        resetSecondaryCooldown();
    }

    @Override
    public void tick() {
        if (prospecting) {
            long elapsed = System.currentTimeMillis() - prospectStartTime;
            
            // Stop prospecting after 60 seconds
            if (elapsed > 60000) {
                prospecting = false;
                return;
            }
            
            Player p = state.toBukkit();
            
            // Highlight nearby ores
            int radius = 40;
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block block = p.getWorld().getBlockAt(p.getLocation().add(x, y, z));
                        Material type = block.getType();
                        
                        // Check if it's an ore
                        if (isOre(type)) {
                            // Show glowing particles on ores
                            p.getWorld().spawnParticle(Particle.SOUL, block.getLocation().add(0.5, 0.5, 0.5), 2, 0.2, 0.2, 0.2, 0);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Helper method to check if a block is an ore
     */
    private boolean isOre(Material material) {
        return material == Material.COAL_ORE || material == Material.DEEPSLATE_COAL_ORE ||
               material == Material.IRON_ORE || material == Material.DEEPSLATE_IRON_ORE ||
               material == Material.GOLD_ORE || material == Material.DEEPSLATE_GOLD_ORE ||
               material == Material.LAPIS_ORE || material == Material.DEEPSLATE_LAPIS_ORE ||
               material == Material.DIAMOND_ORE || material == Material.DEEPSLATE_DIAMOND_ORE ||
               material == Material.EMERALD_ORE || material == Material.DEEPSLATE_EMERALD_ORE ||
               material == Material.REDSTONE_ORE || material == Material.DEEPSLATE_REDSTONE_ORE ||
               material == Material.COPPER_ORE || material == Material.DEEPSLATE_COPPER_ORE;
    }

    @Override
    public void crouchOff() {
    }

    @Override
    public void crouchOn() {
    }

    @Override
    public void onConsume(org.bukkit.event.player.PlayerItemConsumeEvent event) {
        // Dwarves only eat meat: beef, pork, chicken, mutton, salmon, cod
        Material item = event.getItem().getType();
        if (!isMeat(item)) {
            event.setCancelled(true);
            state.toBukkit().sendActionBar(Component.text("Dwarves only eat meat!", NamedTextColor.RED));
        }
    }

    private boolean isMeat(Material material) {
        return material == Material.COOKED_BEEF || material == Material.BEEF ||
               material == Material.COOKED_PORKCHOP || material == Material.PORKCHOP ||
               material == Material.COOKED_CHICKEN || material == Material.CHICKEN ||
               material == Material.COOKED_MUTTON || material == Material.MUTTON ||
               material == Material.COOKED_SALMON || material == Material.SALMON ||
               material == Material.COOKED_COD || material == Material.COD;
    }
}

