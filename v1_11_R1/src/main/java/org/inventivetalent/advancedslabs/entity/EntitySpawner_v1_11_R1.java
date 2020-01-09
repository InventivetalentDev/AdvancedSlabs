/*
 *
 */

package org.inventivetalent.advancedslabs.entity;

import net.minecraft.server.v1_11_R1.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EntitySpawner_v1_11_R1 implements IEntitySpawner {

	@Override
	public ISlabFallingBlock spawnFallingBlock(Location location, Material material, String data) {
		SlabEntityFallingSand_v1_11_R1 entity = new SlabEntityFallingSand_v1_11_R1(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), Block.getById(material.getId()).getBlockData());
		((CraftWorld) location.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
		return entity;
	}

	public ISlabFallingBlock spawnFallingBlock(Location location, Material material, byte data) {
		SlabEntityFallingSand_v1_11_R1 entity = new SlabEntityFallingSand_v1_11_R1(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ(), Block.getById(material.getId()).fromLegacyData(data));
		((CraftWorld) location.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
		return entity;
	}

}
