package net.ark3l.globalbank2.listeners;

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

import net.ark3l.globalbank2.GlobalBank;
import net.ark3l.globalbank2.banker.entity.Banker;
import net.ark3l.globalbank2.util.SqliteDB;
import net.minecraft.server.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class BEntityListener implements Listener {
	public final GlobalBank b;

	public BEntityListener(GlobalBank b) {
		this.b = b;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent entityDamageEvent) {
		if (!(entityDamageEvent instanceof EntityDamageByEntityEvent)) {
			return;
		}

		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) entityDamageEvent;

		if (b.manager.isNPC(event.getEntity())) {
			if(!(event.getDamager() instanceof EntityPlayer)) return;

			Player player = (Player)  event.getDamager();
			if (b.playersDeletingBankers.contains(player)) {
				Banker banker = b.manager.getBanker(b.manager.getNPCIdFromEntity(event.getEntity()));
				SqliteDB.delBanker(banker.bankName);
				b.manager.despawnById(b.manager.getNPCIdFromEntity(banker.getBukkitEntity()));
				player.sendMessage(ChatColor.BLUE + "[GlobalBank2]"
						+ ChatColor.WHITE
						+ " Banker has been removed.");
				b.playersDeletingBankers.remove(player);
			} else if(b.playersChangingBankersDirection.contains(player)) {
				Banker banker = b.manager.getBanker(b.manager.getNPCIdFromEntity(event.getEntity()));
				banker.turnToFace(player.getLocation());
				SqliteDB.delBanker(banker.bankName);
				SqliteDB.newBanker(banker.bankName, banker.getBukkitEntity().getLocation());
				b.playersChangingBankersDirection.remove(player);
			}
			event.setCancelled(true);
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() == null)
			return;
		if (b.manager.isNPC(event.getTarget())) {
			event.setCancelled(true);
		}

	}
}
