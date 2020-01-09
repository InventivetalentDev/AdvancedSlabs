/*
 *
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
