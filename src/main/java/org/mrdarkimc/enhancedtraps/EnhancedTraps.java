package org.mrdarkimc.enhancedtraps;

import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.enhancedtraps.hooks.WGSchemLoader;
import org.mrdarkimc.enhancedtraps.hooks.WorldGuardHook;

public final class EnhancedTraps extends JavaPlugin {
    private static EnhancedTraps instance;

    public static EnhancedTraps getInstance() {
        return instance;
    }
    public static WorldGuardHook hook;

    @Override
    public void onEnable() {
        instance = this;
        hook = new WorldGuardHook();
        //hook.loadSchematicsToCache();
        new WGSchemLoader();
        getServer().getPluginCommand("et").setExecutor(new Commands());

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
