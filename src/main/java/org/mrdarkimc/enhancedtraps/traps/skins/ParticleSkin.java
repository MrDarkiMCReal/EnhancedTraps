package org.mrdarkimc.enhancedtraps.traps.skins;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleSkin implements Skinable {
    String name;
    Particle particleType;
    int amount;
    double offsetX;
    double offsetY;
    double offsetZ;
    double speed;

    public ParticleSkin(String name,Particle particleType, int amount, double offsetX, double offsetY, double offsetZ, double speed) {

        //todo использовать Particle.Trail


        this.name = name;
        this.particleType = particleType;
        this.amount = amount;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getSkin() {
        return this;
    }

    @Override
    public void spawn(Player player) {
        if (particleType == null){
            return;
        }
        Location loc = player.getLocation();
        loc.getWorld().spawnParticle(particleType,loc,amount,offsetX,offsetY,offsetZ,speed);
    }
}
