package org.mrdarkimc.enhancedtraps.listeners;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;
import org.mrdarkimc.enhancedtraps.traps.CooldownHandler;
import org.mrdarkimc.enhancedtraps.wrappers.PlayerWrapper;
import org.mrdarkimc.enhancedtraps.wrappers.WrapperHandler;

import java.util.ArrayList;
import java.util.List;

public class Listener implements org.bukkit.event.Listener {
    public List<String> blockedWorlds;

    public Listener() {
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

    public boolean isAllowedToPlaceTrap() {
        //logic to define world guard region
        return true;
    }

    NamespacedKey key = new NamespacedKey(EnhancedTraps.getInstance(), "enhancedTrap");

    @EventHandler
    void onInteract(PlayerInteractEvent event) {
        if (!event.getHand().equals(EquipmentSlot.HAND))
            return;
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BOOLEAN))
            return;

        PlayerWrapper wPlayer = WrapperHandler.getWPlayer(event.getPlayer());
        if (blockedWorlds.contains(wPlayer.getPlayer().getWorld().getName())) {
            if (!isAllowedToPlaceTrap()) {
                return;
            }
        }
        if (CooldownHandler.hasCooldown(wPlayer.getPlayer())) {
            wPlayer.getPlayer().sendMessage(ChatColor.RED + "[TRAPS] Кулдаун: " + CooldownHandler.getTime(wPlayer.getPlayer()) + " сек.");
            return;
        }
        wPlayer.useTrap();
    }


}
