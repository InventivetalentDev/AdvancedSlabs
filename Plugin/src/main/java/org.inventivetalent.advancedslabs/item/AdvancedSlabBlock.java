/*
 *
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
