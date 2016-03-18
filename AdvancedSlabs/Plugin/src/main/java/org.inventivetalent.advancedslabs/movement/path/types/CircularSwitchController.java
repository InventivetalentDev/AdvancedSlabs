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

package org.inventivetalent.advancedslabs.movement.path.types;

import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.movement.MovementControllerAbstract;
import org.inventivetalent.advancedslabs.movement.path.PathPassenger;
import org.inventivetalent.advancedslabs.movement.path.PathPoint;
import org.inventivetalent.advancedslabs.movement.path.SlabPath;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;

public class CircularSwitchController extends MovementControllerAbstract {

	public CircularSwitchController(SlabPath path) {
		super(path);
	}

	@Override
	public PathPoint getNext(PathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() < path.length() - 1) {
			return path.getPoint(pathPassenger.getPointIndex() + 1);
		} else {
			return path.getPoint(0);
		}
	}

	@Override
	public PathPoint goToNext(PathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() < path.length() - 1) {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() + 1);
			return path.getPoint(pathPassenger.getPointIndex());
		} else {
			pathPassenger.setPointIndex(0);
			return path.getPoint(pathPassenger.getPointIndex());
		}
	}

	@Override
	public PathPoint getPrevious(PathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() > 0) {
			return path.getPoint(pathPassenger.getPointIndex() - 1);
		} else {
			return path.getPoint(path.length() - 1);
		}
	}

	@Override
	public PathPoint goToPrevious(PathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() > 0) {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() - 1);
			return path.getPoint(pathPassenger.getPointIndex());
		} else {
			pathPassenger.setPointIndex(path.length() - 1);
			return path.getPoint(pathPassenger.getPointIndex());
		}
	}

	@Override
	public void move(AdvancedSlab slab) {
		if (isAtTarget(slab)) {
			goToNext(slab);
		}
		Vector direction = getDirection(slab);
		Vector vector = direction.clone().normalize().multiply(blocksPerTick);
		slab.moveRelative(vector);
	}
}
