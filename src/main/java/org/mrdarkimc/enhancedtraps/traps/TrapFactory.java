package org.mrdarkimc.enhancedtraps.traps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicLib.objectManager.interfaces.Reloadable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;

import java.nio.Buffer;
import java.util.List;

public class TrapFactory implements Reloadable {
    public TrapFactory() {
        deserealize();
        Reloadable.register(this);
    }
    public static NamespacedKey key = new NamespacedKey(EnhancedTraps.getInstance(),"customTrap");

    private ItemStack dontTouchMe;
    private void deserealize(){
        ConfigurationSection section = EnhancedTraps.config.get().getConfigurationSection("trapItem");
        Material material = Material.valueOf(section.getString("id").toUpperCase());
        int customModelData = section.getInt("CMD");
        String displayname = Utils.translateHex(section.getString("displayname"));
        List<String> lore = section.getStringList("lore");
        lore.replaceAll(Utils::translateHex);
        dontTouchMe = new ItemStack(material);
        ItemMeta meta = dontTouchMe.getItemMeta();
        meta.setDisplayName(displayname);
        if (customModelData!=-1){
            meta.setCustomModelData(customModelData);
        }
        meta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN,true);
        meta.setLore(lore);
        dontTouchMe.setItemMeta(meta);
    }
    public ItemStack getTrap(int amount){
        ItemStack stackToReturn = dontTouchMe.clone();
        stackToReturn.setAmount(amount);
        return stackToReturn;
    }

    @Override
    public void reload() {
        deserealize();
    }
}
