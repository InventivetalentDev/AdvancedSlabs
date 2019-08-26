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

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.EntityHelper;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.path.IPathPassenger;
import org.inventivetalent.advancedslabs.entity.ISlabFallingBlock;
import org.inventivetalent.reflection.minecraft.Minecraft;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class AdvancedSlab implements IPathPassenger, IAdvancedSlab {

	private final UUID     armorStandUUID;
	private final UUID     shulkerUUID;
	private       UUID     fallingBlockUUID;
	private       Location location;

	@Deprecated
	private MaterialData materialData;

	private BlockData blockData;

	private ArmorStand        armorStand;
	private Shulker           shulker;
	private ISlabFallingBlock fallingBlock;

	public UUID owner;

	public int path           = -1;//-1 = no path
	public int pathPointIndex = 0;

	public boolean despawned = false;

	public AdvancedSlab(ArmorStand armorStand, Shulker shulker, FallingBlock fallingBlock) {
		this.armorStandUUID = armorStand.getUniqueId();
		this.shulkerUUID = shulker.getUniqueId();
		this.fallingBlockUUID = fallingBlock.getUniqueId();
		this.location = shulker.getLocation();
	}

	public AdvancedSlab(Location location) {
		location.setYaw(0);
		location.setPitch(0);

		this.location = location;
		AdvancedSlabs.instance.spawningSlab = true;
		this.armorStandUUID = (this.armorStand = location.getWorld().spawn(location, ArmorStand.class)).getUniqueId();
		this.shulkerUUID = (this.shulker = location.getWorld().spawn(location, Shulker.class)).getUniqueId();
		AdvancedSlabs.instance.spawningSlab = false;

		this.armorStand.setGravity(false);
		this.armorStand.setVisible(false);
		this.armorStand.setCustomName("advancedslab");
		this.armorStand.setCustomNameVisible(false);
		this.armorStand.setMarker(true);
		EntityHelper.setInvulnerable(getArmorStand());

		this.shulker.setCustomName("advancedslab");
		this.shulker.setCustomNameVisible(false);
		this.shulker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
		this.shulker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 120, false, false));// Just a little backup in case the invulnerability fails again...
		EntityHelper.setNoAI(getShulker());
		EntityHelper.clearEntityGoals(getShulker());
		EntityHelper.makeSilent(getShulker());
		EntityHelper.setInvulnerable(getShulker());

		EntityHelper.addPassenger(getArmorStand(), getShulker());
	}

	public AdvancedSlab(JsonObject jsonObject) {
		JsonObject locationObject = jsonObject.get("location").getAsJsonObject();
		this.location = new Location(Bukkit.getWorld(locationObject.get("world").getAsString()), locationObject.get("x").getAsDouble(), locationObject.get("y").getAsDouble(), locationObject.get("z").getAsDouble());
		JsonPrimitive dataElement = jsonObject.get("data").getAsJsonPrimitive();
		if (dataElement.isNumber()) {
			this.materialData = new MaterialData(Material.valueOf(jsonObject.get("material").getAsString()), jsonObject.get("data").getAsByte());
			this.blockData = Bukkit.getServer().createBlockData(Material.valueOf(jsonObject.get("material").getAsString()));
		} else {
			this.blockData = Bukkit.getServer().createBlockData(jsonObject.get("data").getAsString());
		}
		this.armorStandUUID = UUID.fromString(jsonObject.get("armorstand").getAsString());
		this.shulkerUUID = UUID.fromString(jsonObject.get("shulker").getAsString());
		this.fallingBlockUUID = UUID.fromString(jsonObject.get("fallingblock").getAsString());

		if (AdvancedSlabs.instance.getConfig().getBoolean("slabOwners") && jsonObject.has("owner")) {
			try {
				this.owner = UUID.fromString(jsonObject.get("owner").getAsString());
			} catch (Exception e) {
				AdvancedSlabs.instance.getLogger().log(Level.WARNING, "Invalid owner", e);
			}
		}
		if (jsonObject.has("path")) {
			this.path = jsonObject.get("path").getAsInt();
		}

		refreshEntities();
		reStackEntities();
	}

	@Deprecated
	public void setMaterial(Material material, byte data) {
		if (getFallingBlock() != null) {
			getFallingBlock().stopRiding();
			getFallingBlock().allowDeath();
			getFallingBlock().remove();
		}

		this.materialData = new MaterialData(material, data);
		this.fallingBlockUUID = (this.fallingBlock = spawnFallingBlock(this.location, material, data)).getUniqueId();
		this.fallingBlock.setTicksLived(2);
		this.fallingBlock.setDropItem(false);
		this.fallingBlock.setCustomName("advancedslab");
		this.fallingBlock.setCustomNameVisible(false);

		if (getArmorStand() != null) {
			try {
				getFallingBlock().setRiding(Minecraft.getHandle(getArmorStand()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void setMaterial(Material material) {
		if (getFallingBlock() != null) {
			getFallingBlock().stopRiding();
			getFallingBlock().allowDeath();
			getFallingBlock().remove();
		}

		this.blockData = Bukkit.getServer().createBlockData(material);
		this.materialData = new MaterialData(material);
		this.fallingBlockUUID = (this.fallingBlock = spawnFallingBlock(this.location, material, "")).getUniqueId();
		this.fallingBlock.setTicksLived(2);
		this.fallingBlock.setDropItem(false);
		this.fallingBlock.setCustomName("advancedslab");
		this.fallingBlock.setCustomNameVisible(false);

		if (getArmorStand() != null) {
			try {
				getFallingBlock().setRiding(Minecraft.getHandle(getArmorStand()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void setMaterial(String data) {
		if (getFallingBlock() != null) {
			getFallingBlock().stopRiding();
			getFallingBlock().allowDeath();
			getFallingBlock().remove();
		}

		this.blockData = Bukkit.getServer().createBlockData(data);
		this.fallingBlockUUID = (this.fallingBlock = spawnFallingBlock(this.location, null, data)).getUniqueId();
		this.fallingBlock.setTicksLived(2);
		this.fallingBlock.setDropItem(false);
		this.fallingBlock.setCustomName("advancedslab");
		this.fallingBlock.setCustomNameVisible(false);

		if (getArmorStand() != null) {
			try {
				getFallingBlock().setRiding(Minecraft.getHandle(getArmorStand()));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	ISlabFallingBlock spawnFallingBlock(Location location, Material material, String data) {
		return AdvancedSlabs.instance.entityManager.spawnFallingBlock(location, material, data);
	}

	ISlabFallingBlock spawnFallingBlock(Location location, Material material, byte data) {
		return AdvancedSlabs.instance.entityManager.spawnFallingBlock(location, material, data);
	}

	@Override
	public BlockData getBlockData() {
		return blockData;
	}

	@Deprecated
	@Override
	public MaterialData getMaterialData() {
		return materialData;
	}

	@Override
	public UUID getShulkerUUID() {
		return shulkerUUID;
	}

	@Override
	public UUID getArmorStandUUID() {
		return armorStandUUID;
	}

	@Override
	public UUID getFallingBlockUUID() {
		return fallingBlockUUID;
	}

	@Override

	public Shulker getShulker() {
		return shulker;
	}

	@Override

	public ArmorStand getArmorStand() {
		return armorStand;
	}

	@Override

	public ISlabFallingBlock getFallingBlock() {
		return fallingBlock;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void move(Location location) {
		if (despawned) { return; }
		location.setPitch(0);
		location.setYaw(0);

		this.location = location;

		if (getArmorStand() != null) { EntityHelper.setPosition(getArmorStand(), location.getX(), location.getY(), location.getZ()); }

		//		reStackEntities();
	}

	@Override
	public void moveRelative(Vector vector) {
		move(this.location.clone().add(vector));
	}

	public void reStackEntities() {
		if (despawned) { return; }
		if (getArmorStand() == null) { return; }
		EntityHelper.addPassenger(getArmorStand(), getShulker());
		//		EntityHelper.addPassenger(getArmorStand(), getFallingBlock());
		try {
			getFallingBlock().setRiding(Minecraft.getHandle(getArmorStand()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void despawn() {
		despawned = true;
		if (getFallingBlock() != null) {
			getFallingBlock().allowDeath();
			getFallingBlock().remove();
		}
		if (getShulker() != null) { getShulker().remove(); }
		if (getArmorStand() != null) { getArmorStand().remove(); }
	}

	@Override
	public void respawnFallingBlock() {
		if (despawned) { return; }

		setMaterial( getBlockData().getAsString());
	}

	@Override
	public void refreshEntities() {
		for (Entity entity : this.location.getWorld()/*.getNearbyEntities(this.location, 8, 8, 8)*/.getEntitiesByClasses(Shulker.class, ArmorStand.class, FallingBlock.class, ISlabFallingBlock.class)) {
			if (entity.getType() == EntityType.SHULKER) {
				if (entity.getUniqueId().equals(this.shulkerUUID)) {this.shulker = (Shulker) entity;}
			}
			if (entity.getType() == EntityType.ARMOR_STAND) {
				if (entity.getUniqueId().equals(this.armorStandUUID)) { this.armorStand = (ArmorStand) entity; }
			}
			if (entity instanceof ISlabFallingBlock) {
				if (entity.getUniqueId().equals(this.fallingBlockUUID)) { this.fallingBlock = (ISlabFallingBlock) entity; }
			}
		}

		if (this.fallingBlockUUID != null && this.fallingBlock == null) {
			this.respawnFallingBlock();
		}
	}

	@Override
	public boolean isDespawned() {
		return despawned;
	}

	@Override
	public int getPathId() {
		return path;
	}

	@Override
	public int getPointIndex() {
		return pathPointIndex;
	}

	@Override
	public void setPointIndex(int index) {
		pathPointIndex = index;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();

		JsonObject locationObject = new JsonObject();
		locationObject.addProperty("world", getLocation().getWorld().getName());
		locationObject.addProperty("x", getLocation().getX());
		locationObject.addProperty("y", getLocation().getY());
		locationObject.addProperty("z", getLocation().getZ());

		jsonObject.add("location", locationObject);
		jsonObject.addProperty("material", getBlockData().getMaterial().name());
		jsonObject.addProperty("data", getBlockData().getAsString());

		jsonObject.addProperty("shulker", shulkerUUID.toString());
		jsonObject.addProperty("armorstand", armorStandUUID.toString());
		jsonObject.addProperty("fallingblock", fallingBlockUUID != null ? fallingBlockUUID.toString() : null);

		if (AdvancedSlabs.instance.getConfig().getBoolean("slabOwners") && this.owner != null) {
			jsonObject.addProperty("owner", this.owner.toString());
		}
		if (this.path != -1) {
			jsonObject.addProperty("path", this.path);
		}

		return jsonObject;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		AdvancedSlab that = (AdvancedSlab) o;
		return path == that.path &&
				pathPointIndex == that.pathPointIndex &&
				despawned == that.despawned &&
				Objects.equals(armorStandUUID, that.armorStandUUID) &&
				Objects.equals(shulkerUUID, that.shulkerUUID) &&
				Objects.equals(fallingBlockUUID, that.fallingBlockUUID) &&
				Objects.equals(location, that.location) &&
				Objects.equals(blockData, that.blockData) &&
				Objects.equals(armorStand, that.armorStand) &&
				Objects.equals(shulker, that.shulker) &&
				Objects.equals(fallingBlock, that.fallingBlock) &&
				Objects.equals(owner, that.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(armorStandUUID, shulkerUUID, fallingBlockUUID, location, blockData, armorStand, shulker, fallingBlock, owner, path, pathPointIndex, despawned);
	}
}
