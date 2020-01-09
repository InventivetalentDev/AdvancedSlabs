/*
 *
 */

package org.inventivetalent.advancedslabs.slab;

import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;

public class FallingBlockResetTask extends BukkitRunnable {

	private AdvancedSlabs plugin;

	public FallingBlockResetTask(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (IAdvancedSlab slab : plugin.slabManager.getSlabs()) {
			if (slab == null) {
				continue;
			}
			if (slab.getFallingBlock() == null || slab.getFallingBlock().isDead()) {
				slab.respawnFallingBlock();
			}
		}
	}
}
