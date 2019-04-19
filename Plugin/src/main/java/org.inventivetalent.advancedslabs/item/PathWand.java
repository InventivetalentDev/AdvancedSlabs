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

package org.inventivetalent.advancedslabs.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.path.IPathPoint;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;
import org.inventivetalent.advancedslabs.movement.path.PathPoint;
import org.inventivetalent.advancedslabs.movement.path.editor.PathEditor;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;

import java.util.Arrays;

public class PathWand extends AdvancedSlabItem {

	@Override
	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(Material.LEAD);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName("§6Path Wand");
		meta.setLore(Arrays.asList("§7Right-Click to edit a path", /*"§7Left-Click to stop editing",*/ "§7Shift right-click to add a point", "§7Shift left-click to remove a point", " ", "§7Shift left-click in air to reset"));
		itemStack.setItemMeta(meta);

		return itemStack;
	}

	@Override
	public Recipe getRecipe() {
		return new ShapedRecipe(new NamespacedKey(AdvancedSlabs.instance,"path_wand"),getItem())
				.shape("rdr","dld","rdr")
				.setIngredient('r',Material.REDSTONE)
				.setIngredient('d',Material.DIAMOND)
				.setIngredient('l',Material.LEAD);
	}

	@Override
	public void handleInteract(PlayerInteractEvent event) {
		if (event.getPlayer().hasPermission("advancedslabs.path.wand.use")) {
			if (event.getPlayer().isSneaking()) {
				if (event.getAction() == Action.LEFT_CLICK_AIR) {//Reset
					if (AdvancedSlabs.instance.pathEditorManager.isEditing(event.getPlayer().getUniqueId())) {
						AdvancedSlabs.instance.pathEditorManager.removeEditor(event.getPlayer().getUniqueId());
						event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.reset"));
					}
					event.setCancelled(true);
					return;
				}
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {//Add point / Start path
					if (!AdvancedSlabs.instance.pathEditorManager.isEditing(event.getPlayer().getUniqueId())) {//Create new editor first
						AdvancedSlabs.instance.pathEditorManager.newEditor(event.getPlayer().getUniqueId(), AdvancedSlabs.instance.pathManager.newPath(event.getPlayer().getWorld()));
						event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.start"));
					}
					AdvancedSlabs.instance.pathEditorManager.getEditor(event.getPlayer().getUniqueId()).path.addPoint(new PathPoint(event.getClickedBlock()));
					event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.point.added"));
					event.setCancelled(true);
					return;
				}

				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {//Remove point
					if (!AdvancedSlabs.instance.pathEditorManager.isEditing(event.getPlayer().getUniqueId())) {
						event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.error.notEditing"));
						return;
					}
					PathEditor editor = AdvancedSlabs.instance.pathEditorManager.getEditor(event.getPlayer().getUniqueId());
					IPathPoint toRemove = null;
					for (IPathPoint point : editor.path.getPoints()) {
						if (point.isAt(event.getClickedBlock().getLocation())) {
							toRemove = point;
							break;
						}
					}
					if (toRemove != null) {
						editor.path.removePoint(toRemove);
						event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.point.removed"));
						if (editor.path.getPoints().size() <= 0) {
							event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.empty"));
						}
					}
					event.setCancelled(true);
					return;
				}
			} else {//Not sneaking
				if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {//Edit path
					ISlabPath path = AdvancedSlabs.instance.pathManager.getPathForBlock(event.getClickedBlock().getLocation());
					if (path == null) {
						event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.error.notFound"));
						return;
					}
					AdvancedSlabs.instance.pathEditorManager.newEditor(event.getPlayer().getUniqueId(), path);
					event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.edit"));
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@Override
	public void handleEntityInteract(PlayerInteractEntityEvent event) {
		IAdvancedSlab slab = AdvancedSlabs.instance.slabManager.getSlabForEntity(event.getRightClicked());
		if (slab != null) {
			if (AdvancedSlabs.instance.pathEditorManager.isEditing(event.getPlayer().getUniqueId())) {
				PathEditor pathEditor = AdvancedSlabs.instance.pathEditorManager.getEditor(event.getPlayer().getUniqueId());
				((AdvancedSlab) slab).path = pathEditor.path.getId();
				event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.bound"));
				event.setCancelled(true);
			}
		}
	}

	@Override
	public void handleEntityDamage(EntityDamageByEntityEvent event) {
		IAdvancedSlab slab = AdvancedSlabs.instance.slabManager.getSlabForEntity(event.getEntity());
		if (slab != null) {
			if (AdvancedSlabs.instance.pathEditorManager.isEditing(((Player) event.getDamager()).getUniqueId())) {
				PathEditor pathEditor = AdvancedSlabs.instance.pathEditorManager.getEditor(((Player) event.getDamager()).getUniqueId());
				((AdvancedSlab) slab).path = -1;
				((Player) event.getDamager()).sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.path.unbound"));
				event.setCancelled(true);
			}
		}
	}
}
