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

import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.ResolverQuery;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;

public class EntityHelper {

	static NMSClassResolver nmsClassResolver = new NMSClassResolver();

	static Class<?> Entity                 = nmsClassResolver.resolveSilent("Entity");
	static Class<?> EntityLiving           = nmsClassResolver.resolveSilent("EntityLiving");
	static Class<?> EntityInsentient       = nmsClassResolver.resolveSilent("EntityInsentient");
	static Class<?> EntityShulker          = nmsClassResolver.resolveSilent("EntityShulker");
	static Class<?> EntityArmorStand       = nmsClassResolver.resolveSilent("EntityArmorStand");
	static Class<?> EntityFallingBlock     = nmsClassResolver.resolveSilent("EntityFallingBlock");
	static Class<?> PathfinderGoalSelector = nmsClassResolver.resolveSilent("PathfinderGoalSelector");

	static MethodResolver EntityMethodResolver           = new MethodResolver(Entity);
	static MethodResolver EntityInsentientMethodResolver = new MethodResolver(EntityInsentient);

	public static FieldResolver EntityInsentientFieldResolver   = new FieldResolver(EntityInsentient);
	public static FieldResolver EntityFallingBlockFieldResolver = new FieldResolver(EntityFallingBlock);

	static ConstructorResolver PathfinderGoalSelectorConstructorResolver = new ConstructorResolver(PathfinderGoalSelector);

	public static void makeSilent(Object entity) {
		try {
			EntityMethodResolver.resolve(new ResolverQuery("setSilent", boolean.class), new ResolverQuery("c", boolean.class)).invoke(Minecraft.getHandle(entity), true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setNoAI(Object entity) {
		try {
			EntityInsentientMethodResolver.resolve(new ResolverQuery("setAI", boolean.class), new ResolverQuery("setNoAI", boolean.class), new ResolverQuery("m", boolean.class)).invoke(Minecraft.getHandle(entity), true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void clearEntityGoals(Object entity) {
		try {
			Object handle = Minecraft.getHandle(entity);
			EntityInsentientFieldResolver.resolve("goalSelector").set(handle, PathfinderGoalSelectorConstructorResolver.resolveFirstConstructor().newInstance(new Object[] { null }));
			EntityInsentientFieldResolver.resolve("targetSelector").set(handle, PathfinderGoalSelectorConstructorResolver.resolveFirstConstructor().newInstance(new Object[] { null }));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setInvulnerable(Object entity) {
		try {
			EntityMethodResolver.resolve(new ResolverQuery("setInvulnerable", boolean.class), new ResolverQuery("h", boolean.class)).invoke(Minecraft.getHandle(entity), true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void addPassenger(Object vehicle, Object passenger) {
		try {
			EntityMethodResolver.resolve(new ResolverQuery("a", Entity, boolean.class)).invoke(Minecraft.getHandle(passenger), Minecraft.getHandle(vehicle), true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void stopRiding(Object entity) {
		try {
			EntityMethodResolver.resolve(new ResolverQuery("stopRiding")).invoke(Minecraft.getHandle(entity));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setPosition(Object entity, double x, double y, double z) {
		try {
			EntityMethodResolver.resolve(new ResolverQuery("setPosition", double.class, double.class, double.class)).invoke(Minecraft.getHandle(entity), x, y, z);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
