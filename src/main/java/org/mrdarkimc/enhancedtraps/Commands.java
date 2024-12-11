package org.mrdarkimc.enhancedtraps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mrdarkimc.enhancedtraps.hooks.WorldGuardHook;

public class Commands implements CommandExecutor {
    WorldGuardHook hook = EnhancedTraps.hook;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            if (strings.length < 1) {
                commandSender.sendMessage("MrDarkiMC's EnhancedTraps");
                commandSender.sendMessage("Команды: ");
                commandSender.sendMessage("/" + command.getName() + " help - справка");
                return true;
            }
            switch (strings[0]){
                case "try":
                    if (commandSender instanceof Player player) {
                        new WorldGuardHook().pasteSchem(player,player.getLocation());
                        //hook.pasteSchem(player,player.getLocation());
                        return true;
                    }
            }
            return true;
        }
}
