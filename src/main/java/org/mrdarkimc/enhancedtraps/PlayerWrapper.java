package org.mrdarkimc.enhancedtraps;

import com.sk89q.worldedit.LocalSession;
import org.bukkit.entity.Player;


public class PlayerWrapper {

    Player player;
    public Player getPlayer(){
        return player;
    }

    public PlayerWrapper(Player player) {
        this.player = player;
        WrapperHandler.addPlayer(this);
    }
    LocalSession session;
    public void saveSession(LocalSession session){
        this.session = session;
    }

}
