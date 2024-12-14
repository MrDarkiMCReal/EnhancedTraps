package org.mrdarkimc.enhancedtraps.hooks;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import org.bukkit.Bukkit;
import org.mrdarkimc.SatanicLib.objectManager.interfaces.Reloadable;
import org.mrdarkimc.enhancedtraps.EnhancedTraps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WGSchemLoader implements Reloadable {
    public WGSchemLoader() {
        loadSchematicsToCache();
        Reloadable.register(this);
    }

    public static File schemFolder = new File(EnhancedTraps.getInstance().getDataFolder() + "/schems/");
    public static Map<String, Clipboard> clipboardMap = new HashMap<>(); //эта мапа содержит name + .schem
    public void loadSchematicsToCache(){
        clipboardMap.clear();
        if (!schemFolder.exists()) {
            Bukkit.getLogger().warning(" ");
            Bukkit.getLogger().warning("[TRAPS] Папки со схемой не обнаружено!");
            Bukkit.getLogger().warning("[TRAPS] Создаю папку!");

            try {
                schemFolder.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Bukkit.getLogger().warning("[TRAPS] Успешно");
            Bukkit.getLogger().warning("[TRAPS] Положите схемы в папку и /et reload");
            Bukkit.getLogger().warning(" ");
            return;
        }
        if (!schemFolder.isDirectory()) {
            Bukkit.getLogger().warning("[TRAPS] Путь должен быть /plugins/EnhancedTraps/schems/trap.schem");
            return;
        }

        File[] schemlist = schemFolder.listFiles();
        if (schemlist == null || schemlist.length == 0) {
            Bukkit.getLogger().warning("[TRAPS] Папка со схемами пуста. Ничего не было загружено в кэш");
            return;
        }


        //File[] schemlist = this.schemFolder.listFiles();
        for (File schem : schemlist) {
            Bukkit.getLogger().info("[TRAPS] Загружаю в кэш схему: " + schem.getName());
            ClipboardFormat format = ClipboardFormats.findByFile(schem);
            try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
                clipboardMap.put(schem.getName(), reader.read());
                Bukkit.getLogger().info("[TRAPS] " + schem.getName() + " загружена в кэш.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void reload() {
        loadSchematicsToCache();
    }
}
