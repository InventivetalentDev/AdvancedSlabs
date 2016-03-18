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

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.movement.path.editor.PathEditor;
import org.inventivetalent.particle.ParticleEffect;

import java.util.Collections;
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
		Set<UUID> uuids = new HashSet<>(plugin.pathEditorManager.editorMap.keySet());
		for (UUID uuid : uuids) {
			PathEditor editor = plugin.pathEditorManager.getEditor(uuid);
			if (editor == null) { continue; }
			if (editor.path == null) { continue; }
			for (int i = 0; i < editor.path.points.size(); i++) {
				PathPoint point = editor.path.getPoint(i);
				Color color = i == 0 ? Color.LIME : i == editor.path.points.size() - 1 ? Color.RED : Color.BLUE;

				//Center
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX(), point.getY()+.5, point.getZ(), color);

				//Outline
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() - .5, point.getY() , point.getZ() - .5, color);
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() - .5, point.getY() , point.getZ() + .5, color);
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() + .5, point.getY() , point.getZ() - .5, color);
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() + .5, point.getY() , point.getZ() + .5, color);
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() - .5, point.getY() + 1, point.getZ() - .5, color);
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() - .5, point.getY() + 1, point.getZ() + .5, color);
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() + .5, point.getY() + 1, point.getZ() - .5, color);
				ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), point.getX() + .5, point.getY() + 1, point.getZ() + .5, color);

				if (i < editor.path.points.size() - 1) {
					double xDiff =editor.path.getPoint(i + 1).getX()- point.getX();
					double yDiff =  editor.path.getPoint(i + 1).getY()-point.getY();
					double zDiff =  editor.path.getPoint(i + 1).getZ()-point.getZ() ;
					Vector direction = new Vector(xDiff, yDiff, zDiff);
					for (double d = 0; d < 1; d += 0.1) {
						Location location = direction.clone().multiply(d).add(new Vector(point.getX(),point.getY(),point.getZ())).toLocation(editor.getPlayer().getWorld());
						ParticleEffect.REDSTONE.sendColor(Collections.singleton(editor.getPlayer()), location.getX(), location.getY(), location.getZ(), Color.YELLOW);
					}
				}
			}
		}
	}
}
