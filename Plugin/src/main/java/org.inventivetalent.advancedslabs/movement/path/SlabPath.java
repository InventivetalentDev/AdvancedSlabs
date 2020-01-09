/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.inventivetalent.advancedslabs.api.path.IMovementController;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;
import org.inventivetalent.advancedslabs.api.path.PathType;
import org.inventivetalent.advancedslabs.movement.MovementControllerAbstract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SlabPath implements ISlabPath {

	public static int ID_COUNTER = 0;

	public final int    id;
	public final String world;
	public final List<IPathPoint> points = new ArrayList<>();
	public       PathType         type   = PathType.CIRCULAR_SWITCH;//Default

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
		speed = jsonObject.get("speed").getAsDouble();
		movementController = type.newController(this);
	}

	public void tick() {
		if (this.movementController == null /*|| !this.movementController.active*/) { return; }
		checkActive();
		this.movementController.blocksPerTick = this.speed;
		this.movementController.move();
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getWorldName() {
		return world;
	}

	@Override
	public World getWorld() {
		return Bukkit.getWorld(this.world);
	}

	@Override
	public void addPoint(IPathPoint point) {
		this.points.add(point);
	}

	@Override
	public void addPoint(IPathPoint point, int position) {
		if (position < 0) { throw new IllegalArgumentException("position < 0"); }
		if (position >= points.size()) { throw new IllegalArgumentException("position >= size"); }
		this.points.add(position, point);
	}

	@Override
	public void removePoint(IPathPoint point) {
		this.points.remove(point);
	}

	@Override
	public IPathPoint getPoint(int position) {
		if (position < 0) { throw new IllegalArgumentException("position < 0"); }
		if (position >= points.size()) { throw new IllegalArgumentException("position >= size"); }
		return this.points.get(position);
	}

	@Override
	public List<IPathPoint> getPoints() {
		return points;
	}

	@Override
	public IPathPoint getStart() {
		if (length() > 0) {
			return getPoint(0);
		}
		return null;
	}

	@Override
	public IPathPoint getEnd() {
		if (length() > 0) {
			return getPoint(length() - 1);
		}
		return null;
	}

	@Override
	public PathType getType() {
		return type;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

	@Override
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public IMovementController getMovementController() {
		return movementController;
	}

	public boolean checkActive() {
		IPathPoint start = getStart();
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
					this.movementController.setActive(true);
				}
				return true;
			}
		}
		if (this.movementController != null) {
			this.movementController.setActive(false);
		}
		return false;
	}

	@Override
	public void clear() {
		this.points.clear();
	}

	@Override
	public int length() {
		return this.points.size();
	}

	public JsonObject toJson() {
		JsonArray jsonArray = new JsonArray();
		for (IPathPoint point : points) {
			jsonArray.add(((PathPoint) point).toJson());
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
