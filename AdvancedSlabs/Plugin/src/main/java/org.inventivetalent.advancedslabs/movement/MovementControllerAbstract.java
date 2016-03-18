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

package org.inventivetalent.advancedslabs.movement;

import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.movement.path.PathPassenger;
import org.inventivetalent.advancedslabs.movement.path.PathPoint;
import org.inventivetalent.advancedslabs.movement.path.SlabPath;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;

import java.util.Set;

public abstract class MovementControllerAbstract {

	public final SlabPath path;
	public boolean moving        = false;
	public double  blocksPerTick = 0.0625D;

	public MovementControllerAbstract(SlabPath path) {
		this.path = path;
	}

	@Deprecated
	public AdvancedSlab getSlab() {
		return AdvancedSlabs.instance.slabManager.getFirstSlabForPath(this.path);
	}

	public Set<AdvancedSlab> getSlabs() {
		return AdvancedSlabs.instance.slabManager.getSlabsForPath(this.path);
	}

	public abstract PathPoint getNext(PathPassenger pathPassenger);

	public abstract PathPoint goToNext(PathPassenger pathPassenger);

	public abstract PathPoint getPrevious(PathPassenger pathPassenger);

	public abstract PathPoint goToPrevious(PathPassenger pathPassenger);

	public PathPoint getCurrent(PathPassenger pathPassenger) {
		return this.path.getPoint(pathPassenger.getPointIndex());
	}

	public Vector getDirection(AdvancedSlab slab) {
		PathPoint next = getNext(slab);

		return new Vector(next.getX() - slab.getLocation().getX(), next.getY() - slab.getLocation().getY(), next.getZ() - slab.getLocation().getZ());
	}

	public boolean isAtTarget(AdvancedSlab slab) {//target == next block
		PathPoint next = getNext(slab);
		double distance = next.getLocation(slab.getLocation().getWorld()).distance(slab.getLocation());
		return distance < blocksPerTick;
	}

	public void move() {
		for (AdvancedSlab slab : getSlabs()) {
			move(slab);
		}
	}

	public abstract void move(AdvancedSlab slab);

}
