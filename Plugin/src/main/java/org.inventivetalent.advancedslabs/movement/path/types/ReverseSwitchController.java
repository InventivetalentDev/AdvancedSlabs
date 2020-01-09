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

import java.util.HashMap;
import java.util.Map;

public class ReverseSwitchController extends MovementControllerAbstract {

	Map<IPathPassenger, Integer> directionMap = new HashMap<>();

	public ReverseSwitchController(ISlabPath path) {
		super(path);
	}

	@Override
	public IPathPoint getNext(IPathPassenger pathPassenger) {
		int direction = getUpdatedDirection(pathPassenger);
		if (direction == 1) {
			return path.getPoint(pathPassenger.getPointIndex() + 1);
		} else {
			return path.getPoint(pathPassenger.getPointIndex() - 1);
		}
	}

	@Override
	public IPathPoint goToNext(IPathPassenger pathPassenger) {
		updateDirection(pathPassenger);
		if (getDirection(pathPassenger) == 1) {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() + 1);
			return path.getPoint(pathPassenger.getPointIndex());
		} else {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() - 1);
			return path.getPoint(pathPassenger.getPointIndex());
		}
	}

	@Override
	public IPathPoint getPrevious(IPathPassenger pathPassenger) {
		int direction = getUpdatedDirection(pathPassenger);
		if (direction == 1) {
			return path.getPoint(pathPassenger.getPointIndex() - 1);
		} else {
			return path.getPoint(pathPassenger.getPointIndex() + 1);
		}
	}

	@Override
	public IPathPoint goToPrevious(IPathPassenger pathPassenger) {
		updateDirection(pathPassenger);
		if (getDirection(pathPassenger) == 1) {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() - 1);
			return path.getPoint(pathPassenger.getPointIndex());
		} else {
			pathPassenger.setPointIndex(pathPassenger.getPointIndex() + 1);
			return path.getPoint(pathPassenger.getPointIndex());
		}
	}

	int getDirection(IPathPassenger pathPassenger) {
		if (directionMap.containsKey(pathPassenger)) {
			return directionMap.get(pathPassenger);
		}
		return 1;
	}

	void updateDirection(IPathPassenger pathPassenger) {
		int direction = getUpdatedDirection(pathPassenger);
		directionMap.put(pathPassenger, direction);
		//					pathPassenger.setPointIndex(direction);
	}

	int getUpdatedDirection(IPathPassenger pathPassenger) {
		if (pathPassenger.getPointIndex() == path.length() - 1) {//We're at the end
			return 0;
		} else if (pathPassenger.getPointIndex() == 0) {//We're at the start
			return 1;
		}
		return getDirection(pathPassenger);
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
