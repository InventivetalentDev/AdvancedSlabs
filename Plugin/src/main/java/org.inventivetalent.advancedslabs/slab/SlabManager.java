/*
 *
 */

package org.inventivetalent.advancedslabs.slab;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.Synchronized;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.ISlabManager;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class SlabManager implements ISlabManager {

	private AdvancedSlabs plugin;
	private final Set<IAdvancedSlab> slabs = new HashSet<>();

	public SlabManager(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	@Deprecated
	@Synchronized
	@Override
	public AdvancedSlab createSlab(Location location, Material material, byte data) {
		AdvancedSlab slab = new AdvancedSlab(location);
		slab.setMaterial(material, data);

		slabs.add(slab);
		return slab;
	}

	@Override
	public AdvancedSlab createSlab(Location location, BlockData blockData) {
		return createSlab(location, blockData.getAsString());
	}


	@Synchronized
	@Override
	public AdvancedSlab createSlab(Location location, Material material) {
		AdvancedSlab slab = new AdvancedSlab(location);
		slab.setMaterial(material);

		slab.getShulker().setCollidable(false);

		slabs.add(slab);
		return slab;
	}

	@Synchronized
	@Override
	public AdvancedSlab createSlab(Location location, String data) {
		AdvancedSlab slab = new AdvancedSlab(location);
		slab.setMaterial(data);

		slab.getShulker().setCollidable(false);

		slabs.add(slab);
		return slab;
	}

	@Synchronized
	@Override
	public void removeSlab(IAdvancedSlab slab) {
		slabs.remove(slab);
		((AdvancedSlab) slab).despawn();
	}

	@Override
	public IAdvancedSlab getSlabForEntity(Entity entity) {
		return getSlabForUUID(entity.getUniqueId());
	}

	@Synchronized
	@Override
	public IAdvancedSlab getSlabForUUID(UUID uuid) {
		for (IAdvancedSlab slab : slabs) {
			if (slab.getArmorStandUUID().equals(uuid) || slab.getShulkerUUID().equals(uuid) || slab.getFallingBlockUUID().equals(uuid)) {
				return slab;
			}
		}
		return null;
	}

	@Synchronized
	@Override
	public Set<IAdvancedSlab> getSlabsForPath(ISlabPath path) {
		Set<IAdvancedSlab> slabs = new HashSet<>();
		for (IAdvancedSlab slab : this.slabs) {
			if (slab.getPathId() == path.getId()) {
				slabs.add(slab);
			}
		}
		return slabs;
	}

	@Synchronized
	public JsonArray toJson() {
		JsonArray array = new JsonArray();
		for (IAdvancedSlab slab : slabs) {
			array.add(((AdvancedSlab) slab).toJson());
		}
		return array;
	}

	@Synchronized
	public void loadJson(JsonArray jsonArray) {
		for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext(); ) {
			JsonElement next = iterator.next();
			slabs.add(new AdvancedSlab(next.getAsJsonObject()));
		}
	}

	@Synchronized
	public Set<IAdvancedSlab> getSlabs() {
		return new HashSet<>(slabs);
	}

	@Synchronized
	public int size() {
		return slabs.size();
	}
}
