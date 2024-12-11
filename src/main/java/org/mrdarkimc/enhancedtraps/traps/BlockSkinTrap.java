package org.mrdarkimc.enhancedtraps.traps;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BlockSkinTrap implements Skinable {
    String name;
    String sound; //todo
    List<String> actions; //todo
    Clipboard clipboard;
    BlockType[] contents;
    public BlockSkinTrap(String name,Clipboard clipboard, BlockType... materials) {
        this.name = name;
        this.clipboard = clipboard;
        this.contents = Stream.concat(Arrays.stream(materials),this.getDefaultTrapSkins())
                .toArray(BlockType[]::new);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Clipboard getSkin() {
        return clipboard;
    }

    @Override
    public void spawn(Player player) {
        //Clipboard clipboard = WGSchemLoader.clipboardMap.get(WGSchemLoader.schemFolder.listFiles()[0].getName());
        if (clipboard == null){
            player.sendMessage(ChatColor.RED + "[TRAPS] Не удалось определить скин на трапку.");
        }

        Location loc = player.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        com.sk89q.worldedit.world.World adapted = BukkitAdapter.adapt(loc.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(adapted)){
            BlockTypeMask mask = new BlockTypeMask(editSession, contents);
            Mask reversed = Masks.negate(mask);
            player.playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND,1,1);
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
        //todo sound & action
    }
    public static BlockSkinTrap getDefault(){
        return new BlockSkinTrap("pluginDefault",null);
    }


}
