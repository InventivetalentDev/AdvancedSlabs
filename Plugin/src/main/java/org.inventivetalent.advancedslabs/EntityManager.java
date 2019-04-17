/*
 * Copyright 2015-2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
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
