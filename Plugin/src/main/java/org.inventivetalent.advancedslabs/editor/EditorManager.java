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

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;
import org.inventivetalent.glow.GlowAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditorManager {

	private       AdvancedSlabs          plugin;
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

	public BlockEditor newEditor(UUID uuid, IAdvancedSlab slab) {
		removeEditor(uuid);

		BlockEditor editor = new BlockEditor();
		editor.player = uuid;
		editor.slab = slab;
		editorMap.put(uuid, editor);

		slab.refreshEntities();

		setGlowing(slab, editor.getPlayer());

		return editor;
	}

	public static void setGlowing(IAdvancedSlab slab, Player receiver) {
		BlockData blockData = slab.getBlockData();
		Material type = blockData.getMaterial();
		String materialName = type.name();

		GlowAPI.Color glowColor = null;
		if (materialName.startsWith("WHITE_")) {
			glowColor = GlowAPI.Color.WHITE;
		} else if (materialName.startsWith("ORANGE_") || materialName.startsWith("GOLD_")) {
			glowColor = GlowAPI.Color.GOLD;
		} else if (materialName.startsWith("MAGENTA_") || materialName.startsWith("DARK_PURPLE_")) {
			glowColor = GlowAPI.Color.DARK_PURPLE;
		} else if (materialName.startsWith("LIGHT_BLUE_") || materialName.startsWith("BLUE_")) {
			glowColor = GlowAPI.Color.BLUE;
		} else if (materialName.startsWith("YELLOW_")) {
			glowColor = GlowAPI.Color.YELLOW;
		} else if (materialName.startsWith("LIME_") || materialName.startsWith("GREEN_")) {
			glowColor = GlowAPI.Color.GREEN;
		} else if (materialName.startsWith("PINK_") || materialName.startsWith("PURPLE_")) {
			glowColor = GlowAPI.Color.PURPLE;
		} else if (materialName.startsWith("DARK_GRAY_")) {
			glowColor = GlowAPI.Color.DARK_GRAY;
		} else if (materialName.startsWith("GRAY_")) {
			glowColor = GlowAPI.Color.GRAY;
		} else if (materialName.startsWith("CYAN_") || materialName.startsWith("AQUA_")) {
			glowColor = GlowAPI.Color.AQUA;
		} else if (materialName.startsWith("RED_")) {
			glowColor = GlowAPI.Color.RED;
		} else if (materialName.startsWith("BLACK_")) {
			glowColor = GlowAPI.Color.BLACK;
		} else {
			//TODO: add more types
			switch (type) {
				case STONE:
				case COBBLESTONE:
				case COBBLESTONE_STAIRS:
				case MOSSY_COBBLESTONE:
					glowColor = GlowAPI.Color.GRAY;
					break;
				case FURNACE:
				case DROPPER:
				case DISPENSER:
					glowColor = GlowAPI.Color.DARK_GRAY;
					break;
				case SPONGE:
				case SAND:
				case SANDSTONE:
				case SANDSTONE_STAIRS:
				case GLOWSTONE:
				case HAY_BLOCK:
					glowColor = GlowAPI.Color.YELLOW;
					break;
				case RED_SANDSTONE:
				case RED_SANDSTONE_STAIRS:
				case PUMPKIN:
				case JACK_O_LANTERN:
					glowColor = GlowAPI.Color.GOLD;
					break;
				case BRICK:
				case BRICK_STAIRS:
				case REDSTONE_ORE:
				case REDSTONE_BLOCK:
					glowColor = GlowAPI.Color.RED;
					break;
				case DIRT:
				case SOUL_SAND:
				case CHEST:
				case TRAPPED_CHEST:
				case JUKEBOX:
				case NOTE_BLOCK:
					glowColor = GlowAPI.Color.DARK_RED;
					break;
				case LAPIS_BLOCK:
				case LAPIS_ORE:
					glowColor = GlowAPI.Color.BLUE;
					break;
				case MELON:
				case EMERALD_BLOCK:
				case EMERALD_ORE:
				case CACTUS:
				case SLIME_BLOCK:
					glowColor = GlowAPI.Color.GREEN;
					break;
				case PRISMARINE:
				case SEA_LANTERN:
				case ICE:
				case FROSTED_ICE:
				case PACKED_ICE:
				case DIAMOND_ORE:
				case DIAMOND_BLOCK:
					glowColor = GlowAPI.Color.AQUA;
					break;
				case PURPUR_BLOCK:
				case PURPUR_PILLAR:
				case PURPUR_SLAB:
				case PURPUR_STAIRS:
					glowColor = GlowAPI.Color.PURPLE;
					break;
				case OBSIDIAN:
				case NETHER_BRICK:
				case NETHER_BRICK_STAIRS:
				case ENDER_CHEST:
					glowColor = GlowAPI.Color.BLACK;
					break;

				default:
					break;
			}
		}
		if (glowColor != null) {
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
