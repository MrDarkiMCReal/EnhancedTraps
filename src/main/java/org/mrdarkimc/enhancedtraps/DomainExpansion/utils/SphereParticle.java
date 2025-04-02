package org.mrdarkimc.enhancedtraps.DomainExpansion;

import org.bukkit.Location;
import org.bukkit.Particle;

public class SphereParticle {
    public static void spawnParticleSphere(Location center, double radius, int particlesCount, Particle type) {
        for (int i = 0; i < particlesCount; i++) {
            double theta = Math.random() * Math.PI * 2;
            double phi = Math.random() * Math.PI;

            double x = center.getX() + radius * Math.sin(phi) * Math.cos(theta);
            double y = center.getY() + radius * Math.sin(phi) * Math.sin(theta);
            double z = center.getZ() + radius * Math.cos(phi);


            Location particleLocation = new Location(center.getWorld(), x, y, z);
            center.getWorld().spawnParticle(type, particleLocation, 0);
        }
    }
}
