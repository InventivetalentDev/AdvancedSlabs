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

package org.inventivetalent.advancedslabs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.inventivetalent.advancedslabs.editor.EditorManager;
import org.inventivetalent.advancedslabs.item.ItemManager;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;
import org.inventivetalent.glow.GlowAPI;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandHandler implements CommandExecutor, TabCompleter {

	private AdvancedSlabs plugin;

	public Set<UUID> highlightedPlayers = new HashSet<>();

	public CommandHandler(AdvancedSlabs plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("advancedslabs.wand")) {
				sender.sendMessage("§a/aslab wand");
			}
			if (sender.hasPermission("advancedslabs.highlight")) {
				sender.sendMessage("§a/aslab hightlight");
			}
			return true;
		}
		if ("wand".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.wand")) {
				if (sender instanceof Player) {
					((Player) sender).getInventory().addItem(ItemManager.editorWand.getItem());
				}
			}
		}
		if ("highlight".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.hightlight")) {
				if (sender instanceof Player) {
					for (Entity entity : ((Player) sender).getNearbyEntities(16, 16, 16)) {
						AdvancedSlab slab = plugin.slabManager.getSlabForEntity(entity);
						if (slab != null) {
							if (highlightedPlayers.contains(((Player) sender).getUniqueId())) {
								GlowAPI.setGlowing(slab.getShulker(), false, (Player) sender);
							} else {
								EditorManager.setGlowing(slab, ((Player) sender));
							}
						}
					}

					if (highlightedPlayers.contains(((Player) sender).getUniqueId())) {
						highlightedPlayers.remove(((Player) sender).getUniqueId());
					} else {
						highlightedPlayers.add(((Player) sender).getUniqueId());
					}
				}
			}
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		return null;
	}
}
