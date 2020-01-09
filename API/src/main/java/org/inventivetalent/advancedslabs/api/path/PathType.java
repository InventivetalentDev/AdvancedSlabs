/*
 *
 */

package org.inventivetalent.advancedslabs.api.path;

public enum PathType {

	CIRCULAR_SWITCH,
	REVERSE_SWITCH,
	REVERSE_TOGGLE;

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
}
