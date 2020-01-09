/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bukkit.Location;
import org.bukkit.World;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.path.IPathManager;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PathManager implements IPathManager {

	private AdvancedSlabs plugin;
	public final Set<ISlabPath> paths = new HashSet<>();

	public PathManager(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	@Override
	public ISlabPath newPath(World world) {
		SlabPath path = new SlabPath(world);
		paths.add(path);
		return path;
	}

	@Override
	public ISlabPath getPathForBlock(Location location) {
		for (ISlabPath path : paths) {
			for (IPathPoint point : path.getPoints()) {
				if (point.isAt(location)) { return path; }
			}
		}
		return null;
	}

	@Override
	public ISlabPath getPathById(int id) {
		for (ISlabPath path : paths) {
			if (path.getId() == id) { return path; }
		}
		return null;
	}

	public JsonArray toJson() {
		JsonArray jsonArray = new JsonArray();

		for (ISlabPath path : paths) {
			if (path.length() <= 0) { continue; }
			jsonArray.add(((SlabPath) path).toJson());
		}

		return jsonArray;
	}

	public void loadJson(JsonArray jsonArray) {
		for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext(); ) {
			paths.add(new SlabPath(iterator.next().getAsJsonObject()));
		}
	}

	public int size() {
		return paths.size();
	}

}
