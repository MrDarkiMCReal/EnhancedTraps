package org.mrdarkimc.enhancedtraps;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.mrdarkimc.SatanicLib.objectManager.interfaces.Reloadable;
import org.mrdarkimc.enhancedtraps.hooks.WGSchemLoader;
import org.mrdarkimc.enhancedtraps.traps.skins.BlockSkinTrap;
import org.mrdarkimc.enhancedtraps.traps.SkinHandler;
import org.mrdarkimc.enhancedtraps.traps.skins.ParticleSkin;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.*;

public class Deserealizer implements Reloadable {
    public Deserealizer() {
        Reloadable.register(this);
    }

    public void deserealize(){
        deserealizeBlockSkinTraps();
        deserealizeParticleSkins();
    }
    private void deserealizeBlockSkinTraps() {
        SkinHandler.trapList.clear();


        Set<String> tags = EnhancedTraps.config.get().getConfigurationSection("skins.blockSkins").getKeys(false);


        List<BlockType> strContents = new ArrayList<>();
        for (Map.Entry<String, Clipboard> nameWithExtension : WGSchemLoader.clipboardMap.entrySet()) {
            Bukkit.getLogger().info("[TRAPS] Создаю скин: " + nameWithExtension.getKey());
            String name = nameWithExtension.getKey().replace(".schematics", "");
            name = name.replace(".schem", "");
            if (tags.contains(name)){
                Clipboard clipboard = nameWithExtension.getValue();

                List<BlockType> skinContents = EnhancedTraps.config.get()
                        .getStringList("skins.blockSkins." + name + ".contents")
                        .stream()
                        .map(String::toLowerCase)
                        .map(BlockTypes::get)
                        .toList();
                strContents.addAll(skinContents);

                SkinHandler.add(new BlockSkinTrap(name, clipboard));
                Bukkit.getLogger().info("[TRAPS] Скин создан успешно: " + nameWithExtension.getKey());
            }else {
                Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] Схема: " + nameWithExtension.getKey());
                Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] Не была загружена в кеш. Т.к в конфиге не указан скин с таким названием");
                Bukkit.getLogger().info(ChatColor.RED + "[TRAPS] Название скина в конфиге должно быть: " + name);

            }


        }
        BlockSkinTrap.globalContents = strContents.toArray(BlockType[]::new);
    }
    private void deserealizeParticleSkins(){
        SkinHandler.particleList.clear();
        FileConfiguration config = EnhancedTraps.config.get();
        ConfigurationSection section = config.getConfigurationSection("skins.particleSkins");
        Set<String> tags = section.getKeys(false);
        for (String tag : tags) {
            Particle particle = Particle.valueOf(section.getString(tag + ".particle_type").toUpperCase());

            int amount = section.getInt(tag + ".amount");
            double offsetX = section.getDouble(tag + ".offsetX");
            double offsetY = section.getDouble(tag + ".offsetY");
            double offsetZ = section.getDouble(tag + ".offsetZ");
            double speed = section.getDouble(tag + ".speed");;
           SkinHandler.add(new ParticleSkin(tag, particle,amount,offsetX,offsetY,offsetZ,speed));
           Bukkit.getLogger().info(ChatColor.YELLOW + "[TRAPS] Зарегистрирован партикл: " + tag);
        }
        Bukkit.getLogger().info(ChatColor.YELLOW + "[TRAPS] Зарегистрировано " + tags.size() + "  партиклов ");
    }

    @Override
    public void reload() {
        deserealize();
    }
}
