/*
 *
 */

package org.inventivetalent.advancedslabs.api.path;

import org.bukkit.World;

import java.util.List;

public interface ISlabPath {

	int getId();

	String getWorldName();

	World getWorld();

	void addPoint(IPathPoint point);

	void addPoint(IPathPoint point, int index);

	void removePoint(IPathPoint point);

	IPathPoint getPoint(int index);

	List<IPathPoint> getPoints();

	IPathPoint getStart();

	IPathPoint getEnd();

	PathType getType();

	double getSpeed();

	void setSpeed(double speed);

	IMovementController getMovementController();

	void clear();

	int length();

}
