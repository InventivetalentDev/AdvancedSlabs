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
