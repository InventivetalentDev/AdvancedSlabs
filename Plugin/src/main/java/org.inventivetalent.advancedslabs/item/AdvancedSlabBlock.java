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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.advancedslabs.AdvancedSlabs;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;

public class AdvancedSlabBlock extends AdvancedSlabItem {
	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.STONE);//Will be dynamically changed
	}

	@Override
	public Recipe getRecipe() {
		//		return new ShapedRecipeBuilder(getItem())//
		//				.withShape("rrr", "rbr", "rrr")//
		//				.withIngredient('r', Material.REDSTONE)//
		//				.withIngredient('b', Material.STONE)//Will be dynamically changed
		//				.fromConfig(AdvancedSlabs.instance.getConfig().getConfigurationSection("recipes.placement.block")).build();
		return null;
	}

	@Override
	public void tryHandleBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) { return; }
		if (event.getItemInHand() != null) {
			ItemStack itemStack = event.getItemInHand();
			ItemMeta meta = itemStack.getItemMeta();
			if (meta != null && meta.getDisplayName() != null) {
				if (meta.getDisplayName().startsWith(AdvancedSlabs.instance.messages.getMessage("blockPrefix"))) {
					if (event.getPlayer().hasPermission("advancedslabs.place.block")) {
						event.setCancelled(true);
						event.setBuild(false);

						AdvancedSlab slab = AdvancedSlabs.instance.slabManager.createSlab(event.getBlockPlaced().getLocation().add(.5, 0, .5), itemStack.getType());
						if (AdvancedSlabs.instance.getConfig().getBoolean("slabOwners")) {
							slab.owner = event.getPlayer().getUniqueId();
						}
					}
				}
			}
		}
	}
}
