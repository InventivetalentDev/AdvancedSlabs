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

import net.minecraft.server.v1_9_R1.Entity;
import net.minecraft.server.v1_9_R1.EntityFallingBlock;
import net.minecraft.server.v1_9_R1.IBlockData;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Location;

import java.util.UUID;

public class SlabEntityFallingSand extends EntityFallingBlock implements ISlabFallingBlock {

	public SlabEntityFallingSand(World world) {
		super(world);
	}

	public SlabEntityFallingSand(World world, double d0, double d1, double d2, IBlockData iblockdata) {
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
		super.setCustomName(s);
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
		super.teleportTo(location, false);
	}

	@Override
	public void remove() {
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
	public void m() {
		//		super.m();
		//Ignore any updates.
	}
}
