package org.mrdarkimc.enhancedtraps.traps.skins;

import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Stream;

public interface Skinable {
    String getName();
    Object getSkin();
    void spawn(Player player);
    default Stream<BlockType> getDefaultTrapSkins(){
        return Arrays.stream(new BlockType[]{BlockTypes.CHEST,BlockTypes.SHULKER_BOX,BlockTypes.TRAPPED_CHEST,BlockTypes.BARREL, BlockTypes.ENDER_CHEST, BlockTypes.BEDROCK});
    }
}
