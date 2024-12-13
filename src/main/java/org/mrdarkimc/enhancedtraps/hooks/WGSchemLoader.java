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
    }

    public static File schemFolder = new File(EnhancedTraps.getInstance().getDataFolder() + "/schems/");
    public static Map<String, Clipboard> clipboardMap = new HashMap<>();
    public void loadSchematicsToCache(){
        clipboardMap.clear();
        if (!schemFolder.exists()) {
            Bukkit.getLogger().warning("[TRAPS] Schems folder does not exist!");
            return;
        }
        if (!schemFolder.isDirectory()) {
            Bukkit.getLogger().warning("[TRAPS] Schems path is not a directory!");
            return;
        }

        File[] schemlist = schemFolder.listFiles();
        if (schemlist == null || schemlist.length == 0) {
            Bukkit.getLogger().warning("[TRAPS] No schematics found in schems folder.");
            return;
        }


        //File[] schemlist = this.schemFolder.listFiles();
        for (File schem : schemlist) {
            Bukkit.getLogger().info("[TRAPS] Loading schem: " + schem.getName());
            ClipboardFormat format = ClipboardFormats.findByFile(schem);
            try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
                clipboardMap.put(schem.getName(), reader.read());
                Bukkit.getLogger().info("[TRAPS] " + schem.getName() + " loaded.");
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
