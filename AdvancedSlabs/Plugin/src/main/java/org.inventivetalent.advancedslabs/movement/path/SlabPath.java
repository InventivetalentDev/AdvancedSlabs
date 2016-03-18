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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.inventivetalent.advancedslabs.movement.MovementControllerAbstract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlabPath {

	public static int ID_COUNTER = 0;

	public final int    id;
	public final String world;
	public final List<PathPoint> points = new ArrayList<>();
	public       PathType        type   = PathType.CIRCULAR_TOGGLE;//Default

	public double speed = 0.0625;//Blocks per tick
	public MovementControllerAbstract movementController;

	public SlabPath(String world) {
		this.id = ID_COUNTER++;
		this.world = world;
	}

	public SlabPath(World world) {
		this(world.getName());
	}

	public SlabPath(JsonObject jsonObject) {
		id = jsonObject.get("id").getAsInt();
		if (id > ID_COUNTER) { ID_COUNTER = id; }

		world = jsonObject.get("world").getAsString();
		JsonArray jsonArray = jsonObject.get("points").getAsJsonArray();
		for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext(); ) {
			points.add(new PathPoint(iterator.next().getAsJsonObject()));
		}
		type = PathType.valueOf(jsonObject.get("type").getAsString());
		speed = jsonObject.get("speed").getAsInt();
		movementController = type.newController(this);
	}

	public void tick() {
		if (this.movementController == null || !this.movementController.moving) { return; }
		this.movementController.blocksPerTick = this.speed;
		this.movementController.move();
	}

	public World getWorld() {
		return Bukkit.getWorld(this.world);
	}

	public void addPoint(PathPoint point) {
		this.points.add(point);
	}

	public void addPoint(PathPoint point, int position) {
		if (position < 0) { throw new IllegalArgumentException("position < 0"); }
		if (position >= points.size()) { throw new IllegalArgumentException("position >= size"); }
		this.points.add(position, point);
	}

	public void removePoint(PathPoint point) {
		this.points.remove(point);
	}

	public PathPoint getPoint(int position) {
		if (position < 0) { throw new IllegalArgumentException("position < 0"); }
		if (position >= points.size()) { throw new IllegalArgumentException("position >= size"); }
		return this.points.get(position);
	}

	public PathPoint getStart() {
		if (length() > 0) {
			return getPoint(0);
		}
		return null;
	}

	public PathPoint getEnd() {
		if (length() > 0) {
			return getPoint(length() - 1);
		}
		return null;
	}

	public boolean isActive() {
		PathPoint start = getStart();
		if (start == null) { return false; }
		Block startBlock = start.getLocation(getWorld()).getBlock();
		BlockFace[] faces = new BlockFace[] {
				BlockFace.SELF,
				BlockFace.UP,
				BlockFace.DOWN,
				BlockFace.NORTH,
				BlockFace.SOUTH,
				BlockFace.EAST,
				BlockFace.WEST };
		for (BlockFace face : faces) {
			Block relative = startBlock.getRelative(face);
			if (relative.isBlockIndirectlyPowered() || relative.isBlockPowered()) {
				if (this.movementController != null) {
					this.movementController.moving = true;
				}
				return true;
			}
		}
		return false;
	}

	public void clear() {
		this.points.clear();
	}

	public int length() {
		return this.points.size();
	}

	public JsonObject toJson() {
		JsonArray jsonArray = new JsonArray();
		for (PathPoint point : points) {
			jsonArray.add(point.toJson());
		}

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("id", id);
		jsonObject.addProperty("world", world);
		jsonObject.add("points", jsonArray);
		jsonObject.addProperty("type", type.name());
		jsonObject.addProperty("speed", speed);

		return jsonObject;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		SlabPath path = (SlabPath) o;

		if (id != path.id) { return false; }
		return world != null ? world.equals(path.world) : path.world == null;

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (world != null ? world.hashCode() : 0);
		return result;
	}
}
