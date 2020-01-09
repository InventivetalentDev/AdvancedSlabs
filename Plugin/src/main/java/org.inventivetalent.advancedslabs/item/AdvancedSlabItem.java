/*
 *
 */

package org.inventivetalent.advancedslabs.item;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public abstract class AdvancedSlabItem {

	public abstract ItemStack getItem();

	public Recipe getRecipe() {
		return null;
	}

	public void tryHandlePrepareCraft(PrepareItemCraftEvent event) {
		if (event.getRecipe() == null) { return; }
		if (event.getRecipe().equals(getRecipe())) {
			handlePrepareCraft(event);
		}
	}

	public void tryHandleCraft(CraftItemEvent event) {
		if (event.getRecipe() == null) { return; }
		if (event.getRecipe().equals(getRecipe())) {
			handleCraft(event);
		}
	}

	public void tryHandleInteract(PlayerInteractEvent event) {
		if (event.getItem() != null && event.getItem().equals(getItem())) {
			handleInteract(event);
		}
	}

	public void tryHandleEntityInteract(PlayerInteractEntityEvent event) {
		if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().equals(getItem())) {
			handleEntityInteract(event);
		}
	}

	public void tryHandleBlockPlace(BlockPlaceEvent event) {
		if (event.getItemInHand() != null && event.getItemInHand().equals(getItem())) {
			handleBlockPlace(event);
		}
	}

	public void tryHandleEntityDamge(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (((Player) event.getDamager()).getItemInHand().equals(getItem())) {
				handleEntityDamage(event);
			}
		}
	}

	public void handlePrepareCraft(PrepareItemCraftEvent event) {
	}

	public void handleCraft(CraftItemEvent event) {
	}

	public void handleInteract(PlayerInteractEvent event) {
	}

	public void handleEntityInteract(PlayerInteractEntityEvent event) {
	}

	public void handleBlockPlace(BlockPlaceEvent event) {
	}

	public void handleEntityDamage(EntityDamageByEntityEvent event) {
	}

}
