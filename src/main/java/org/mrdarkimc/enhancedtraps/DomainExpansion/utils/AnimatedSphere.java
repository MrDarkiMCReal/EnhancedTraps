package org.mrdarkimc.enhancedtraps.DomainExpansion;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;


public class AnimatedSphere {
    Clipboard clipboard;

    public AnimatedSphere(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    public void spawn(Player player, Location loc) {
        //Clipboard clipboard = WGSchemLoader.clipboardMap.get(WGSchemLoader.schemFolder.listFiles()[0].getName());
        if (clipboard == null) {
            if (EnhancedTraps.config.get().getBoolean("messages.no-trap-found.enable")) {
                player.sendMessage(Utils.translateHex(EnhancedTraps.config.get().getString("messages.no-trap-found.message")));
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
                //возвращаем трапку
            }
        }


        com.sk89q.worldedit.world.World adapted = BukkitAdapter.adapt(loc.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(adapted)) {
            final BlockVector3 playerLocation = new BlockVector3(
                    (int) loc.getX(),
                    (int) loc.getY(),
                    (int) loc.getZ()
            );
            new HSphere(editSession,playerLocation,BlockTypes.SCULK.getDefaultState(), player).makeSphere();


            Operation op = editSession.commit();
            Operations.complete(op);

            editSession.close();

            new BukkitRunnable() {
                @Override
                public void run() {
                    editSession.undo(editSession);
                }
            }.runTaskLater(EnhancedTraps.getInstance(), (34 * 20L)); //todo 35 время действия и - 5 т.к должна удалиться первой
            //createClaimZone(player.getLocation()); //todo
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
    }
}
