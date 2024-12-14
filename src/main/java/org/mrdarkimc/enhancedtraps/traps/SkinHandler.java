package org.mrdarkimc.enhancedtraps.traps;

import org.mrdarkimc.enhancedtraps.traps.skins.BlockSkinTrap;
import org.mrdarkimc.enhancedtraps.traps.skins.ParticleSkin;
import org.mrdarkimc.enhancedtraps.traps.skins.Skinable;

import java.util.HashMap;
import java.util.Map;

public class SkinHandler {
    //todo
//    public static class BlockSkinPair{
//        public BlockSkinTrap trap;
//        public List<BlockType> contents;
//    }
    public static Map<String, BlockSkinTrap> trapList = new HashMap<>(); //эта мапа без .schem
    public static Map<String, ParticleSkin> particleList = new HashMap<>();
    public static void add(Skinable s){
        if (s instanceof BlockSkinTrap)
            trapList.put(s.getName(),(BlockSkinTrap) s);
        if (s instanceof ParticleSkin)
            particleList.put(s.getName(),(ParticleSkin) s);
    }
}
