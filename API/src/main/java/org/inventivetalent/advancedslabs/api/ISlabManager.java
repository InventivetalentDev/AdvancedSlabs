/*
 *
 */

package org.inventivetalent.advancedslabs.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;

import java.util.Set;
import java.util.UUID;

public interface ISlabManager {

	IAdvancedSlab createSlab(Location location, BlockData blockData);

	IAdvancedSlab createSlab(Location location, Material material);

	IAdvancedSlab createSlab(Location location, String data);

	@Deprecated
	IAdvancedSlab createSlab(Location location, Material material, byte data);

	void removeSlab(IAdvancedSlab slab);

	IAdvancedSlab getSlabForEntity(Entity entity);

	IAdvancedSlab getSlabForUUID(UUID uuid);

	Set<IAdvancedSlab> getSlabsForPath(ISlabPath path);

}
