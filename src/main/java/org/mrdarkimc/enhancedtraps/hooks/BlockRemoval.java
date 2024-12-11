package org.mrdarkimc.enhancedtraps.hooks;

import com.sk89q.worldedit.EditSession;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockRemoval extends BukkitRunnable{
    EditSession session;
    public BlockRemoval(EditSession session) {
        this.session = session;
    }

    @Override
    public void run() {
        session.undo(session);
        Bukkit.getLogger().info("Undo complite");
    }
}
