/*
 *
 */

package org.inventivetalent.advancedslabs.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class AdvancedSlabCarpet extends AdvancedSlabItem {

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.WHITE_CARPET);
	}

	@Override
	public Recipe getRecipe() {
//		ShapedRecipe recipe = new ShapedRecipe(getItem());
		return null;
	}
}
