/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;
import org.inventivetalent.advancedslabs.movement.path.editor.PathEditor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PathParticleTask extends BukkitRunnable {

	private AdvancedSlabs plugin;

	public PathParticleTask(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().isEmpty()) { return; }
		Set<UUID> uuids = new HashSet<>(plugin.pathEditorManager.editorMap.keySet());
		for (UUID uuid : uuids) {
			PathEditor editor = plugin.pathEditorManager.getEditor(uuid);
			if (editor == null) { continue; }
			if (editor.path == null) { continue; }
			for (int i = 0; i < editor.path.length(); i++) {
				IPathPoint point = editor.path.getPoint(i);
				Color color = i == 0 ? Color.LIME : i == editor.path.length() - 1 ? Color.RED : Color.BLUE;

				//Center
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX(), point.getY() + .5, point.getZ(), 0, new Particle.DustOptions(color, 1));

				//Outline
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() - .5, point.getY(), point.getZ() - .5, 0, new Particle.DustOptions(color, 1));
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() - .5, point.getY(), point.getZ() + .5, 0, new Particle.DustOptions(color, 1));
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() + .5, point.getY(), point.getZ() - .5, 0, new Particle.DustOptions(color, 1));
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() + .5, point.getY(), point.getZ() + .5, 0, new Particle.DustOptions(color, 1));
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() - .5, point.getY() + 1, point.getZ() - .5, 0, new Particle.DustOptions(color, 1));
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() - .5, point.getY() + 1, point.getZ() + .5, 0, new Particle.DustOptions(color, 1));
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() + .5, point.getY() + 1, point.getZ() - .5, 0, new Particle.DustOptions(color, 1));
				editor.getPlayer().spawnParticle(Particle.REDSTONE, point.getX() + .5, point.getY() + 1, point.getZ() + .5, 0, new Particle.DustOptions(color, 1));

				if (i < editor.path.length() - 1) {
					double xDiff = editor.path.getPoint(i + 1).getX() - point.getX();
					double yDiff = editor.path.getPoint(i + 1).getY() - point.getY();
					double zDiff = editor.path.getPoint(i + 1).getZ() - point.getZ();
					Vector direction = new Vector(xDiff, yDiff, zDiff);
					for (double d = 0; d < 1; d += 0.1) {
						Location location = direction.clone().multiply(d).add(new Vector(point.getX(), point.getY(), point.getZ())).toLocation(editor.getPlayer().getWorld());
						editor.getPlayer().spawnParticle(Particle.REDSTONE,  location.getX(), location.getY() + .5, location.getZ(), 0, new Particle.DustOptions(Color.YELLOW, 1));
					}
				}
			}
		}
	}

	double particleColor(double value) {
		if (value <= 0) {
			value = -1;
		}
		return value / 255;
	}

}
