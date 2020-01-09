/*
 *
 */

package org.inventivetalent.advancedslabs.entity;

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.inventivetalent.advancedslabs.entity.ISlabFallingBlock;

import java.util.UUID;

public class SlabEntityFallingSand_v1_14_R1 extends EntityFallingBlock implements ISlabFallingBlock {

	private boolean canDie = false;

	public SlabEntityFallingSand_v1_14_R1(World world) {
		super(EntityTypes.FALLING_BLOCK, world);
	}

	public SlabEntityFallingSand_v1_14_R1(World world, double d0, double d1, double d2, IBlockData iblockdata) {
		super(world, d0, d1, d2, iblockdata);
	}

	@Override
	public UUID getUniqueId() {
		return this.uniqueID;
	}

	@Override
	public void setTicksLived(int ticksLived) {
		this.ticksLived = ticksLived;
	}

	@Override
	public void setDropItem(boolean dropItem) {
		this.dropItem = dropItem;
	}

	@Override
	public void setCustomName(String s) {
		super.setCustomName(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + s + "\"}"));
	}

	@Override
	public void setCustomNameVisible(boolean flag) {
		super.setCustomNameVisible(flag);
	}

	@Override
	public boolean isDead() {
		return !super.isAlive();
	}

	@Override
	public void teleport(Location location) {
		if (location != null) {
			org.bukkit.World world = location.getWorld();
			if (world != null) {
				super.teleportTo(((CraftWorld) world).getHandle().getWorldProvider().getDimensionManager(), new BlockPosition(location.getX(), location.getY(), location.getZ()));
			}
		}
	}

	@Override
	public void remove() {
		if (!canDie) {
			dead = false;
			return;
		}
		die();
	}

	@Override
	public void eject() {
	}

	@Override
	public void setRiding(Object vehicle) {
		super.a((Entity) vehicle, true);
	}

	@Override
	public void stopRiding() {
		super.stopRiding();
	}

	@Override
	public void allowDeath() {
		this.canDie = true;
	}

	@Override
	public void denyDeath() {
		this.canDie = false;
	}

	@Override
	public void die() {
		if (!canDie) {
			dead = false;
			return;
		}
		super.die();
	}

	@Override
	public void tick() {
		//Ignore any updates.
	}
}
