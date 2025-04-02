package org.mrdarkimc.enhancedtraps.DomainExpansion;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.BlockTypeMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.factory.SphereRegionFactory;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;



public class Sphere{

    public void spawn(Player player, Location loc) {


        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        com.sk89q.worldedit.world.World adapted = BukkitAdapter.adapt(loc.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(adapted)){

            //this.playSoundToNearPlayers(player);
            //this.applyNegateEffectToEnemies(player);
            //owner.applyEffectTo(player);


            BlockType[] type = {BlockTypes.get("air")};
            BlockTypeMask mask = new BlockTypeMask(editSession, type);
            Mask reversed = Masks.negate(mask);
            editSession.setMask(reversed);


            BlockType[] removal = {BlockTypes.get("tall_grass"),
                    BlockTypes.get("short_grass"),
                    BlockTypes.get("grass"),
                    BlockTypes.get("dandelion"),
                    BlockTypes.get("azure_bluet"),
                    BlockTypes.get("corn_flower"),
                    BlockTypes.get("poppy"),
                    BlockTypes.get("oxeye_daisy"),
                    BlockTypes.get("rose_bush"),
                    //Бревна
                    BlockTypes.get("oak_log"),
                    BlockTypes.get("birch_log"),
                    BlockTypes.get("stripped_jungle_log"),
                    BlockTypes.get("acacia_log"),
                    BlockTypes.get("stripped_birch_log"),
                    BlockTypes.get("stripped_cherry_log"),
                    BlockTypes.get("cherry_log"),
                    BlockTypes.get("stripped_oak_log"),
                    BlockTypes.get("spruce_log"),
                    BlockTypes.get("stripped_spruce_log"),
                    BlockTypes.get("jungle_log"),
                    BlockTypes.get("stripped_acacia_log"),
                    BlockTypes.get("dark_oak_log"),
                    BlockTypes.get("stripped_dark_oak_log"),
                    BlockTypes.get("mangrove_log"),
                    BlockTypes.get("stripped_mangrove_log"),
                    //листья
                    BlockTypes.get("oak_leaves"),
                    BlockTypes.get("acacia_leaves"),
                    BlockTypes.get("dark_oak_leaves"),
                    BlockTypes.get("cherry_leaves"),
                    BlockTypes.get("flowering_azalea_leaves"),
                    BlockTypes.get("azalea_leaves"),
                    BlockTypes.get("mangrove_leaves"),
                    BlockTypes.get("jungle_leaves"),
                    BlockTypes.get("birch_leaves"),

            };
            BlockTypeMask removalMask = new BlockTypeMask(editSession, removal);
            new BukkitRunnable(){
                int i = 5;
                @Override
                public void run() {
                        Region region = new SphereRegionFactory().createCenteredAt(new BlockVector3((int)x,(int)y,(int)z),i);
                    try {
                        editSession.replaceBlocks(region,removalMask,BlockTypes.AIR.getDefaultState());
                        editSession.replaceBlocks(region,reversed, BlockTypes.SCULK.getDefaultState());
                    } catch (MaxChangedBlocksException e) {
                        throw new RuntimeException(e);
                    }
                    Operation op = editSession.commit();
                    try {
                        Operations.complete(op);
                    } catch (WorldEditException e) {
                        throw new RuntimeException(e);
                    }
                    playSoundToNearPlayers(player);
                    i = i+5;
                        if (i >30){
                            cancel();
                        }

                }
            }.runTaskTimer(EnhancedTraps.getInstance(),0,3);



//            BlockArrayClipboard clip = new BlockArrayClipboard(region);
//            Operation operation = new ClipboardHolder(clip)
//                    .createPaste(editSession)
//                    .to(BlockVector3.at(x, y, z))
//                    .ignoreAirBlocks(false)
//                    .build();

            editSession.close();

            new BukkitRunnable(){
                @Override
                public void run() {
                    editSession.undo(editSession);
                    player.getWorld().getNearbyEntities(player.getLocation(),40,40,40)
                            .stream()
                            .filter(e -> e instanceof Player)
                            .map(e -> (Player)e)
                            .forEach(p -> p.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK,1,1));
                }
            }.runTaskLater(EnhancedTraps.getInstance(), 35 * 20L); //35 сек время действия
        }
    }
    void playSoundToNearPlayers(Player player){
        player.getWorld().getNearbyEntities(player.getLocation(),45,45,45)
                .stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player)e)
                .forEach(p -> p.playSound(player.getLocation(), Sound.BLOCK_SCULK_SPREAD,1,1));
    }

}
