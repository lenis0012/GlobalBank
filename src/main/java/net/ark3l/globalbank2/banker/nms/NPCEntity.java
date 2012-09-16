package net.ark3l.globalbank2.banker.nms;

import net.ark3l.globalbank2.banker.NPCManager;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EnumGamemode;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.WorldServer;

/**
 *
 * @author martin
 */
public class NPCEntity extends EntityPlayer {

	public NPCEntity(NPCManager npcManager, WorldServer world, String s, ItemInWorldManager itemInWorldManager) {
		super(npcManager.getMcServer(), world, s, itemInWorldManager);

		itemInWorldManager.b(EnumGamemode.a(0));

		netServerHandler = new NPCNetHandler(npcManager, this);
		// fake sleeping
		fauxSleeping = true;
	}

	@Override
	public void move(double arg0, double arg1, double arg2) {
		setPosition(arg0, arg1, arg2);
	}

	public void setBukkitEntity(org.bukkit.entity.Entity entity) {
		bukkitEntity = entity;
	}

}