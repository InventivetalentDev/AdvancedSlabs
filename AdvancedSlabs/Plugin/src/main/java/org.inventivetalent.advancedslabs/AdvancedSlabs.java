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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.inventivetalent.advancedslabs.editor.BlockEditor;
import org.inventivetalent.advancedslabs.editor.EditorManager;
import org.inventivetalent.advancedslabs.item.ItemListener;
import org.inventivetalent.advancedslabs.item.ItemManager;
import org.inventivetalent.advancedslabs.slab.AdvancedSlab;
import org.inventivetalent.advancedslabs.slab.FallingBlockResetTask;
import org.inventivetalent.advancedslabs.slab.SlabManager;
import org.inventivetalent.apihelper.APIManager;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.messagebuilder.MessageBuilder;
import org.inventivetalent.messagebuilder.MessageContainer;
import org.inventivetalent.packetlistener.PacketListenerAPI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

public class AdvancedSlabs extends JavaPlugin implements Listener {

	public static AdvancedSlabs instance;

	public SlabManager   slabManager;
	public EditorManager editorManager;
	public ItemManager   itemManager;
	public EntityManager entityManager;

	public MessageContainer messages;

	public FallingBlockResetTask fallingBlockResetTask;

	File    slabFile   = new File(getDataFolder(), "slabs.json");
	boolean firstStart = !slabFile.exists();

	@Override
	public void onLoad() {
		APIManager.require(PacketListenerAPI.class, this);
		APIManager.require(GlowAPI.class, this);
	}

	@Override
	public void onEnable() {
		instance = this;

		APIManager.initAPI(PacketListenerAPI.class);
		APIManager.initAPI(GlowAPI.class);

		saveDefaultConfig();
		if (!slabFile.exists()) {
			try {
				slabFile.createNewFile();
			} catch (Exception e) {
				getLogger().log(Level.SEVERE, "Failed to create slab file", e);
			}
		}

		this.slabManager = new SlabManager(this);
		this.editorManager = new EditorManager(this);
		this.itemManager = new ItemManager(this);
		this.entityManager = new EntityManager(this);

		this.fallingBlockResetTask = new FallingBlockResetTask(this);
		this.fallingBlockResetTask.runTaskTimer(this, 20, 20);

		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);

		CommandHandler commandHandler = new CommandHandler(this);
		PluginCommand command = getCommand("advancedslabs");
		command.setExecutor(commandHandler);
		command.setTabCompleter(commandHandler);

		MessageBuilder messageBuilder = new MessageBuilder()//
				.withMessage("editor.editing", "§aYou are now editing this block")//
				.withMessage("editor.finished", "§aYou are no longer editing")//
				.withMessage("blockPrefix", "§bAdvancedSlab: §e")//
				.withMessage("editor.error.solidBlock", "&eSorry, you cannot place this block above a solid block")//
				.withMessage("editor.scrollSteps", "§eSteps: &7%s")//
				.withMessage("editor.locationInfo", "§ex: §7%x§e, y: §7%s§e, z: §7%s")//
				;
		this.messages = messageBuilder.fromConfig(getConfig().getConfigurationSection("messages")).build();

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				getLogger().info("Loading data...");
				loadSlabs();
			}
		}, 40);
	}

	@Override
	public void onDisable() {
		getLogger().info("Saving data...");
		saveSlabs();
	}

	@EventHandler
	public void onScroll(PlayerItemHeldEvent event) {
		if (editorManager.isEditing(event.getPlayer().getUniqueId())) {
			int from = event.getPreviousSlot();
			int to = event.getNewSlot();

			if (from >= 8 && to <= 1) {
				to = 9;
			}
			if (from <= 1 && to >= 8) {
				to = -1;
			}

			final boolean decrease = to > from;
			final boolean increase = to < from;

			BlockEditor editor = editorManager.getEditor(event.getPlayer().getUniqueId());
			if (editor != null) {
				editor.handleScroll(increase, decrease, event.getPlayer().isSneaking());
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		editorManager.removeEditor(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		for (final AdvancedSlab slab : slabManager.slabs) {
			if (slab.getLocation().getChunk().equals(event.getChunk())) {
				Bukkit.getScheduler().runTaskLater(this, new Runnable() {
					@Override
					public void run() {
						slab.refreshEntities();
					}
				}, 5);
				Bukkit.getScheduler().runTaskLater(this, new Runnable() {
					@Override
					public void run() {
						slab.respawnFallingBlock();
					}
				}, 10);
			}
		}
	}

	@EventHandler
	public void on(EntityChangeBlockEvent event) {
		if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("advancedslab")) {
			((FallingBlock) event.getEntity()).setTicksLived(1);
			event.setCancelled(true);

			AdvancedSlab slab = slabManager.getSlabForEntity(event.getEntity());
			if (slab != null) {
				if (editorManager.getEditorForSlab(slab) == null) {
					slabManager.removeSlab(slab);
					slabManager.createSlab(slab.getLocation(), slab.getMaterialData().getItemType(), slab.getMaterialData().getData());
				}
			}
		}
	}

	@EventHandler
	public void on(EntityDeathEvent event) {
		AdvancedSlab slab = slabManager.getSlabForEntity(event.getEntity());
		if (slab != null) {
			if (slab.despawned) {
				slabManager.removeSlab(slab);
			} else {
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(CreatureSpawnEvent event) {
	}

	public Team getCollisionTeam() {
		Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("advslabcoll");
		if (team == null) {
			team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("advslabcoll");
			team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
		}
		return team;
	}

	public void saveSlabs() {
		JsonArray slabArray = slabManager.toJson();

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(slabFile));
			writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(slabArray));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new RuntimeException("Failed to save slabs", e);
		}
	}

	public void loadSlabs() {
		try {
			slabManager.loadJson(new JsonParser().parse(new FileReader(slabFile)).getAsJsonArray());
		} catch (Exception e) {
			if (!firstStart) {
				throw new RuntimeException("Failed to load slabs", e);
			}//Ignore on first start, since the file is empty
		}
	}

}
