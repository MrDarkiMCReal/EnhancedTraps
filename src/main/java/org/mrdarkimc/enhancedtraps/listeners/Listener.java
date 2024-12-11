package org.mrdarkimc.enhancedtraps.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mrdarkimc.enhancedtraps.PlayerWrapper;
import org.mrdarkimc.enhancedtraps.WrapperHandler;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    void onjoin(PlayerJoinEvent e){
        new PlayerWrapper(e.getPlayer());
    }
    @EventHandler
    void onquit(PlayerQuitEvent e){
        WrapperHandler.removePlayer(WrapperHandler.getPlayer(e.getPlayer()));
    }

}
