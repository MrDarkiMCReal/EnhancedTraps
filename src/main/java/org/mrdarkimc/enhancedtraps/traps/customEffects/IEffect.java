package org.mrdarkimc.enhancedtraps.traps.customEffects;

import org.bukkit.entity.Player;

public interface IEffect {
    enum Target {
        owner,enemy;
    }
    void applyEffectTo(Player player);
}
