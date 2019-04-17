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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.advancedslabs.api.AdvancedSlabsAPI;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.advancedslabs.api.ISlabManager;
import org.inventivetalent.advancedslabs.api.path.IPathManager;
import org.inventivetalent.advancedslabs.editor.BlockEditor;
import org.inventivetalent.advancedslabs.editor.EditorManager;
import org.inventivetalent.advancedslabs.item.ItemListener;
import org.inventivetalent.advancedslabs.item.ItemManager;
import org.inventivetalent.advancedslabs.movement.PathMovementTask;
import org.inventivetalent.advancedslabs.movement.path.PathManager;
import org.inventivetalent.advancedslabs.movement.path.PathParticleTask;
import org.inventivetalent.advancedslabs.movement.path.editor.PathEditor;
import org.inventivetalent.advancedslabs.movement.path.editor.PathEditorManager;
import org.inventivetalent.advancedslabs.slab.FallingBlockResetTask;
import org.inventivetalent.advancedslabs.slab.SlabManager;
import org.inventivetalent.apihelper.APIManager;
import org.inventivetalent.glow.GlowAPI;
import org.inventivetalent.messagebuilder.MessageBuilder;
import org.inventivetalent.messagebuilder.MessageContainer;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

public class AdvancedSlabs extends JavaPlugin implements Listener {

	public static AdvancedSlabs instance;

	public SlabManager       slabManager;
	public PathManager       pathManager;
	public EditorManager     editorManager;
	public PathEditorManager pathEditorManager;
	public ItemManager       itemManager;
	public EntityManager     entityManager;

	public PacketListener packetListener;

	public MessageContainer messages;

	public FallingBlockResetTask fallingBlockResetTask;
	public PathParticleTask      pathParticleTask;
	public PathMovementTask      pathMovementTask;

	File    slabFile   = new File(getDataFolder(), "slabs.json");
	File    pathFile   = new File(getDataFolder(), "paths.json");
	boolean firstStart = !slabFile.exists() || !pathFile.exists();

	public boolean spawningSlab = false;

	SpigetUpdate spigetUpdate;

	protected AdvancedSlabsAPI api = new AdvancedSlabsAPI() {
		@Override
		public ISlabManager getSlabManager() {
			return slabManager;
		}

		@Override
		public IPathManager getPathManager() {
			return pathManager;
		}
	};

	@Override
	public void onLoad() {
		APIManager.require(PacketListenerAPI.class, this);
		APIManager.require(GlowAPI.class, this);
	}

