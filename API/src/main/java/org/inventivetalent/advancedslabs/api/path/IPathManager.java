/*
 *
 */

package org.inventivetalent.advancedslabs.api.path;

import org.bukkit.Location;
import org.bukkit.World;

public interface IPathManager {

	ISlabPath newPath(World world);

	ISlabPath getPathForBlock(Location location);

	ISlabPath getPathById(int id);

}
