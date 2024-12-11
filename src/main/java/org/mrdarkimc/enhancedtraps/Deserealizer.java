package org.mrdarkimc.enhancedtraps;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.mrdarkimc.enhancedtraps.hooks.WGSchemLoader;
import org.mrdarkimc.enhancedtraps.traps.BlockSkinTrap;
import org.mrdarkimc.enhancedtraps.traps.SkinHandler;

import java.util.Map;

public class Deserealizer {
    public void deserealizeBlockSkinTraps(){
        for (Map.Entry<String, Clipboard> nameWithExtension : WGSchemLoader.clipboardMap.entrySet()) {
                String name = nameWithExtension.getKey().replace(".schem","");
            SkinHandler.add(new BlockSkinTrap(name,nameWithExtension.getValue(),EnhancedTraps.config.get()
                    .getStringList("skins.blockSkins." + name +".contents")
                    .stream()
                    .map(String::toUpperCase)
                    .map(BlockTypes::get)
                    .toArray(BlockType[]::new)));
        }
    }
}
