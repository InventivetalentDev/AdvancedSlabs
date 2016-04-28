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

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.path.IMovementController;
import org.inventivetalent.advancedslabs.api.path.IPathPassenger;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;
import org.inventivetalent.boundingbox.BoundingBox;

import java.util.Set;

public abstract class MovementControllerAbstract implements IMovementController {

	public final ISlabPath path;
	public boolean active        = false;
	public double  blocksPerTick = 0.0625D;

	public MovementControllerAbstract(ISlabPath path) {
		this.path = path;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<IAdvancedSlab> getSlabs() {
		return AdvancedSlabs.instance.slabManager.getSlabsForPath(this.path);
	}

	public abstract IPathPoint getNext(IPathPassenger pathPassenger);

	public abstract IPathPoint goToNext(IPathPassenger pathPassenger);

	public abstract IPathPoint getPrevious(IPathPassenger pathPassenger);

	public abstract IPathPoint goToPrevious(IPathPassenger pathPassenger);

	public IPathPoint getCurrent(IPathPassenger pathPassenger) {
		return this.path.getPoint(pathPassenger.getPointIndex());
	}

	public Vector getDirection(IAdvancedSlab slab) {
		IPathPoint next = getNext(slab);

		return new Vector(next.getX() - slab.getLocation().getX(), next.getY() - slab.getLocation().getY(), next.getZ() - slab.getLocation().getZ());
	}

	public boolean isAtTarget(IAdvancedSlab slab) {//target == next block
		IPathPoint next = getNext(slab);

		Location target = next.getLocation(slab.getLocation().getWorld());
		Location self = slab.getLocation();

		BoundingBox targetBox = new BoundingBox(target.getX(), target.getY(), target.getZ(), target.getX() + 1, target.getY() + 1, target.getZ() + 1);
		BoundingBox selfBox = new BoundingBox(self.getX(), self.getY(), self.getZ(), self.getX() + 1, self.getY() + 1, self.getZ() + 1);

		return targetBox.expand(blocksPerTick).contains(selfBox);
	}

	public void move() {
		if (!active) { return; }
		for (IAdvancedSlab slab : getSlabs()) {
			move(slab);
		}
	}

	public abstract void move(IAdvancedSlab slab);

}
