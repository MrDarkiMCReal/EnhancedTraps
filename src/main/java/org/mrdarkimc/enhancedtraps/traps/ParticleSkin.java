package org.mrdarkimc.enhancedtraps.traps;

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
    int speed;

    public ParticleSkin(String name,Particle particleType, int amount, double offsetX, double offsetY, double offsetZ, int speed) {
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
        return null;
    }

    @Override
    public Object getSkin() {
        return this;
    }

    @Override
    public void spawn(Player player) {
        if (particleType == null){
            player.sendMessage(ChatColor.RED + "[TRAPS] Не удалось определить партиклы");
            return;
        }
        Location loc = player.getLocation();
        loc.getWorld().spawnParticle(particleType,loc,amount,offsetX,offsetY,offsetZ,speed);
    }
    public static ParticleSkin getDefault(){
        return new ParticleSkin("pluginDefault",null,0,0,0,0,0);
    }
    public static class EmptySkin implements Skinable{

        @Override
        public String getName() {
            return "pluginDefault";
        }

        @Override
        public Object getSkin() {
            return this;
        }

        @Override
        public void spawn(Player player) {

        }
    }
}
