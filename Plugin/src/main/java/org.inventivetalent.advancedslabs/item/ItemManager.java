/*
 *
 */

package org.inventivetalent.advancedslabs.item;

import org.bukkit.Bukkit;
import org.inventivetalent.advancedslabs.AdvancedSlabs;

public class ItemManager {

	private AdvancedSlabs plugin;

	public static final EditorWand        editorWand        = new EditorWand();
	public static final PathWand          pathWand          = new PathWand();
	public static final AdvancedSlabBlock advancedSlabBlock = new AdvancedSlabBlock();

	public static final AdvancedSlabItem[] ITEMS = new AdvancedSlabItem[] {
			editorWand,
			pathWand,
			advancedSlabBlock };

	public ItemManager(AdvancedSlabs plugin) {
		this.plugin = plugin;

		for (AdvancedSlabItem item : ITEMS) {
			if (item.getRecipe() != null) {
				Bukkit.addRecipe(item.getRecipe());
			}
		}
	}

}
