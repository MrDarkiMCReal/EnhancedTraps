package org.mrdarkimc.enhancedtraps.traps;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;
import java.util.HashMap;
import java.util.Map;

public class CooldownHandler {
    static int cooldowntime = 45;
    private static Map<Player,Long> cooldowns = new HashMap<>(); //player and its time to being un cooldowned
    public static boolean hasCooldown(Player player){
        return cooldowns.containsKey(player);
    }
    public static void setCooldown(Player player){
        cooldowns.put(player,System.currentTimeMillis() + (cooldowntime*1000L));
        new BukkitRunnable(){
            @Override
            public void run() {
                cooldowns.remove(player);
            }
        }.runTaskLaterAsynchronously(EnhancedTraps.getInstance(),cooldowntime * 20L);
    }
public static long getTime(Player player){
    long current = System.currentTimeMillis();
    long unCooldownTime = cooldowns.get(player);
    return ((unCooldownTime-current)/1000);
}
}
