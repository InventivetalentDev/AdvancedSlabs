/*
 *
 */

package org.inventivetalent.advancedslabs.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.inventivetalent.advancedslabs.AdvancedSlabs;

public class ItemListener implements Listener {

	private AdvancedSlabs plugin;

	public ItemListener(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PrepareItemCraftEvent event) {
		for (AdvancedSlabItem item : ItemManager.ITEMS) {
			item.tryHandlePrepareCraft(event);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(CraftItemEvent event) {
		for (AdvancedSlabItem item : ItemManager.ITEMS) {
			item.tryHandleCraft(event);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerInteractEvent event) {
		for (AdvancedSlabItem item : ItemManager.ITEMS) {
			item.tryHandleInteract(event);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(PlayerInteractEntityEvent event) {
		for (AdvancedSlabItem item : ItemManager.ITEMS) {
			item.tryHandleEntityInteract(event);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(BlockPlaceEvent event) {
		for (AdvancedSlabItem item : ItemManager.ITEMS) {
			item.tryHandleBlockPlace(event);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EntityDamageByEntityEvent event) {
		for (AdvancedSlabItem item : ItemManager.ITEMS) {
			item.tryHandleEntityDamge(event);
		}
	}

}

