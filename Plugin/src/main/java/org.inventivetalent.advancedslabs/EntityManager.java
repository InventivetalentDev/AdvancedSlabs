/*
 *
 */

package org.inventivetalent.advancedslabs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.inventivetalent.advancedslabs.entity.IEntitySpawner;
import org.inventivetalent.advancedslabs.entity.ISlabFallingBlock;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ClassResolver;

public class EntityManager {

	static ClassResolver classResolver = new ClassResolver();

	private AdvancedSlabs plugin;

	public final Class<?> EntitySpawnerClass;
	public final Class<?> FallingBlockClass;

	public IEntitySpawner spawner;

	public EntityManager(AdvancedSlabs plugin) {
		this.plugin = plugin;

		EntitySpawnerClass = classResolver.resolveSilent("org.inventivetalent.advancedslabs.entity.EntitySpawner_" + Minecraft.VERSION.name());
		FallingBlockClass = classResolver.resolveSilent("org.inventivetalent.advancedslabs.entity.SlabEntityFallingSand_" + Minecraft.VERSION.name());

		if (EntitySpawnerClass == null) {
			plugin.getLogger().severe("This plugin is currently not compatible with this server version (" + Minecraft.VERSION.name() + ")");
			throw new RuntimeException("Incompatible Server Version");
		}

		try {
			this.spawner = (IEntitySpawner) EntitySpawnerClass.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public ISlabFallingBlock spawnFallingBlock(Location location, BlockData blockData) {
		return this.spawner.spawnFallingBlock(location, blockData.getMaterial(), blockData.getAsString());
	}

	public ISlabFallingBlock spawnFallingBlock(Location location, Material material, String data) {
		return this.spawner.spawnFallingBlock(location, material, data);
	}

	@Deprecated
	public ISlabFallingBlock spawnFallingBlock(Location location, Material material, byte data) {
		return this.spawner.spawnFallingBlock(location, material, data);
	}

}
