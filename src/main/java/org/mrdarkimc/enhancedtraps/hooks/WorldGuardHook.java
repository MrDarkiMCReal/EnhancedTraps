package org.mrdarkimc.enhancedtraps.hooks;



import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import org.mrdarkimc.enhancedtraps.EnhancedTraps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class WorldGuardHook {

    public void action(){
        //Copy cuboid by location or coords
        //cut this region
        //paste trap
        //claim location
    }
    public CuboidRegion getLocalRegion3x3ByLocation(Location loc){
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        //min
        double minX = x-2;
        double minZ = z-2;
        double minY = y-1;


        //max
        double maxX = x+2;
        double maxY = y+3;
        double maxZ = z+2;
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(loc.getWorld());
        return new CuboidRegion(world,BlockVector3.at(minX,minY,minZ), BlockVector3.at(maxX,maxY,maxZ));
    }

//    public void saveLocation(Location loc) {
//        Bukkit.getLogger().info("saving location");
//
//        // Получаем область вокруг местоположения
//        CuboidRegion region = getLocalRegion3x3ByLocation(loc);
//
//        // Создаем маску для исключения сундуков и шалкер-боксов
//        BlockMask mask = new BlockMask();
//        mask.add(BlockTypes.CHEST);
//        mask.add(BlockTypes.SHULKER_BOX);
//        InverseMask inverseMask = new InverseMask(mask);
//        ///
//
//
//        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
//        com.sk89q.worldedit.world.World adapted = BukkitAdapter.adapt(loc.getWorld());
//        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
//                adapted, region, clipboard, region.getMinimumPoint()
//        );
//        forwardExtentCopy.setSourceMask(mask);
//// configure here
//        Operations.complete(forwardExtentCopy);
//    }

//
//        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
//
//            // Выполняем замену блоков на воздух в области, исключая сундуки и шалкер-боксы
//            editSession.replaceBlocks(region, mask, BlockTypes.AIR);
//
//            // Создаем Clipboard (буфер для копирования блоков)
//            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
//
//            // Сохраняем текущие блоки в clipboard (копируем)
//
//            // Получаем координаты для вставки
//            double x = loc.getX();
//            double y = loc.getY();
//            double z = loc.getZ();
//
//            // Создаем операцию для вставки
//            Operation operation = new ClipboardHolder(clipboard)
//                    .createPaste(editSession)    // Указываем, что это вставка
//                    .maskSource(mask)            // Применяем маску для вставки
//                    .to(BlockVector3.at(x, y+5, z)) // Устанавливаем целевое местоположение
//                    .build();
//
//            // Выполняем операцию
//            Operations.complete(operation);
//            Bukkit.getLogger().info("Location saved and pasted.");
//        } catch (WorldEditException e) {
//            e.printStackTrace();
//            Bukkit.getLogger().warning("Error while saving location.");
//        }
//    }



    public void pasteSchem(Player player, Location loc){
        Clipboard clipboard = WGSchemLoader.clipboardMap.get(WGSchemLoader.schemFolder.listFiles()[0].getName());
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        com.sk89q.worldedit.world.World adapted = BukkitAdapter.adapt(loc.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(adapted)){
            BlockTypeMask mask = new BlockTypeMask(editSession, BlockTypes.CHEST,BlockTypes.SHULKER_BOX, BlockTypes.GLASS);
            Mask reversed = Masks.negate(mask);
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND,1,1);
            //editSession.setMask(inverseMask);
            editSession.setMask(reversed);
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(x, y, z))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
            new BukkitRunnable(){

                @Override
                public void run() {
                    editSession.undo(editSession);
                    player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND,1,1);
                }
            }.runTaskLater(EnhancedTraps.getInstance(),300);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }



//            new BlockRemoval(editSession).runTaskLater(EnhancedTraps.getInstance(),300);
//            localSession.remember(editSession);
    }
}
