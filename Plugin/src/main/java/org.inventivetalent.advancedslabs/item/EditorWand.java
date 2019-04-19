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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;

import java.util.Arrays;

public class EditorWand extends AdvancedSlabItem {
	@Override
	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(Material.STICK);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName("§aEditor Wand");
		meta.setLore(Arrays.asList("§7Right-Click Blocks to edit!"));
		itemStack.setItemMeta(meta);

		return itemStack;
	}

	@Override
	public Recipe getRecipe() {
		return new ShapedRecipe(new NamespacedKey(AdvancedSlabs.instance, "editor_wand"), getItem())
				.shape("rdr","rsr"," s ")
				.setIngredient('r',Material.REDSTONE)
				.setIngredient('s',Material.STICK)
				.setIngredient('d',Material.DIAMOND);
	}

	@Override
	public void handlePrepareCraft(PrepareItemCraftEvent event) {
	}

	@Override
	public void handleCraft(CraftItemEvent event) {
		if (!event.getWhoClicked().hasPermission("advancedslabs.crafting.wand")) {
			event.setCancelled(true);
		}
	}

	@Override
	public void tryHandleInteract(PlayerInteractEvent event) {
		if (!event.getPlayer().hasPermission("advancedslabs.wand.use")) {
			return;
		}
		super.tryHandleInteract(event);
		//Ignore the item when editing
		if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (AdvancedSlabs.instance.editorManager.isEditing(event.getPlayer().getUniqueId())) {
				IAdvancedSlab slab = AdvancedSlabs.instance.editorManager.getEditor(event.getPlayer().getUniqueId()).slab;

				AdvancedSlabs.instance.editorManager.removeEditor(event.getPlayer().getUniqueId());
				event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.finished"));
				event.setCancelled(true);

				if (AdvancedSlabs.instance.getConfig().getBoolean("ensureSolid")) {
					//Place a barrier block
					Location location = slab.getLocation().clone();
					double partial = location.getY() - ((int) location.getY());
					if (partial >= 0.5) {
						//Below
						if (location.getBlock().getType() == Material.AIR) {
							location.getBlock().setType(Material.BARRIER);
						}
						if (partial > 0.5625) {
							//Also place a slab
							location.clone().add(0, 0.5, 0).getBlock().setType(Material.FLOWER_POT);
						}
					} else {
						//Same height
						location = location.add(0, 0.015625, 0);
						if (location.getBlock().getType() == Material.AIR) {
							location.getBlock().setType(Material.BARRIER);
						}
					}
				}
			}
		}
	}

	@Override
	public void handleInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getPlayer().isSneaking()) {
				Material blockType = event.getClickedBlock().getType();
				byte blockData = event.getClickedBlock().getData();

				event.getClickedBlock().setType(Material.AIR);

				ItemStack slabBlock = new ItemStack(blockType, 1, blockData);
				ItemMeta meta = slabBlock.getItemMeta();
				meta.setDisplayName(AdvancedSlabs.instance.messages.getMessage("blockPrefix") + blockType.name() + (blockData > 0 ? ":" + blockData : ""));
				slabBlock.setItemMeta(meta);
//				try {
//					slabBlock = new ItemBuilder(slabBlock).buildMeta().glow().item().build();
//				} catch (Exception e) {
//				}

				for (int i = 0; i < AdvancedSlabs.instance.getConfig().getInt("slabRatio"); i++) {
					event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation().add(.5, .5, .5), slabBlock).setPickupDelay(0);
				}
				return;
			}
		}
	}

	@Override
	public void handleEntityInteract(PlayerInteractEntityEvent event) {
		final IAdvancedSlab slab = AdvancedSlabs.instance.slabManager.getSlabForEntity(event.getRightClicked());
		if (slab != null) {
			if (AdvancedSlabs.instance.getConfig().getBoolean("slabOwners")) {
				if (slab.getOwner() != null && !slab.getOwner().equals(event.getPlayer().getUniqueId()) && !event.getPlayer().hasPermission("advancedslabs.editall")) {
					event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.error.noPermission"));
					return;
				}
			}
			if (event.getPlayer().isSneaking()) {
				AdvancedSlabs.instance.editorManager.removeEditor(event.getPlayer().getUniqueId());
				AdvancedSlabs.instance.slabManager.removeSlab(slab);

				Material blockType = slab.getBlockData().getMaterial();

				final ItemStack defaultBlock = new ItemStack(blockType, 1);
				Bukkit.getScheduler().runTaskLater(AdvancedSlabs.instance, new Runnable() {
					@Override
					public void run() {
						//Slight delay to avoid more events by the second interact event
						slab.getLocation().getWorld().dropItemNaturally(slab.getLocation(), defaultBlock).setPickupDelay(0);
					}
				}, 2);
			} else {
				AdvancedSlabs.instance.editorManager.newEditor(event.getPlayer().getUniqueId(), slab);
				event.getPlayer().sendMessage(AdvancedSlabs.instance.messages.getMessage("editor.editing"));
			}
			event.setCancelled(true);
		}
	}
}
