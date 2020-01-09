/*
 *
 */

package org.inventivetalent.advancedslabs.editor;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public enum EditAxis {

	X,
	Y,
	Z,
	INVALID;

	protected boolean negative = false;

	EditAxis() {
	}

	public boolean isNegative() {
		return negative;
	}

	public Vector applyToVector(Vector vector, double value) {
		switch (this) {
			case X:
				vector = vector.setX(value);
				break;
			case Y:
				vector = vector.setY(value);
				break;
			case Z:
				vector = vector.setZ(value);
				break;
		}
		return vector;
	}

	public EulerAngle applyToAngle(EulerAngle angle, double value) {
		switch (this) {
			case X:
				angle = angle.setX(value);
				break;
			case Y:
				angle = angle.setY(value);
				break;
			case Z:
				angle = angle.setZ(value);
				break;
		}
		return angle;
	}

	@Override
	public String toString() {
		return this.name() + "[negative=" + isNegative() + "]";
	}

	public static EditAxis getForCardinal(int cardinal) {
		if (cardinal == 0) {
			X.negative = true;
			return X;
		}
		if (cardinal == 2) {
			X.negative = false;
			return X;
		}
		if (cardinal == 1) {
			Z.negative = true;
			return Z;
		}
		if (cardinal == 3) {
			Z.negative = false;
			return Z;
		}

		return INVALID;
	}

	public static EditAxis getForDirection(float yaw, float pitch) {
		if (pitch > 75) {
			Y.negative = true;
			return Y;
		}
		if (pitch < -75) {
			Y.negative = false;
			return Y;
		}

		yaw = (yaw - 90) % 360;
		if (yaw < 0) {
			yaw += 360;
		}

		int i = floor((double) (yaw * 4.0F / 360.0F) + 0.5D) & 3;

		return getForCardinal(i);
	}

	public static EditAxis getForPlayer(Player player) {
		Location location = player.getLocation();
		return getForDirection(location.getYaw(), location.getPitch());
	}

	private static int floor(double d) {
		int i = (int) d;
		return d < (double) i ? i - 1 : i;
	}

}
