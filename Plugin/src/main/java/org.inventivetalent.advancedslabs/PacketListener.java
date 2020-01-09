/*
 *
 */

package org.inventivetalent.advancedslabs;

import org.bukkit.Bukkit;
import org.inventivetalent.advancedslabs.api.IAdvancedSlab;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.PacketOptions;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;
import org.inventivetalent.reflection.minecraft.Minecraft;

import java.util.UUID;

public class PacketListener {

	private PacketHandler packetHandler;

	boolean use13 = Minecraft.VERSION.newerThan(Minecraft.Version.v1_13_R1);

	public PacketListener(AdvancedSlabs plugin) {
		this.packetHandler = new PacketHandler(plugin) {

			@Override
			@PacketOptions(forcePlayer = true)
			public void onSend(SentPacket sentPacket) {
				if ("PacketPlayOutSpawnEntityLiving".equals(sentPacket.getPacketName())) {
					int c = (int) sentPacket.getPacketValue("c");
					if ((use13 && c == 59) || c == 69) {//Shulker
						UUID b = (UUID) sentPacket.getPacketValue("b");
						final IAdvancedSlab slab = AdvancedSlabs.instance.slabManager.getSlabForUUID(b);
						if (slab != null) {
							Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
								@Override
								public void run() {
									slab.refreshEntities();
									slab.respawnFallingBlock();
								}
							}, 10);
						}
					}
				}
			}

			@Override
			public void onReceive(ReceivedPacket receivedPacket) {
			}
		};
		PacketHandler.addHandler(this.packetHandler);
	}

	public void disable() {
		if (this.packetHandler != null) {
			PacketHandler.removeHandler(this.packetHandler);
		}
	}

}
