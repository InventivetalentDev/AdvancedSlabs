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

package org.inventivetalent.advancedslabs.slab;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.movement.path.SlabPath;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class SlabManager {

	private AdvancedSlabs plugin;
	public final Set<AdvancedSlab> slabs = new HashSet<>();

	public SlabManager(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	public AdvancedSlab createSlab(Location location, Material material, byte data) {
		AdvancedSlab slab = new AdvancedSlab(location);
		slab.setMaterial(material, data);

		plugin.getCollisionTeam().addEntry(slab.getShulkerUUID().toString());

		slabs.add(slab);
		return slab;
	}

	public void removeSlab(AdvancedSlab slab) {
		slabs.remove(slab);
		slab.despawn();
	}

	public AdvancedSlab getSlabForEntity(Entity entity) {
		return getSlabForUUID(entity.getUniqueId());
	}

	public AdvancedSlab getSlabForUUID(UUID uuid) {
		for (AdvancedSlab slab : slabs) {
			if (slab.getArmorStandUUID().equals(uuid) || slab.getShulkerUUID().equals(uuid) || slab.getFallingBlockUUID().equals(uuid)) {
				return slab;
			}
		}
		return null;
	}

	public AdvancedSlab getSlabForPath(SlabPath path) {
		for (AdvancedSlab slab : slabs) {
			if (slab.path == path.id) {
				return slab;
			}
		}
		return null;
	}

	public JsonArray toJson() {
		JsonArray array = new JsonArray();
		for (AdvancedSlab slab : slabs) {
			array.add(slab.toJson());
		}
		return array;
	}

	public void loadJson(JsonArray jsonArray) {
		for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext(); ) {
			JsonElement next = iterator.next();
			slabs.add(new AdvancedSlab(next.getAsJsonObject()));
		}
	}

}
