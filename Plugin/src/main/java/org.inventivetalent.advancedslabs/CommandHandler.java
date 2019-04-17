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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.path.ISlabPath;
import org.inventivetalent.advancedslabs.editor.EditorManager;
import org.inventivetalent.advancedslabs.item.ItemManager;
import org.inventivetalent.advancedslabs.movement.path.editor.PathEditor;
import org.inventivetalent.glow.GlowAPI;

import java.util.*;

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
			if (sender.hasPermission("advancedslabs.pathwand")) {
				sender.sendMessage("§a/aslab pathwand");
			}
			if (sender.hasPermission("advancedslabs.highlight")) {
				sender.sendMessage("§a/aslab highlight");
			}
			if (sender.hasPermission("advancedslabs.reload")) {
				sender.sendMessage("§a/aslab reload");
			}
			if (sender.hasPermission("advancedslabs.give")) {
				sender.sendMessage("§a/aslab give <Material>[:Data] [Player]");
			}
			if (sender.hasPermission("advancedslabs.save")) {
				sender.sendMessage("§a/aslab save");
			}
			return true;
		}
		if ("wand".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.wand")) {
				if (sender instanceof Player) {
					((Player) sender).getInventory().addItem(ItemManager.editorWand.getItem());
				}
				return true;
			}
		}
		if ("pathwand".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.pathwand")) {
				if (sender instanceof Player) {
					((Player) sender).getInventory().addItem(ItemManager.pathWand.getItem());
				}
				return true;
			}
		}
		if ("highlight".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.highlight")) {
				if (sender instanceof Player) {
					for (Entity entity : ((Player) sender).getNearbyEntities(16, 16, 16)) {
						IAdvancedSlab slab = plugin.slabManager.getSlabForEntity(entity);
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
						sender.sendMessage(plugin.messages.getMessage("highlight"));
					}
					return true;
				}
			}
		}
		if ("respawn".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.respawn")) {
				if (sender instanceof Player) {
					for (Entity entity : ((Player) sender).getNearbyEntities(16, 16, 16)) {
						IAdvancedSlab slab = plugin.slabManager.getSlabForEntity(entity);
						if (slab != null) {
							slab.refreshEntities();
							slab.respawnFallingBlock();
						}
					}
					sender.sendMessage(plugin.messages.getMessage("respawn"));
				}
			}
		}
		if ("reload".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.reload")) {
				AdvancedSlabs.instance.reload();
				sender.sendMessage("§aReloaded.");
				return true;
			}
		}
		if ("give".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.give")) {
				if (args.length == 1) {
					sender.sendMessage("§a/aslab give <Material>[Data] [Player]");
					return false;
				}
				String materialString = args[1];
				String dataString = "";
				if (materialString.contains("[")) {
					String[] split = materialString.split("\\[");
					materialString = split[0];
					dataString = "["+split[1];
				}
				Material material = null;
				try {
						material = Material.matchMaterial(materialString);
				} catch (Exception e) {
				}
				if (material == null) {
					sender.sendMessage(plugin.messages.getMessage("invalidMaterial", materialString));
					return false;
				}

				Player target;
				if (args.length > 2) {
					target = Bukkit.getPlayer(args[2]);
					if (target == null || !target.isOnline()) {
						sender.sendMessage(plugin.messages.getMessage("error.notOnline"));
						return false;
					}
				} else {
					if (!(sender instanceof Player)) {
						sender.sendMessage("§cPlease specify the player");
						return false;
					}
					target = (Player) sender;
				}

				ItemStack slabBlock = new ItemStack(material, 1);
				ItemMeta meta = slabBlock.getItemMeta();
				meta.setDisplayName(AdvancedSlabs.instance.messages.getMessage("blockPrefix") + material.name() + (dataString.length() > 0 ? dataString : ""));
				slabBlock.setItemMeta(meta);
//				try {
//					slabBlock = new ItemBuilder(slabBlock).buildMeta().glow().item().build();
//				} catch (Exception e) {
//				}

				target.getWorld().dropItemNaturally(target.getLocation(), slabBlock).setPickupDelay(0);
				return true;
			}
		}
		if ("pathspeed".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.pathspeed")) {
				if (sender instanceof Player) {
					if (!plugin.pathEditorManager.isEditing(((Player) sender).getUniqueId())) {
						sender.sendMessage(plugin.messages.getMessage("editor.path.error.notEditing"));
						return false;
					}
					if (args.length == 1) {
						sender.sendMessage("§cPlease specify the speed");
						return false;
					}
					ISlabPath path = plugin.pathEditorManager.getEditor(((Player) sender).getUniqueId()).path;
					try {
						double speed = Double.parseDouble(args[1]);
						if (speed > PathEditor.MIN_SPEED && speed < PathEditor.MAX_SPEED) {
							path.setSpeed(speed);
							sender.sendMessage("§aSpeed changed to " + speed);
						} else {
							sender.sendMessage("§cSpecified number is too small or too big");
							return false;
						}
					} catch (NumberFormatException e) {
						sender.sendMessage("§cInvalid number: " + args[1]);
						return false;
					}

				}
			}
		}
		if ("save".equalsIgnoreCase(args[0])) {
			if (sender.hasPermission("advancedslabs.save")) {
				sender.sendMessage("§eSaving Slabs...");
				plugin.saveSlabs();
				sender.sendMessage("§eSaving Paths...");
				plugin.savePaths();

				sender.sendMessage("§aDone!");
			}
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		List<String> list = new ArrayList<>();

		if (args.length <= 1) {
			if (sender.hasPermission("advancedslabs.wand")) {
				list.add("wand");
			}
			if (sender.hasPermission("advancedslabs.pathwand")) {
				list.add("pathwand");
			}
			if (sender.hasPermission("advancedslabs.highlight")) {
				list.add("highlight");
			}
			if (sender.hasPermission("advancedslabs.reload")) {
				list.add("reload");
			}
			if (sender.hasPermission("advancedslabs.give")) {
				list.add("give");
			}
		} else {
			if ("give".equalsIgnoreCase(args[0])) {
				if (sender.hasPermission("advancedslabs.give")) {
					for (Material material : Material.values()) {
						list.add(material.name());
					}
				}
			}
		}

		return TabCompletionHelper.getPossibleCompletionsForGivenArgs(args, list.toArray(new String[list.size()]));
	}
}
