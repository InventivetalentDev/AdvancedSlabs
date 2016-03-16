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

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;
import org.inventivetalent.glow.GlowAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditorManager {

	private AdvancedSlabs plugin;
	private final Map<UUID, BlockEditor> editorMap = new HashMap<>();

	public EditorManager(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	public BlockEditor getEditor(UUID uuid) {
		return editorMap.get(uuid);
	}

	public BlockEditor getEditorForSlab(AdvancedSlab slab) {
		for (BlockEditor editor : editorMap.values()) {
			if (editor.slab.equals(slab)) {
				return editor;
			}
		}
		return null;
	}

	public BlockEditor newEditor(UUID uuid, AdvancedSlab slab) {
		removeEditor(uuid);

		BlockEditor editor = new BlockEditor();
		editor.player = uuid;
		editor.slab = slab;
		editorMap.put(uuid, editor);

		slab.refreshEntities();

		setGlowing(slab, editor.getPlayer());

		return editor;
	}

	public static void setGlowing(AdvancedSlab slab, Player receiver) {
		Material type = slab.getMaterialData().getItemType();
		boolean colorable = type == Material.STAINED_GLASS_PANE || type == Material.STAINED_GLASS || type == Material.STAINED_CLAY || type == Material.WOOL;

		if (colorable) {
			GlowAPI.Color glowColor = GlowAPI.Color.WHITE;
			switch (DyeColor.getByData(slab.getMaterialData().getData())) {
				case WHITE:
					glowColor = GlowAPI.Color.WHITE;
					break;
				case ORANGE:
					glowColor = GlowAPI.Color.GOLD;
					break;
				case MAGENTA:
					glowColor = GlowAPI.Color.DARK_PURPLE;
					break;
				case LIGHT_BLUE:
					glowColor = GlowAPI.Color.BLUE;
					break;
				case YELLOW:
					glowColor = GlowAPI.Color.YELLOW;
					break;
				case LIME:
					glowColor = GlowAPI.Color.GREEN;
					break;
				case PINK:
					glowColor = GlowAPI.Color.PURPLE;
					break;
				case GRAY:
					glowColor = GlowAPI.Color.DARK_GRAY;
					break;
				case SILVER:
					glowColor = GlowAPI.Color.GRAY;
					break;
				case CYAN:
					glowColor = GlowAPI.Color.AQUA;
					break;
				case PURPLE:
					glowColor = GlowAPI.Color.RED;
					break;
				case BLUE:
					glowColor = GlowAPI.Color.DARK_BLUE;
					break;
				case BROWN:
					glowColor = GlowAPI.Color.DARK_RED;
					break;
				case GREEN:
					glowColor = GlowAPI.Color.DARK_GREEN;
					break;
				case RED:
					glowColor = GlowAPI.Color.DARK_RED;
					break;
				case BLACK:
					glowColor = GlowAPI.Color.BLUE;
					break;
			}
			GlowAPI.setGlowing(slab.getShulker(), glowColor, receiver);
		} else {
			GlowAPI.setGlowing(slab.getShulker(), true, receiver);
		}
	}

	public void removeEditor(UUID uuid) {
		BlockEditor editor = editorMap.remove(uuid);
		if (editor != null) {
			GlowAPI.setGlowing(editor.slab.getShulker(), false, editor.getPlayer());
		}
	}

	public boolean isEditing(UUID uuid) {
		return editorMap.containsKey(uuid);
	}

}
