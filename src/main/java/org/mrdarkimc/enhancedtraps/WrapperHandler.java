package org.mrdarkimc.enhancedtraps;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class WrapperHandler {
    private static List<PlayerWrapper> onlinePlayers = new ArrayList<>();
    public static void addPlayer(PlayerWrapper playerWrapper){
        onlinePlayers.add(playerWrapper);
    }
    public static void removePlayer(PlayerWrapper playerWrapper){
        onlinePlayers.remove(playerWrapper);
    }
    public static PlayerWrapper getPlayer(Player player){
        for (PlayerWrapper wrapper : onlinePlayers) {
            if (wrapper.getPlayer().equals(player)){
                return wrapper;
            }
        }
        return null;
    }
}
