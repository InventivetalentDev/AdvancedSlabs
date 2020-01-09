/*
 *
 */

package org.inventivetalent.advancedslabs.api;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Shulker;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.api.path.IPathPassenger;
import org.inventivetalent.advancedslabs.entity.ISlabFallingBlock;

import java.util.UUID;

public interface IAdvancedSlab extends IPathPassenger {

	@Deprecated
	MaterialData getMaterialData();

	BlockData getBlockData();

	UUID getShulkerUUID();

	UUID getArmorStandUUID();

	UUID getFallingBlockUUID();

	Shulker getShulker();

	ArmorStand getArmorStand();

	ISlabFallingBlock getFallingBlock();

	Location getLocation();

	void move(Location location);

	void moveRelative(Vector vector);

	boolean isDespawned();

	void refreshEntities();

	void respawnFallingBlock();

	UUID getOwner();

	void setOwner(UUID uuid);
}
