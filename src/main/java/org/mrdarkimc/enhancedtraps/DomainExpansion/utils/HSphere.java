package org.mrdarkimc.enhancedtraps.DomainExpansion;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mrdarkimc.SatanicLib.ItemStackUtils.EnchantHandler;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;

public class HSphere {
    public HSphere(EditSession session, BlockVector3 pos, Pattern block, Player player) {
        this.session = session;
        this.pos = new BlockVector3(pos.x(), pos.y()-8, pos.z());
        this.block = block;
        this.player = player;
    }

    int affected = 0;
    double radiusX = 30 + 0.5;
    double radiusY = 30 + 0.5;
    double radiusZ = 30 + 0.5;
    double invRadiusX = 1.0 / radiusX;
    double invRadiusY = 1.0 / radiusY;
    double invRadiusZ = 1.0 / radiusZ;
    int ceilRadiusX = (int) Math.ceil(radiusX);
    int ceilRadiusY = (int) Math.ceil(radiusY);
    int ceilRadiusZ = (int) Math.ceil(radiusZ);
    double nextXn = 0.0;
    boolean filled = false;


    EditSession session;
    BlockVector3 pos;
    Pattern block;
    Player player;
    //todo на будущее сделать мапу, где ключ - высота постройки, а значение - список координат блоков.
    //todo таким образом мы кешируем координаты, и когда нам нужно вставить, просто итерируемся по списку и с помощью метода BlockVector.add добавляем коорды игрока к постройке
    //todo и экономим время выполнения

    public int makeSphere() throws MaxChangedBlocksException {

        new BukkitRunnable() {
            int x = 0;


            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {

                    double xn = nextXn;
                    nextXn = (double) (x + 1) * invRadiusX;
                    double nextYn = 0.0;
                    for (int y = 0; y <= ceilRadiusY; ++y) {
                        double yn = nextYn;
                        nextYn = (double) (y + 1) * invRadiusY;
                        double nextZn = 0.0;

                        for (int z = 0; z <= ceilRadiusZ; ++z) {
                            double zn = nextZn;
                            nextZn = (double) (z + 1) * invRadiusZ;
                            double distanceSq = lengthSq(xn, yn, zn);
                            if (distanceSq > 1.0) {
                                if (z == 0) {
                                    if (y == 0) {
                                        return;
                                    }
                                    continue;
                                }
                                break;
                            }
                            try {
                                if (filled || !(lengthSq(nextXn, yn, zn) <= 1.0) || !(lengthSq(xn, nextYn, zn) <= 1.0) || !(lengthSq(xn, yn, nextZn) <= 1.0)) {
                                    if (session.setBlock(pos.add(x, y, z), block)) {
                                        ++affected;
                                    }

                                    if (session.setBlock(pos.add(-x, y, z), block)) {
                                        ++affected;
                                    }

                                    if (session.setBlock(pos.add(x, -y, z), block)) {
                                        ++affected;
                                    }

                                    if (session.setBlock(pos.add(x, y, -z), block)) {
                                        ++affected;
                                    }

                                    if (session.setBlock(pos.add(-x, -y, z), block)) {
                                        ++affected;
                                    }

                                    if (session.setBlock(pos.add(x, -y, -z), block)) {
                                        ++affected;
                                    }

                                    if (session.setBlock(pos.add(-x, y, -z), block)) {
                                        ++affected;
                                    }

                                    if (session.setBlock(pos.add(-x, -y, -z), block)) {
                                        ++affected;
                                    }
                                }
                            } catch (MaxChangedBlocksException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    playSoundToNearPlayers(player, Sound.BLOCK_SCULK_SPREAD);
                    var v = session.commit();
                    try {
                        Operations.complete(v);
                    } catch (WorldEditException e) {
                        throw new RuntimeException(e);
                    }
                    ++x;
                    if (x >= ceilRadiusX - 5) {
                        try {
                            session.makeSphere(pos, BlockTypes.OBSIDIAN.getDefaultState(), 31, false);
                            session.makeSphere(pos, BlockTypes.OBSIDIAN.getDefaultState(), 32, false);
                        } catch (MaxChangedBlocksException e) {
                            e.printStackTrace();
                        }
                    }
                    if (x >= ceilRadiusX) {
                        cancel();
                        playSoundToNearPlayers(player, Sound.ENTITY_WITHER_BREAK_BLOCK);

                    }

                }
            }
        }.runTaskTimer(EnhancedTraps.getInstance(), 0, 3L);
//        for (int x = 0; x <= ceilRadiusX; ++x) {
//            double xn = nextXn;
//            nextXn = (double) (x + 1) * invRadiusX;
//            double nextYn = 0.0;
//
//            for (int y = 0; y <= ceilRadiusY; ++y) {
//                double yn = nextYn;
//                nextYn = (double) (y + 1) * invRadiusY;
//                double nextZn = 0.0;
//
//                for (int z = 0; z <= ceilRadiusZ; ++z) {
//                    double zn = nextZn;
//                    nextZn = (double) (z + 1) * invRadiusZ;
//                    double distanceSq = lengthSq(xn, yn, zn);
//                    if (distanceSq > 1.0) {
//                        if (z == 0) {
//                            if (y == 0) {
//                                return affected;
//                            }
//                            continue;
//                        }
//                        break;
//                    }
//
//                    if (filled || !(lengthSq(nextXn, yn, zn) <= 1.0) || !(lengthSq(xn, nextYn, zn) <= 1.0) || !(lengthSq(xn, yn, nextZn) <= 1.0)) {
//                        if (session.setBlock(pos.add(x, y, z), block)) {
//                            ++affected;
//                        }
//
//                        if (session.setBlock(pos.add(-x, y, z), block)) {
//                            ++affected;
//                        }
//
//                        if (session.setBlock(pos.add(x, -y, z), block)) {
//                            ++affected;
//                        }
//
//                        if (session.setBlock(pos.add(x, y, -z), block)) {
//                            ++affected;
//                        }
//
//                        if (session.setBlock(pos.add(-x, -y, z), block)) {
//                            ++affected;
//                        }
//
//                        if (session.setBlock(pos.add(x, -y, -z), block)) {
//                            ++affected;
//                        }
//
//                        if (session.setBlock(pos.add(-x, y, -z), block)) {
//                            ++affected;
//                        }
//
//                        if (session.setBlock(pos.add(-x, -y, -z), block)) {
//                            ++affected;
//                        }
//                    }
//                }
//            }
//        }

        return affected;
    }

    private static double lengthSq(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    void playSoundToNearPlayers(Player player, Sound sound) {
        player.getWorld().getNearbyEntities(player.getLocation(), 45, 45, 45)
                .stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .forEach(p -> p.playSound(player.getLocation(), sound, 0.65F, 1));
    }
}
