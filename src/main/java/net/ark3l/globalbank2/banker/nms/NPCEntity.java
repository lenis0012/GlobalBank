package net.ark3l.globalbank2.banker.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;

import net.ark3l.globalbank2.banker.NPCManager;
import net.minecraft.server.v1_5_R2.EntityPlayer;
import net.minecraft.server.v1_5_R2.EnumGamemode;
import net.minecraft.server.v1_5_R2.PlayerInteractManager;
import net.minecraft.server.v1_5_R2.WorldServer;

/**
 *
 * @author martin
 */
public class NPCEntity extends EntityPlayer {
	private float forceYaw;
	private float forcePitch;
	
	public NPCEntity(NPCManager npcManager, WorldServer world, String s, PlayerInteractManager itemInWorldManager) {
		super(npcManager.getMcServer(), world, s, itemInWorldManager);

		itemInWorldManager.b(EnumGamemode.a(0));

		this.playerConnection = new NPCNetHandler(npcManager, this);
		// fake sleeping
		fauxSleeping = true;
		this.forceYaw = 0F;
		this.forcePitch = 0F;
	}

	@Override
	public void move(double arg0, double arg1, double arg2) {
		setPosition(arg0, arg1, arg2);
	}

	public void setBukkitEntity(org.bukkit.entity.Entity entity) {
		bukkitEntity = (CraftEntity) entity;
	}
	
	@Override
	public void l_() {
		super.l_();
		
		this.yaw = forceYaw;
		this.az = forceYaw;
		this.pitch = forcePitch;
	}
	
	public void setYaw(float yaw) {
		this.forceYaw = yaw;
	}
	
	public void setPitch(float pitch) {
		this.forcePitch = pitch;
	}
	
	public Location getLocation() {
		return new Location(this.world.getWorld(), this.locX, this.locY, this.locZ, this.forceYaw, this.forcePitch);
	}
}