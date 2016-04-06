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
