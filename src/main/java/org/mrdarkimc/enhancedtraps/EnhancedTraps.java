package org.mrdarkimc.enhancedtraps;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.mrdarkimc.SatanicLib.SatanicLib;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicLib.configsetups.Configs;
import org.mrdarkimc.enhancedtraps.commands.Commands;
import org.mrdarkimc.enhancedtraps.hooks.WGSchemLoader;
import org.mrdarkimc.enhancedtraps.hooks.WorldGuardHook;
import org.mrdarkimc.enhancedtraps.listeners.Listener;
import org.mrdarkimc.enhancedtraps.traps.TrapFactory;

public final class EnhancedTraps extends JavaPlugin {
    private static EnhancedTraps instance;

    public static EnhancedTraps getInstance() {
        return instance;
    }
    //public static WorldGuardHook hook;
    public static Configs config;
    public static TrapFactory factory;

    public static StateFlag CAN_USE_TRAP;

    @Override
    public void onLoad() {

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // create a flag with the name "my-custom-flag", defaulting to true
            StateFlag flag = new StateFlag("ET-canUseTrap", false);
            registry.register(flag);
            CAN_USE_TRAP = flag; // only set our field if there was no error
        } catch (FlagConflictException e) {
            Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] Почему то существует флаг ET-canUseTrap. Это как?");}
//            // some other plugin registered a flag by the same name already.
//            // you can use the existing flag, but this may cause conflicts - be sure to check type
//            Flag<?> existing = registry.get("ET-canUseTrap");
//            if (existing instanceof StateFlag) {
//                MY_CUSTOM_FLAG = (StateFlag) existing;
//            } else {
//                // types don't match - this is bad news! some other plugin conflicts with you
//                // hopefully this never actually happens
//            }
        }


    @Override
    public void onEnable() {
        SatanicLib.setupLib(this);
        instance = this;
        config = Configs.Defaults.setupConfig();
        Utils.startUp("EnhancedTraps Premium");
        //hook = new WorldGuardHook(); //for removal
        //hook.loadSchematicsToCache();
        new WGSchemLoader();
        new Deserealizer().deserealize();
        factory = new TrapFactory();
        getServer().getPluginCommand("et").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new Listener(),this);

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
