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
