package org.mrdarkimc.enhancedtraps;

import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.SatanicLib.SatanicLib;
import org.mrdarkimc.SatanicLib.configsetups.Configs;
import org.mrdarkimc.enhancedtraps.hooks.WGSchemLoader;
import org.mrdarkimc.enhancedtraps.hooks.WorldGuardHook;
import org.mrdarkimc.enhancedtraps.traps.TrapFactory;

public final class EnhancedTraps extends JavaPlugin {
    private static EnhancedTraps instance;

    public static EnhancedTraps getInstance() {
        return instance;
    }
    public static WorldGuardHook hook;
    public static Configs config;

    @Override
    public void onEnable() {
        SatanicLib.setupLib(this);
        instance = this;
        config = Configs.Defaults.setupConfig();
        hook = new WorldGuardHook();
        //hook.loadSchematicsToCache();
        new WGSchemLoader();
        new Deserealizer().deserealizeBlockSkinTraps();
        new TrapFactory();
        getServer().getPluginCommand("et").setExecutor(new Commands());

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
