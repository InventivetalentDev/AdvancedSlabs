/*
 *
 */

package org.inventivetalent.advancedslabs.movement;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;
import org.inventivetalent.advancedslabs.movement.path.SlabPath;

public class PathMovementTask extends BukkitRunnable {

	private AdvancedSlabs plugin;

	public PathMovementTask(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().isEmpty()) { return; }
		for (IAdvancedSlab slab : plugin.slabManager.getSlabs()) {
			if (slab.getPathId() == -1) { continue; }
			ISlabPath path = plugin.pathManager.getPathById(slab.getPathId());
			if (path != null) {
				((SlabPath) path).tick();
			}
		}
	}
}
