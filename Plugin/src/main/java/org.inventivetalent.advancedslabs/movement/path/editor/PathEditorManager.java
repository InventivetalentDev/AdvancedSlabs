/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path.editor;

import org.bukkit.Location;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PathEditorManager {

	private AdvancedSlabs plugin;
	public final Map<UUID, PathEditor> editorMap = new HashMap<>();

	public PathEditorManager(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	public PathEditor getEditor(UUID uuid) {
		return editorMap.get(uuid);
	}

	public PathEditor getEditorForPathBlock(Location location) {
		for (PathEditor editor : editorMap.values()) {
			if (editor.path == null) { continue; }
			for (IPathPoint point : editor.path.getPoints()) {
				if (point.isAt(location)) { return editor; }
			}
		}
		return null;
	}

	public PathEditor newEditor(UUID uuid, ISlabPath path) {
		removeEditor(uuid);

		PathEditor editor = new PathEditor();
		editor.player = uuid;
		editor.path = path;
		editorMap.put(uuid, editor);

		return editor;
	}

	public void removeEditor(UUID uuid) {
		PathEditor editor = editorMap.remove(uuid);
	}

	public boolean isEditing(UUID uuid) {
		return editorMap.containsKey(uuid);
	}

}
