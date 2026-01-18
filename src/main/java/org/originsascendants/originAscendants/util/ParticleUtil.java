package org.originsascendants.originAscendants.util;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * Utility class for spawning particle effects
 */
public class ParticleUtil {
    
    /**
     * Spawn particles at location
     */
    public static void spawnParticles(Location loc, Particle particle, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        if (loc == null || loc.getWorld() == null) return;
        loc.getWorld().spawnParticle(particle, loc, count, offsetX, offsetY, offsetZ, speed);
    }
    
    /**
     * Spawn particles around player
     */
    public static void spawnParticlesAroundPlayer(Player player, Particle particle, int count) {
        if (player == null) return;
        spawnParticles(player.getLocation(), particle, count, 0.5, 0.5, 0.5, 0.1);
    }
    
    /**
     * Spawn particles at eye level
     */
    public static void spawnParticlesAtEyes(Player player, Particle particle, int count, double offsetX, double offsetY, double offsetZ) {
        if (player == null) return;
        spawnParticles(player.getEyeLocation(), particle, count, offsetX, offsetY, offsetZ, 0.1);
    }
    
    /**
     * Spawn line of particles
     */
    public static void spawnParticleLine(Location start, Location end, Particle particle, int pointsPerMeter) {
        if (start == null || end == null || !start.getWorld().equals(end.getWorld())) return;
        
        double distance = start.distance(end);
        int points = Math.max(1, (int) (distance * pointsPerMeter));
        
        for (int i = 0; i <= points; i++) {
            double progress = i / (double) points;
            Location loc = start.clone().add(
                end.getX() - start.getX(),
                end.getY() - start.getY(),
                end.getZ() - start.getZ()
            );
            spawnParticles(loc, particle, 1, 0, 0, 0, 0);
        }
    }
}
