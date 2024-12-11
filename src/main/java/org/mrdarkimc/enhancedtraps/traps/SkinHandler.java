package org.mrdarkimc.enhancedtraps.traps;

import java.util.HashMap;
import java.util.Map;

public class SkinHandler {
    public static Map<String,BlockSkinTrap> trapList = new HashMap<>();
    public static Map<String,ParticleSkin> particleList = new HashMap<>();
    public static void add(Skinable s){
        if (s instanceof BlockSkinTrap)
            trapList.put(s.getName(),(BlockSkinTrap) s);
        if (s instanceof ParticleSkin)
            particleList.put(s.getName(),(ParticleSkin) s);
    }
}
