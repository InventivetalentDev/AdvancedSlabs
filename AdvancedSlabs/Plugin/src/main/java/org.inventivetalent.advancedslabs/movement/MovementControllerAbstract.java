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
import org.inventivetalent.advancedslabs.movement.path.PathPoint;
import org.inventivetalent.advancedslabs.movement.path.SlabPath;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;

public abstract class MovementControllerAbstract {

	public final SlabPath path;
	public boolean moving        = false;
	public int     pointIndex    = 0;
	public double  blocksPerTick = 0.0625D;

	public MovementControllerAbstract(SlabPath path) {
		this.path = path;
	}

	public AdvancedSlab getSlab() {
		return AdvancedSlabs.instance.slabManager.getSlabForPath(this.path);
	}

	public abstract PathPoint getNext();

	public abstract PathPoint goToNext();

	public abstract PathPoint getPrevious();

	public abstract PathPoint goToPrevious();

	public PathPoint getCurrent() {
		return this.path.getPoint(pointIndex);
	}

	public Vector getDirection() {
		PathPoint next = getNext();

		return new Vector(next.getX() - getSlab().getLocation().getX(), next.getY() - getSlab().getLocation().getY(), next.getZ() - getSlab().getLocation().getZ());
	}

	public boolean isAtTarget() {//target == next block
		PathPoint next = getNext();
		double distance = next.getLocation(getSlab().getLocation().getWorld()).distance(getSlab().getLocation());
		return distance < blocksPerTick;
	}

	public abstract void move();

}
