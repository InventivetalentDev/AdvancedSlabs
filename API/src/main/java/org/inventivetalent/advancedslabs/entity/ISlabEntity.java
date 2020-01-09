/*
 *
 */

package org.inventivetalent.advancedslabs.entity;

import org.bukkit.Location;

import java.util.UUID;

public interface ISlabEntity {

	UUID getUniqueId();

	void setTicksLived(int ticksLived);

	void setCustomName(String customName);

	void setCustomNameVisible(boolean customNameVisible);

	boolean isDead();

	void teleport(Location location);

	void remove();

	void eject();

	void stopRiding();

	void setRiding(Object vehicle);

	void allowDeath();

	void denyDeath();

}
