package org.mrdarkimc.enhancedtraps.traps;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;

import java.util.List;

public class TrapFactory {
    public TrapFactory() {
        deserealize();
    }

    private ItemStack dontFuckingEverTouchMeBitch;
    private void deserealize(){
        ConfigurationSection section = EnhancedTraps.config.get().getConfigurationSection("trapItem");
        Material material = Material.valueOf(section.getString("id").toUpperCase());
        int customModelData = section.getInt("CMD");
        String displayname = Utils.translateHex(section.getString("displayname"));
        List<String> lore = section.getStringList("lore");
        lore.replaceAll(Utils::translateHex);
        dontFuckingEverTouchMeBitch = new ItemStack(material);
        ItemMeta meta = dontFuckingEverTouchMeBitch.getItemMeta();
        meta.setDisplayName(displayname);
        if (customModelData!=-1){
            meta.setCustomModelData(customModelData);
        }
        meta.setLore(lore);
        dontFuckingEverTouchMeBitch.setItemMeta(meta);
    }
    public ItemStack getTrap(int... amount){
        ItemStack stackToReturn = dontFuckingEverTouchMeBitch.clone();
        stackToReturn.setAmount(amount[0]);
        return stackToReturn;
    }
}
