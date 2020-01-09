/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path.editor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;
import org.inventivetalent.advancedslabs.movement.path.SlabPath;

import java.util.UUID;

public class PathEditor {

	public static final double MIN_SPEED = 0.001953125D;
	public static final double MAX_SPEED = 10;

	public UUID      player;
	public ISlabPath path;

	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}

	public void handleScroll(boolean increase, boolean decrease, boolean sneaking) {
		if (sneaking) {
			if (increase) {
				if (((SlabPath) path).speed < MAX_SPEED) {
					((SlabPath) path).speed += 0.001953125D;
				}
			} else {
				if (((SlabPath) path).speed > MIN_SPEED) {
					((SlabPath) path).speed -= 0.001953125D;
				}
			}
			getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.speed.format", path.getSpeed()));
		}
	}

	public void handleDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().isSneaking()) {
			((SlabPath) path).type = ((SlabPath) path).type.previous();
		} else {
			((SlabPath) path).type = ((SlabPath) path).type.next();
		}
		((SlabPath) path).movementController = ((SlabPath) path).type.newController(path);
		event.getPlayer().sendMessage(((SlabPath) path).type.getFormattedDescription());
		event.setCancelled(true);
	}

}
