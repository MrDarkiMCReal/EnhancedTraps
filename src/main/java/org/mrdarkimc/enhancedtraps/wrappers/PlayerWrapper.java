package org.mrdarkimc.enhancedtraps.wrappers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mrdarkimc.SatanicLib.objectManager.interfaces.Reloadable;
import org.mrdarkimc.enhancedtraps.traps.skins.BlockSkinTrap;
import org.mrdarkimc.enhancedtraps.traps.CooldownHandler;
import org.mrdarkimc.enhancedtraps.traps.skins.ParticleSkin;
import org.mrdarkimc.enhancedtraps.traps.SkinHandler;

import java.util.Map;


public class PlayerWrapper implements Reloadable {

    Player player;
    BlockSkinTrap trapSkin = BlockSkinTrap.getDefault();
    ParticleSkin particleSkin = ParticleSkin.getDefault();
    public Player getPlayer(){
        return player;
    }

    public PlayerWrapper(Player player) {
        this.player = player;
        Reloadable.register(this);
        updateSkins();
    }
    public void useTrap(){
        ItemStack stack = player.getInventory().getItemInMainHand();
        CooldownHandler.setCooldown(player, trapSkin.getCooldown());
        stack.setAmount(stack.getAmount()-1);
        trapSkin.spawn(player);
        particleSkin.spawn(player);
    }
    public void calculateTrapSkin(){
        for (Map.Entry<String, BlockSkinTrap> trap : SkinHandler.trapList.entrySet()) {
            String trapname = trap.getKey();
            if (player.hasPermission("enhancedtraps." + trapname)){
                trapSkin = trap.getValue();
                return;
            }
        }
    }
    public void calculateParticleSkin(){
        for (Map.Entry<String, ParticleSkin> trap : SkinHandler.particleList.entrySet()) {
            String particlename = trap.getKey();
            if (player.hasPermission("enhancedtraps." + particlename)){
                particleSkin = trap.getValue();
                return;
            }
        }
    }
    public void updateSkins(){
        calculateParticleSkin();
        calculateTrapSkin();
    }


    @Override
    public void reload() {
        updateSkins();
    }
}
