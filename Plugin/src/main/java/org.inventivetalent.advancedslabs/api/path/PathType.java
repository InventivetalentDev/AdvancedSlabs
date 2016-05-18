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
