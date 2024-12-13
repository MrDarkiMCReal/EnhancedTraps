package org.mrdarkimc.enhancedtraps.commands;

import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.enginehub.linbus.stream.token.LinToken;
import org.mrdarkimc.SatanicLib.objectManager.interfaces.Reloadable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;
import org.mrdarkimc.enhancedtraps.hooks.WorldGuardHook;
import org.mrdarkimc.enhancedtraps.traps.TrapFactory;
import org.mrdarkimc.enhancedtraps.traps.skins.BlockSkinTrap;
import org.mrdarkimc.enhancedtraps.wrappers.WrapperHandler;

public class Commands implements CommandExecutor {
    //WorldGuardHook hook = EnhancedTraps.hook;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            commandSender.sendMessage("MrDarkiMC's EnhancedTraps");
            commandSender.sendMessage("Команды: ");
            commandSender.sendMessage("/" + command.getName() + " help - справка");
            return true;
        }
        switch (strings[0]) {
            case "try":
                if (commandSender instanceof Player player) {
                    WrapperHandler.getWPlayer(player).useTrap();
                    //new WorldGuardHook().pasteSchem(player,player.getLocation());
                    //hook.pasteSchem(player,player.getLocation());
                    return true;
                }
            case "give": // /et give mrdarkimc 1
                    int amount = 1;
                    if (strings.length > 2) {
                        amount = Integer.parseInt(strings[2]);
                    }
                    Player giveTo = Bukkit.getPlayer(strings[1]);
                    if (giveTo.isOnline()) {
                        ItemStack trap = EnhancedTraps.factory.getTrap(amount);
                        giveTo.getInventory().addItem(trap);
                    }
                    return true;
            case "reload": // /et give mrdarkimc 1
                EnhancedTraps.config.reloadConfig();
                Reloadable.reloadAll();
                return true;

        }
        return true;
    }
}
