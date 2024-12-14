package org.mrdarkimc.enhancedtraps.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.SatanicLib.objectManager.interfaces.Reloadable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;
import org.mrdarkimc.enhancedtraps.wrappers.WrapperHandler;

public class Commands implements CommandExecutor {
    //WorldGuardHook hook = EnhancedTraps.hook;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("enhancedtraps.admin.permission")){
            return true;
        }
        if (strings.length < 1) {
            commandSender.sendMessage(Utils.translateHex("   "));
            commandSender.sendMessage(Utils.translateHex("                   &#D40092MrDarkiMC's EnhancedTraps"));
            commandSender.sendMessage(Utils.translateHex("   "));
            commandSender.sendMessage(Utils.translateHex("    &#D40092/" + command.getName() + " try&r&7 - заспавнить трапку"));
            commandSender.sendMessage(Utils.translateHex("    &#D40092/" + command.getName() + " give ник кол-во&r&7 - дать трапку игроку"));
            commandSender.sendMessage(Utils.translateHex("    &#D40092/" + command.getName() + " update&r&7 - обновить скины игроку"));
            commandSender.sendMessage(Utils.translateHex("    &#D40092/" + command.getName() + " reload&r&7 - перезагрузить кэш"));
            commandSender.sendMessage(Utils.translateHex("    &#D40092ET-canUseTrap&7 - флаг для WorldGuard"));
            commandSender.sendMessage(Utils.translateHex("    &7После выдачи игроку прав, нужно вызвать /et update Ник"));
            commandSender.sendMessage(Utils.translateHex("   "));
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
                    }else {
                        commandSender.sendMessage(ChatColor.RED + "[TRAPS] Игрок не найден");
                    }
                    commandSender.sendMessage(ChatColor.YELLOW +  "[TRAPS] Трапка выдана игроку: " + giveTo.getName());
                    return true;
            case "reload": // /et give mrdarkimc 1
                EnhancedTraps.config.reloadConfig();
                Reloadable.reloadAll();
                commandSender.sendMessage(ChatColor.YELLOW + "[TRAPS] Кэш конфига перезагружен");
                return true;
            case "update":
                Player updateMe = Bukkit.getPlayer(strings[1]);
                WrapperHandler.getWPlayer(updateMe).updateSkins();
                commandSender.sendMessage(ChatColor.YELLOW + "[Traps] Скины на трапки/партиклы у игрока обновлены согласно его правам.");
                return true;
        }
        return true;
    }
}
