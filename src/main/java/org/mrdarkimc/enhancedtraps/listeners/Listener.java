package org.mrdarkimc.enhancedtraps.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicLib.objectManager.interfaces.Reloadable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;
import org.mrdarkimc.enhancedtraps.traps.CooldownHandler;
import org.mrdarkimc.enhancedtraps.traps.TrapFactory;
import org.mrdarkimc.enhancedtraps.wrappers.PlayerWrapper;
import org.mrdarkimc.enhancedtraps.wrappers.WrapperHandler;

import java.util.List;

public class Listener implements org.bukkit.event.Listener, Reloadable {
    public List<String> blockedWorlds;

    public Listener() {
        Reloadable.register(this);
        deserealize();
    }
    public void deserealize(){
        blockedWorlds = EnhancedTraps.config.get().getStringList("general.blockedWorlds");
    }

    @EventHandler
    void onjoin(PlayerJoinEvent e) {
        WrapperHandler.addPlayer(e.getPlayer());
    }

    @EventHandler
    void onquit(PlayerQuitEvent e) {
        WrapperHandler.removePlayer(e.getPlayer());
    }


    public boolean isAllowedToPlaceTrap(Player player) {
        World world = BukkitAdapter.adapt(player.getWorld());
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(world);
        Location loc = player.getLocation();
        boolean canPlace = true;
        if (blockedWorlds.contains(player.getWorld().getName())){
            canPlace = false;
            for (ProtectedRegion applicableRegion : manager.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))) {
                StateFlag.State flagState = applicableRegion.getFlag(EnhancedTraps.CAN_USE_TRAP);
                if (flagState.equals(StateFlag.State.ALLOW)) {
                    canPlace = true;
                    break;
                }
            }
            if (!canPlace){
                if (EnhancedTraps.config.get().getBoolean("messages.cant-Use-Trap-World.enable")) {
                    player.sendMessage(Utils.translateHex(EnhancedTraps.config.get().getString("messages.cant-Use-Trap-World.message")));
                }
                return false;
            }else return true;

        }
        for (ProtectedRegion applicableRegion : manager.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))) {
            StateFlag.State flagState = applicableRegion.getFlag(EnhancedTraps.CAN_USE_TRAP);
            if (flagState == StateFlag.State.DENY) {
                canPlace = false;
                break;
            }
            if (flagState.equals(StateFlag.State.ALLOW) || flagState == null) {
                return true;
            }

        }
        if (!canPlace) {
            if (EnhancedTraps.config.get().getBoolean("messages.cant-Use-Trap-Here.enable")) {
                player.sendMessage(Utils.translateHex(EnhancedTraps.config.get().getString("messages.cant-Use-Trap-Here.message")));
            }
        }
        return canPlace;
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            if (event.getHand().equals(EquipmentSlot.HAND)) {

                ItemStack stack = event.getPlayer().getInventory().getItemInMainHand();
                if (stack==null || !stack.getItemMeta().getPersistentDataContainer().has(TrapFactory.key, PersistentDataType.BOOLEAN))
                    return;


                PlayerWrapper wPlayer = WrapperHandler.getWPlayer(event.getPlayer());
                if (!isAllowedToPlaceTrap(wPlayer.getPlayer()))
                    return;

                if (CooldownHandler.hasCooldown(wPlayer.getPlayer())) {
                    if (EnhancedTraps.config.get().getBoolean("messages.cooldown.enable")) {
                        wPlayer.getPlayer().sendMessage(Utils.translateHex(EnhancedTraps.config.get().getString("messages.cooldown.message")
                                .replaceAll("\\{time}",String.valueOf(CooldownHandler.getTime(wPlayer.getPlayer())))));
                    }
                    return;
                }
                wPlayer.useTrap();
                //Все остальное в useTrap
            }
        }
    }


    @Override
    public void reload() {
        deserealize();
    }
}
