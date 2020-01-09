/*
 *
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
