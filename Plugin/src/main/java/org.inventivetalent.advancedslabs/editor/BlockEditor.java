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

package org.inventivetalent.advancedslabs.editor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;

import java.util.UUID;

public class BlockEditor {

	static final double MAX_STEPS = 1.0D;
	static final double MIN_STEPS = 0.001953125D;

	public UUID          player;
	public IAdvancedSlab slab;

	public double steps = 0.0625;

	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}

	public void handleScroll(boolean increase, boolean decrease, boolean sneaking) {
		EditAxis axis = EditAxis.getForPlayer(getPlayer());

		if (sneaking) {
			if (increase) {
				if (steps < MAX_STEPS) {
					steps *= 2;
				}
			} else {
				if (steps > MIN_STEPS) {
					steps /= 2;
				}
			}
			getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.scrollSteps", steps));
		} else {
			//Reverse the action based on the direction
			if (axis.isNegative()) {
				increase = !increase;
				decrease = !decrease;
			}

			Vector vector = axis.applyToVector(new Vector(), increase ? steps : -steps);
			Location target = this.slab.getLocation().add(vector);

			getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.locationInfo", target.getX(), target.getY(), target.getZ()));

			this.slab.refreshEntities();
			this.slab.move(target);
		}
	}

}
