package org.mrdarkimc.enhancedtraps.traps.customEffects;

import org.bukkit.potion.PotionEffectType;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;

import java.util.List;
import java.util.NoSuchElementException;

public class CustomEffectHandler {
    //List<IEffect> effects = new ArrayList<>();
    public void deserealize(String trapSkiName, IEffect.Target target){
        //effects.clear();
        List<String> ownerEffects = EnhancedTraps.config.get().getStringList("skins.blockSkins." + trapSkiName + ".custom-effects." + target.toString());
        for (String effect : ownerEffects) {
            if (effect.startsWith("[effectType] ")){
                //create new MinecraftEffect
            }
            String[] parts = effect.split(":");
            PotionEffectType type = PotionEffectType.getByName(parts[0]);
            int duration = Integer.parseInt(parts[1]);
            int level = Integer.parseInt(parts[2]);
            if (type != null) {
                //effects.add(new MinecraftEffect() new PotionEffect(type, duration, level));
            }else {
                throw new NoSuchElementException("[TRAPS] Внимание. Майнкрафт эффект: " + parts[0] + " не существует. (" + trapSkiName + ")");
            }
        }
    }
}
