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
