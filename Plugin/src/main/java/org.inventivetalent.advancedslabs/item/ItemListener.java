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

