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

import org.bukkit.util.Vector;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.movement.MovementControllerAbstract;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;

import java.util.HashMap;
import java.util.Map;

public enum PathType {

	CIRCULAR_TOGGLE("editor.path.type.circular.toggle.description") {
		@Override
		public MovementControllerAbstract newController(SlabPath path) {
			return new MovementControllerAbstract(path) {

				@Override
				public PathPoint getNext(PathPassenger pathPassenger) {
					if (pathPassenger.getPointIndex() < path.length() - 1) {
						return path.getPoint(pathPassenger.getPointIndex() + 1);
					} else {
						return path.getPoint(0);
					}
				}

				@Override
				public PathPoint goToNext(PathPassenger pathPassenger) {
					if (pathPassenger.getPointIndex() < path.length() - 1) {
						pathPassenger.setPointIndex(pathPassenger.getPointIndex() + 1);
						return path.getPoint(pathPassenger.getPointIndex());
					} else {
						pathPassenger.setPointIndex(0);
						return path.getPoint(pathPassenger.getPointIndex());
					}
				}

				@Override
				public PathPoint getPrevious(PathPassenger pathPassenger) {
					if (pathPassenger.getPointIndex() > 0) {
						return path.getPoint(pathPassenger.getPointIndex() - 1);
					} else {
						return path.getPoint(path.length() - 1);
					}
				}

				@Override
				public PathPoint goToPrevious(PathPassenger pathPassenger) {
					if (pathPassenger.getPointIndex() > 0) {
						pathPassenger.setPointIndex(pathPassenger.getPointIndex() - 1);
						return path.getPoint(pathPassenger.getPointIndex());
					} else {
						pathPassenger.setPointIndex(path.length() - 1);
						return path.getPoint(pathPassenger.getPointIndex());
					}
				}

				@Override
				public void move(AdvancedSlab slab) {
					if (isAtTarget(slab)) {
						goToNext(slab);
					}
					Vector direction = getDirection(slab);
					Vector vector = direction.clone().normalize().multiply(blocksPerTick);
					slab.moveRelative(vector);
				}
			};
		}
	},
	REVERSE_TOGGLE("editor.path.type.reverse.toggle.description") {
		@Override
		public MovementControllerAbstract newController(SlabPath path) {
			return new MovementControllerAbstract(path) {
				//				int direction = 1;//1 = forward, 0 = backward
				Map<PathPassenger, Integer> directionMap = new HashMap<>();

				@Override
				public PathPoint getNext(PathPassenger pathPassenger) {
					int direction = getUpdatedDirection(pathPassenger);
					if (direction == 1) {
						return path.getPoint(pathPassenger.getPointIndex() + 1);
					} else {
						return path.getPoint(pathPassenger.getPointIndex() - 1);
					}
				}

				@Override
				public PathPoint goToNext(PathPassenger pathPassenger) {
					updateDirection(pathPassenger);
					if (getDirection(pathPassenger) == 1) {
						pathPassenger.setPointIndex(pathPassenger.getPointIndex() + 1);
						return path.getPoint(pathPassenger.getPointIndex());
					} else {
						pathPassenger.setPointIndex(pathPassenger.getPointIndex() - 1);
						return path.getPoint(pathPassenger.getPointIndex());
					}
				}

				@Override
				public PathPoint getPrevious(PathPassenger pathPassenger) {
					int direction = getUpdatedDirection(pathPassenger);
					if (direction == 1) {
						return path.getPoint(pathPassenger.getPointIndex() - 1);
					} else {
						return path.getPoint(pathPassenger.getPointIndex() + 1);
					}
				}

				@Override
				public PathPoint goToPrevious(PathPassenger pathPassenger) {
					updateDirection(pathPassenger);
					if (getDirection(pathPassenger) == 1) {
						pathPassenger.setPointIndex(pathPassenger.getPointIndex() - 1);
						return path.getPoint(pathPassenger.getPointIndex());
					} else {
						pathPassenger.setPointIndex(pathPassenger.getPointIndex() + 1);
						return path.getPoint(pathPassenger.getPointIndex());
					}
				}

				int getDirection(PathPassenger pathPassenger) {
					if (directionMap.containsKey(pathPassenger)) {
						return directionMap.get(pathPassenger);
					}
					return 1;
				}

				void updateDirection(PathPassenger pathPassenger) {
					int direction = getUpdatedDirection(pathPassenger);
					directionMap.put(pathPassenger, direction);
//					pathPassenger.setPointIndex(direction);
				}

				int getUpdatedDirection(PathPassenger pathPassenger) {
					if (pathPassenger.getPointIndex() == path.length() - 1) {//We're at the end
						return 0;
					} else if (pathPassenger.getPointIndex() == 0) {//We're at the start
						return 1;
					}
					return getDirection(pathPassenger);
				}

				@Override
				public void move(AdvancedSlab slab) {
					if (isAtTarget(slab)) {
						goToNext(slab);
					}
					Vector direction = getDirection(slab);
					Vector vector = direction.clone().normalize().multiply(blocksPerTick);
					slab.moveRelative(vector);
				}
			};
		}
	};

	public String description;

	PathType(String description) {
		this.description = description;
	}

	public String getFormattedDescription() {
		String format = AdvancedSlabs.instance.messages.getMessage("editor.path.type.format");
		if (format.isEmpty()) { return ""; }
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

	public MovementControllerAbstract newController(SlabPath path) {
		return null;
	}
}
