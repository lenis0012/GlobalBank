package net.ark3l.globalbank2.banker.entity;

/*
   GlobalBank2 - RuneScape/WoW style banking for Bukkit
   Copyright (C) 2012  Oliver 'Arkel' Brown

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
   */

import net.ark3l.globalbank2.banker.nms.NPCEntity;
import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
//import org.getspout.spout.player.SpoutCraftPlayer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class Banker {

	public final String bankName;
	private final Entity entity;

	public Banker(Entity entity, String bankName) {
		this.entity = entity;
		this.bankName = bankName;
		setItemInHand(Material.PAPER, (short) 0);
	}

	public SpoutPlayer getSpoutPlayer() {
		try {
			Class.forName("org.getspout.spout.Spout");

			/*if (!(getEntity().getBukkitEntity() instanceof SpoutCraftPlayer)) {
				((NPCEntity) getEntity()).setBukkitEntity(new SpoutCraftPlayer((CraftServer) Bukkit.getServer(), (EntityPlayer) getEntity()));
			}*/

			return (SpoutPlayer) getEntity().getBukkitEntity();
		} catch (ClassNotFoundException e) {
			Bukkit.getServer().getLogger().warning("Cannot get spout player without spout installed");
		}
		return null;
	}

	public void setItemInHand(Material m, short damage) {
		((HumanEntity) getEntity().getBukkitEntity()).setItemInHand(new ItemStack(m, 1, damage));
	}

	public Entity getEntity() {
		return entity;
	}

	public void removeFromWorld() {
		try {
			entity.world.removeEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public org.bukkit.entity.Entity getBukkitEntity() {
		return entity.getBukkitEntity();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Banker && getBukkitEntity().getEntityId() == ((Banker) obj).getBukkitEntity().getEntityId();
	}

	public void turnToFace(Location point) {
		if (getEntity().getBukkitEntity().getWorld() != point.getWorld()) {
			return;
		}
		Location npcLoc = ((LivingEntity) getEntity().getBukkitEntity()).getEyeLocation();
		double xDiff = point.getX() - npcLoc.getX();
		double zDiff = point.getZ() - npcLoc.getZ();
		double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
		double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
		if (zDiff < 0.0) {
			newYaw = newYaw + Math.abs(180 - newYaw) * 2;
		}
		setYaw((float) (newYaw - 90));
	}

	public void setYaw(float yaw) {
		getEntity().yaw = yaw;
		((EntityPlayer)getEntity()).az = yaw;
	}
}
