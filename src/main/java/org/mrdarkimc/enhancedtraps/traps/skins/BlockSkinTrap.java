package org.mrdarkimc.enhancedtraps.traps.skins;

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
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.SatanicLib.Utils;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;
import org.mrdarkimc.enhancedtraps.traps.customEffects.MinecraftEffect;
import org.mrdarkimc.enhancedtraps.traps.customEffects.IEffect;

import java.util.*;
import java.util.stream.Stream;

public class BlockSkinTrap implements Skinable {
    String name;

    int cooldown;
    Sound soundSpawn;
    Sound soundEnd;
    IEffect owner;
    IEffect enemy;
    List<String> actions; //todo
    Clipboard clipboard;
    public static BlockType[] globalContents; // поле инициализируется в классе Deserealizer
    public int trapActiveTime; //todo deserealize

    public BlockSkinTrap(String name,Clipboard clipboard) {
        this.name = name;
        this.clipboard = clipboard;
        cooldown = EnhancedTraps.config.get().getInt("skins.blockSkins." + name + ".cooldown");
        soundSpawn = Sound.valueOf(EnhancedTraps.config.get().getString("skins.blockSkins." + name + ".sound.spawn").toUpperCase());
        soundEnd = Sound.valueOf(EnhancedTraps.config.get().getString("skins.blockSkins." + name + ".sound.end").toUpperCase());
        trapActiveTime = EnhancedTraps.config.get().getInt("skins.blockSkins." + name + ".trapActiveTime");
        if (trapActiveTime >= cooldown){
            Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] Время действия должно быть меньше, чем КД");
            Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] Устанавливаю новые значения на трапку: " + name);
            Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] КД:" + 45);
            Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] Время действия:" + 15);
            trapActiveTime = 15;
            cooldown = 45;
        }
        owner = new MinecraftEffect(name, IEffect.Target.owner);
        enemy = new MinecraftEffect(name, IEffect.Target.enemy);
    }
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Clipboard getSkin() {
        return clipboard;
    }

    public void createClaimZone(Location location){
        World world = BukkitAdapter.adapt(location.getWorld());
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        double minX = x-5;
        double minY = y-6;
        double minZ = z-5;


        //max
        double maxX = x+5;
        double maxY = y+4;
        double maxZ = z+5;
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(world);
        String regionName = "EnhancedTraps-" + UUID.randomUUID().toString().substring(0,8);
        ProtectedRegion region = new ProtectedCuboidRegion(regionName,BlockVector3.at(minX,minY,minZ),BlockVector3.at(maxX,maxY,maxZ));
        Set<String> allowedCommands = new HashSet<>(); //blocked all commands except //todo
        region.setFlag(Flags.ALLOWED_CMDS, allowedCommands);
        region.setFlag(Flags.BUILD, StateFlag.State.DENY);
        //region.setFlag(Flags.BLOCK_PLACE, StateFlag.State.DENY);
        region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
        region.setFlag(Flags.OTHER_EXPLOSION, StateFlag.State.DENY);
        region.setFlag(Flags.USE, StateFlag.State.ALLOW);
        region.setPriority(10);
        region.setFlag(EnhancedTraps.CAN_USE_TRAP, StateFlag.State.DENY);
        manager.addRegion(region);
        StringBuilder loc = new StringBuilder();
        loc.append(location.getWorld().getName());
        loc.append(" ");
        loc.append(String.valueOf((int)x));
        loc.append(" ");
        loc.append(((int)y));
        loc.append(" ");
        loc.append(((int)z));

        Bukkit.getLogger().warning("[TRAPS-LOG-CREATE] Делаю трапку: " + regionName);
        Bukkit.getLogger().warning("[TRAPS-LOG-CREATE] Локация: " + loc.toString());
        new BukkitRunnable(){

            @Override
            public void run() {
                manager.removeRegion(regionName);
                Bukkit.getLogger().warning("[TRAPS-LOG-DELETE] Трапка удалена успешно: " + regionName);
            }
        }.runTaskLater(EnhancedTraps.getInstance(), trapActiveTime * 20L);
    }

    @Override
    public void spawn(Player player) {
        //Clipboard clipboard = WGSchemLoader.clipboardMap.get(WGSchemLoader.schemFolder.listFiles()[0].getName());
        if (clipboard == null){
            if (EnhancedTraps.config.get().getBoolean("messages.no-trap-found.enable")) {
                player.sendMessage(Utils.translateHex(EnhancedTraps.config.get().getString("messages.no-trap-found.message")));
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()+1);
                //возвращаем трапку
            }
        }

        Location loc = player.getLocation();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        com.sk89q.worldedit.world.World adapted = BukkitAdapter.adapt(loc.getWorld());
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(adapted)){
            //

//            CuboidRegion region = getLocalRegion3x3ByLocation(loc);
//            BlockArrayClipboard originalClipboard = new BlockArrayClipboard(region);
//            originalClipboard.setOrigin(BlockVector3.at(x,y,z));
//
//            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
//                    adapted, region, originalClipboard, region.getMinimumPoint()
//            );
//            Operations.complete(forwardExtentCopy);


            //

            //BlockTypeMask mask2 = new BlockTypeMask(editSession,new BlockTypeExtended("minecraft:polished_blackstone",player.getUniqueId()), new BlockTypeExtended("minecraft:chest", player.getUniqueId()));
            //Mask reversed = Masks.negate(mask2);


            BlockType[] type = Stream.concat(Arrays.stream(globalContents), this.getDefaultTrapSkins()).toArray(BlockType[]::new);
            BlockTypeMask mask = new BlockTypeMask(editSession, type);
            Mask reversed = Masks.negate(mask);
            player.getWorld().getNearbyEntities(player.getLocation(),10,10,10)
                    .stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player)e)
                    .forEach(p -> p.playSound(player.getLocation(), soundSpawn,1,1));
            player.getWorld().getNearbyEntities(player.getLocation(),1.5,1.5,1.5)
                    .stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player)e)
                    .forEach(p -> {
                        if (!p.equals(player)){
                            enemy.applyEffectTo(p);
                        }
                    });
            owner.applyEffectTo(player);
            editSession.setMask(reversed);
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(x, y, z))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
            editSession.close();
            new BukkitRunnable(){
                @Override
                public void run() {
                    editSession.undo(editSession);
                    player.getWorld().getNearbyEntities(player.getLocation(),10,10,10)
                            .stream()
                            .filter(e -> e instanceof Player)
                            .map(e -> (Player)e)
                            .forEach(p -> p.playSound(player.getLocation(), soundEnd,1,1));
                }
            }.runTaskLater(EnhancedTraps.getInstance(), trapActiveTime * 20L);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }
        createClaimZone(player.getLocation());
    }
    public CuboidRegion getLocalRegion3x3ByLocation(Location loc){
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        //min
        double minX = x-2;
        double minY = y-1;
        double minZ = z-2;


        //max
        double maxX = x+2;
        double maxY = y+3;
        double maxZ = z+2;
        com.sk89q.worldedit.world.World world = BukkitAdapter.adapt(loc.getWorld());
        return new CuboidRegion(world,BlockVector3.at(minX,minY,minZ), BlockVector3.at(maxX,maxY,maxZ));
    }

}