	@Override
	public void onEnable() {
		instance = this;

		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_13_R2)) {
			getLogger().log(Level.WARNING, "This version of the plugin is intended to be used with 1.13.2+! Please use a version older than 1.9.0 for older MC versions.");
		}

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
		if (!pathFile.exists()) {
			try {
				pathFile.createNewFile();
			} catch (Exception e) {
				getLogger().log(Level.SEVERE, "Failed to create path file", e);
			}
		}

		this.slabManager = new SlabManager(this);
		this.pathManager = new PathManager(this);
		this.editorManager = new EditorManager(this);
		this.pathEditorManager = new PathEditorManager(this);
		this.itemManager = new ItemManager(this);
		this.entityManager = new EntityManager(this);

		this.packetListener = new PacketListener(this);

		this.fallingBlockResetTask = new FallingBlockResetTask(this);
		this.fallingBlockResetTask.runTaskTimer(this, 20, 20);

		this.pathParticleTask = new PathParticleTask(this);
		this.pathParticleTask.runTaskTimer(this, 10, 10);

		this.pathMovementTask = new PathMovementTask(this);
		this.pathMovementTask.runTaskTimer(this, 1, 1);

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
				.withMessage("editor.scrollSteps", "§eSteps: &7%s")//
				.withMessage("editor.locationInfo", "§ex: §7%x§e, y: §7%s§e, z: §7%s")//
				.withMessage("highlight", "§aHighlighted nearby slabs")//
				.withMessage("respawn", "§aRespawned nearby slab entities")//
				.withMessage("error.invalidMaterial", "§cInvalid Material: %s")//
				.withMessage("error.notOnline", "§cPlayer %s is not online")//
				.withMessage("editor.path.reset", "§aYou are no longer editing a path")//
				.withMessage("editor.path.point.added", "§aPoint added")//
				.withMessage("editor.path.point.removed", "§cPoint removed")//
				.withMessage("editor.path.start", "§aYou started a new path")//
				.withMessage("editor.path.edit", "§aYou are now editing this path")//
				.withMessage("editor.path.error.notEditing", "§cYou are not editing a path")//
				.withMessage("editor.path.error.notFound", "§cCould not find a path")//
				.withMessage("editor.path.empty", "§7This path is now empty and will be deleted if it stays empty")//
				.withMessage("editor.path.type.format", "§6%s §8- §7%s")//
				.withMessage("editor.path.type.circular.toggle.description", "Go directly to the start when at the end. Repeat until toggled off")//
				.withMessage("editor.path.type.reverse.toggle.description", "Move the path in reversed order when at the end. Repeat until toggled off")//
				.withMessage("editor.path.speed.format", "§aSpeed: §7%s")//
				.withMessage("editor.path.bound", "§aPath bound to slab")//
				.withMessage("editor.path.unbound", "§aPath unbound from slab")//
				;

		this.messages = messageBuilder.fromConfig(getConfig().getConfigurationSection("messages")).build();

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				getLogger().info("Loading data...");
				loadPaths();
				loadSlabs();
			}
		}, 40);

		try {
			new Metrics(this);

			spigetUpdate = new SpigetUpdate(this, 20164).setUserAgent("AdvancedSlabs/" + getDescription().getVersion()).setVersionComparator(VersionComparator.SEM_VER);
			spigetUpdate.checkForUpdate(new UpdateCallback() {
				@Override
				public void updateAvailable(String s, String s1, boolean b) {
					getLogger().info("A new version is available (" + s + "). Download it from https://r.spiget.org/20164");
				}

				@Override
				public void upToDate() {
					getLogger().info("The plugin is up-to-date.");
				}
			});
		} catch (Exception e) {
		}
	}

	public void reload() {
		reloadConfig();
	}

	@Override
	public void onDisable() {
		getLogger().info("Saving data...");
		saveSlabs();
		savePaths();

		if (this.packetListener != null) {
			this.packetListener.disable();
		}
	}

	public AdvancedSlabsAPI getApi() {
		return api;
	}

	@EventHandler
	public void onScroll(PlayerItemHeldEvent event) {
		if (editorManager.isEditing(event.getPlayer().getUniqueId()) || pathEditorManager.isEditing(event.getPlayer().getUniqueId())) {
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

			if (event.getPlayer().isSneaking()) {
				PathEditor pathEditor = pathEditorManager.getEditor(event.getPlayer().getUniqueId());
				if (pathEditor != null) {
					pathEditor.handleScroll(increase, decrease, event.getPlayer().isSneaking());
					return;//Prioritize the path editor
				}
			}

			BlockEditor editor = editorManager.getEditor(event.getPlayer().getUniqueId());
			if (editor != null) {
				editor.handleScroll(increase, decrease, event.getPlayer().isSneaking());
			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if (pathEditorManager.isEditing(event.getPlayer().getUniqueId())) {
			PathEditor editor = pathEditorManager.getEditor(event.getPlayer().getUniqueId());
			if (editor != null) {
				editor.handleDrop(event);
			}
		}
	}

	@EventHandler
	public void on(final PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("advancedslabs.updatecheck")) {
			spigetUpdate.checkForUpdate(new UpdateCallback() {
				@Override
				public void updateAvailable(String s, String s1, boolean b) {
					event.getPlayer().sendMessage("§aA new version for §6AdvancedSlabs §ais available (§7v" + s + "§a). Download it from https://r.spiget.org/20164");
				}

				@Override
				public void upToDate() {
				}
			});
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		editorManager.removeEditor(event.getPlayer().getUniqueId());
		pathEditorManager.removeEditor(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void on(EntityChangeBlockEvent event) {
		if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("advancedslab")) {
			((FallingBlock) event.getEntity()).setTicksLived(1);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void on(EntityDeathEvent event) {
		IAdvancedSlab slab = slabManager.getSlabForEntity(event.getEntity());
		if (slab != null) {
			if (slab.isDespawned()) {
				slabManager.removeSlab(slab);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(CreatureSpawnEvent event) {
		if (spawningSlab) {
			if (event.isCancelled()) {
				if (event.getEntityType() == EntityType.SHULKER) {
					event.setCancelled(false);
				}
			}
		}
	}

	public void saveSlabs() {
		JsonArray slabArray = slabManager.toJson();
		writeJson(slabArray, slabFile);
	}

	public void savePaths() {
		JsonArray pathArray = pathManager.toJson();
		writeJson(pathArray, pathFile);
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

	public void loadPaths() {
		try {
			pathManager.loadJson(new JsonParser().parse(new FileReader(pathFile)).getAsJsonArray());
		} catch (Exception e) {
			if (!firstStart) {
				throw new RuntimeException("Failed to load paths", e);
			}
		}
	}

	void writeJson(JsonElement jsonElement, File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement));
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new RuntimeException("Failed to write json", e);
		}
	}

}
