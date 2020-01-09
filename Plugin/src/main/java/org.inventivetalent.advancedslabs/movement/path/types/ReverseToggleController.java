/*
 *
 */

package org.inventivetalent.advancedslabs.movement.path.types;

import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;

public class ReverseToggleController extends ReverseSwitchController {

	public boolean wasActive = false;
	public boolean toggled   = false;

	public ReverseToggleController(ISlabPath path) {
		super(path);
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			if (!wasActive) {
				wasActive = toggled = true;
			}
		} else {
			wasActive = false;
		}
	}

	@Override
	public void move() {
		if (!toggled) {
			return;
		}
		active = true;
		super.move();
	}

	@Override
	public boolean isAtTarget(IAdvancedSlab slab) {
		boolean atTarget = super.isAtTarget(slab);
		if (atTarget) { toggled = false; }
		return atTarget;
	}

}
