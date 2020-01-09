/*
 *
 */

package org.inventivetalent.advancedslabs.entity;

import org.bukkit.Location;
import org.bukkit.Material;

public interface IEntitySpawner {

	ISlabFallingBlock spawnFallingBlock(Location location, Material material, String data);

	@Deprecated
	ISlabFallingBlock spawnFallingBlock(Location location, Material material, byte data);

}
