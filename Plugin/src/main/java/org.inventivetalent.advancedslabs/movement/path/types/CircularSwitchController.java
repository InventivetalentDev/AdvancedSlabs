/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path.types;

import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.path.IPathPassenger;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;
import org.inventivetalent.advancedslabs.movement.MovementControllerAbstract;

public class CircularSwitchController extends MovementControllerAbstract {

	public CircularSwitchController(ISlabPath path) {
		super(path);
	}

	@Override
	public IPathPoint getNext(IPathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() < path.length() - 1) {
			return path.getPoint(pathPassenger.getPointIndex() + 1);
		} else {
			return path.getPoint(0);
		}
	}

	@Override
	public IPathPoint goToNext(IPathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() < path.length() - 1) {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() + 1);
			return path.getPoint(pathPassenger.getPointIndex());
		} else {
			pathPassenger.setPointIndex(0);
			return path.getPoint(pathPassenger.getPointIndex());
		}
	}

	@Override
	public IPathPoint getPrevious(IPathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() > 0) {
			return path.getPoint(pathPassenger.getPointIndex() - 1);
		} else {
			return path.getPoint(path.length() - 1);
		}
	}

	@Override
	public IPathPoint goToPrevious(IPathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() > 0) {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() - 1);
			return path.getPoint(pathPassenger.getPointIndex());
		} else {
			pathPassenger.setPointIndex(path.length() - 1);
			return path.getPoint(pathPassenger.getPointIndex());
		}
	}

	@Override
	public void move(IAdvancedSlab slab) {
		if (isAtTarget(slab)) {
			goToNext(slab);
		}
		Vector direction = getDirection(slab);
		Vector vector = direction.clone().normalize().multiply(blocksPerTick);
		slab.moveRelative(vector);
	}
}
