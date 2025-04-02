package org.mrdarkimc.enhancedtraps.DomainExpansion;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;
import org.mrdarkimc.enhancedtraps.hooks.WGSchemLoader;

public class DomainExpansion {
    public void spawn(Player player) {
        Location loc = player.getLocation();
        playSoundToNearPlayers(player);
        SphereParticle.spawnParticleSphere(loc,29,1000, Particle.SOUL_FIRE_FLAME);

        new BukkitRunnable() {
            @Override
            public void run() {
                new Sphere().spawn(player, loc);
            }
        }.runTaskLater(EnhancedTraps.getInstance(), 10);

        Clipboard board = WGSchemLoader.clipboardMap.get("AnimatedSphere.schem");
        new BukkitRunnable() {
            @Override
            public void run() {
                new AnimatedSphere(board).spawn(player, loc);
            }
        }.runTaskLater(EnhancedTraps.getInstance(), 10 + (6 * 3L));


    }

    void playSoundToNearPlayers(Player player) {
        player.getWorld().getNearbyEntities(player.getLocation(), 45, 45, 45)
                .stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .forEach(p -> p.playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 0.5F, 1));
    }
}
