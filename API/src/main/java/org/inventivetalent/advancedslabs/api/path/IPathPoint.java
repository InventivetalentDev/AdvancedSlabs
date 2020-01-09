/*
 *
 */

package org.inventivetalent.advancedslabs.api.path;

import org.bukkit.Location;
import org.bukkit.World;

public interface IPathPoint {

	double getX();

	double getY();

	double getZ();

	Location getLocation(World world);

	boolean isAt(Location location);

}
