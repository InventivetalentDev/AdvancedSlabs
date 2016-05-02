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
