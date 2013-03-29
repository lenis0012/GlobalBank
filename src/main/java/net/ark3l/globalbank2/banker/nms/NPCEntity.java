package net.ark3l.globalbank2.banker.nms;

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

	public NPCEntity(NPCManager npcManager, WorldServer world, String s, PlayerInteractManager itemInWorldManager) {
		super(npcManager.getMcServer(), world, s, itemInWorldManager);

		itemInWorldManager.b(EnumGamemode.a(0));

		this.playerConnection = new NPCNetHandler(npcManager, this);
		// fake sleeping
		fauxSleeping = true;
	}

	@Override
	public void move(double arg0, double arg1, double arg2) {
		setPosition(arg0, arg1, arg2);
	}

	public void setBukkitEntity(org.bukkit.entity.Entity entity) {
		bukkitEntity = (CraftEntity) entity;
	}

}