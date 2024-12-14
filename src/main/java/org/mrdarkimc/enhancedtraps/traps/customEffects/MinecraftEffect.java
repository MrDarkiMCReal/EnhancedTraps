package org.mrdarkimc.enhancedtraps.traps.customEffects;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinecraftEffect implements IEffect {
    public MinecraftEffect(String trapSkiName, IEffect.Target target) {
        deserialize(trapSkiName, target);
    }

    List<PotionEffect> potionEffects = new ArrayList<>();

    public void deserialize(String trapSkinName, IEffect.Target target) {
        potionEffects.clear();
        String path = "skins.blockSkins." + trapSkinName + ".custom-effects." + target.toString();
        List<String> ownerEffects = EnhancedTraps.config.get().getStringList(path);

        if (ownerEffects == null || ownerEffects.isEmpty()) {
            return;
        }

        for (String ownerEffect : ownerEffects) {
            String[] parts = ownerEffect.split(":");
            if (parts.length != 3) {
                EnhancedTraps.getInstance().getLogger().warning("[TRAPS] Неверный формат эффекта в конфиге: " + ownerEffect);
                continue;
            }

            PotionEffectType type = PotionEffectType.getByKey(NamespacedKey.fromString(parts[0].toLowerCase()));

            if (type == null) {
                throw new NoSuchElementException("[TRAPS] Внимание. Майнкрафт эффект: " + parts[0] + " не существует. (" + trapSkinName + ")");
            }

            int duration;
            int level;
            try {
                duration = Integer.parseInt(parts[1])*20;
                level = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                EnhancedTraps.getInstance().getLogger().warning("[TRAPS] Ошибка парсинга чисел для эффекта: " + ownerEffect);
                continue;
            }

            potionEffects.add(new PotionEffect(type, duration, level));
        }

    }

    @Override
    public void applyEffectTo(Player player) {
        potionEffects.forEach(player::addPotionEffect);
    }
}
