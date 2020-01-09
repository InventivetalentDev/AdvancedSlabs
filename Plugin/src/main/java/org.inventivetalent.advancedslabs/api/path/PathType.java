/*
 *
 */

package org.inventivetalent.advancedslabs.api.path;

import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.movement.MovementControllerAbstract;
import org.inventivetalent.advancedslabs.movement.path.types.CircularSwitchController;
import org.inventivetalent.advancedslabs.movement.path.types.ReverseSwitchController;
import org.inventivetalent.advancedslabs.movement.path.types.ReverseToggleController;

public enum PathType {

	CIRCULAR_SWITCH("editor.path.type.circular.switch.description") {
		@Override
		public MovementControllerAbstract newController(ISlabPath path) {
			return new CircularSwitchController(path);
		}
	},
	REVERSE_SWITCH("editor.path.type.reverse.switch.description") {
		@Override
		public MovementControllerAbstract newController(ISlabPath path) {
			return new ReverseSwitchController(path);
		}
	},
	REVERSE_TOGGLE("editor.path.type.reverse.toggle.description") {
		@Override
		public MovementControllerAbstract newController(ISlabPath path) {
			return new ReverseToggleController(path);
		}
	};

	public String description;

	PathType(String description) {
		this.description = description;
	}

	public String getFormattedDescription() {
		String format = AdvancedSlabs.instance.messages.getMessage("editor.path.type.format", "%s", "%s");//Workaround to keep the format
		if (format == null || format.isEmpty()) { return ""; }
		return String.format(format, this.name(), AdvancedSlabs.instance.messages.getMessage(this.description));
	}

	public PathType next() {
		if (ordinal() < values().length - 1) {
			return values()[ordinal() + 1];
		}
		return values()[0];
	}

	public PathType previous() {
		if (ordinal() > 0) {
			return values()[ordinal() - 1];
		}
		return values()[values().length - 1];
	}

	public MovementControllerAbstract newController(ISlabPath path) {
		return null;
	}
}
