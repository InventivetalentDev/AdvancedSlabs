/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path;

import com.google.gson.JsonObject;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;

public class PathPoint implements IPathPoint {

	public final double x;
	public final double y;
	public final double z;

	public PathPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public PathPoint(Location location) {
		this(location.getX(), location.getY(), location.getZ());
	}

	public PathPoint(Block block) {
		this(block.getLocation().add(.5, 0, .5));
	}

	public PathPoint(JsonObject jsonObject) {
		this.x = jsonObject.get("x").getAsDouble();
		this.y = jsonObject.get("y").getAsDouble();
		this.z = jsonObject.get("z").getAsDouble();
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public double getZ() {
		return z;
	}

	@Override
	public Location getLocation(World world) {
		return new Location(world, getX(), getY(), getZ());
	}

	@Override
	public boolean isAt(Location other) {
		return other.getBlock().getLocation().equals(getLocation(other.getWorld()).getBlock().getLocation());
	}

	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("x", getX());
		jsonObject.addProperty("y", getY());
		jsonObject.addProperty("z", getZ());

		return jsonObject;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		PathPoint pathPoint = (PathPoint) o;

		if (Double.compare(pathPoint.x, x) != 0) { return false; }
		if (Double.compare(pathPoint.y, y) != 0) { return false; }
		return Double.compare(pathPoint.z, z) == 0;

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

}
