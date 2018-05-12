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
