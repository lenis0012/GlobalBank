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

import java.util.logging.Level;

import net.ark3l.globalbank2.GlobalBank;
import net.ark3l.globalbank2.PlayerState;
import net.ark3l.globalbank2.PlayerState.PlayerStatus;
import net.ark3l.globalbank2.delayedTasks.InventoryClose;
import net.ark3l.globalbank2.methods.SimpleMethods;
import net.minecraft.server.v1_5_R2.EntityPlayer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class BInventoryListener implements Listener {
	private final GlobalBank b;

	public BInventoryListener(GlobalBank b) {
		this.b = b;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClose(InventoryCloseEvent e) {
		CraftPlayer craftPlayer = (CraftPlayer) e.getPlayer();
		EntityPlayer entityPlayer = craftPlayer.getHandle();

		b.getLogger().log(Level.INFO, (entityPlayer == null) + "/" + (entityPlayer.activeContainer == entityPlayer.defaultContainer)+"/"+craftPlayer.getOpenInventory().getTitle());
		if (!(e.getPlayer() instanceof Player)) return;
		int i = PlayerState.getPlayerState((Player) e.getPlayer()).getSlot();
		b.getServer().getScheduler().scheduleSyncDelayedTask(b, new InventoryClose((Player) e.getPlayer(), e.getInventory(), i));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) return;
		Player p = ((Player) e.getWhoClicked());
		PlayerStatus ps = PlayerState.getPlayerState(p).getPs();
		if (e.getCurrentItem() == null)
			return;
		if (!b.isk.containsKey(p))
			return;
		if (b.isk.get(p).contains(e.getCurrentItem())) {
			if (ps.equals(PlayerStatus.CHEST_SELECT)) {
				if (e.getCurrentItem().getType() == Material.CHEST) {
					// player has selected a slot in the slot selection screen
					e.setCancelled(SimpleMethods.handleBank(b, p, e.getSlot()));
				} else {
					e.setCancelled(true);
				}
			} else if (ps.equals(PlayerStatus.SLOT)) {
				// player has done something inside the slot
				e.setCancelled(SimpleMethods.handleSlot(e.getCurrentItem(), p,
						e.getInventory(), b));
			}

		} else {
			if (e.isShiftClick()) {
				if (ps.equals(PlayerStatus.CHEST_SELECT)) {
					e.setCancelled(true);
				} else if ((e.getCurrentItem().getType() == Material.CHEST || e
						.getCurrentItem().getType() == Material.PAPER)
						&& ps.equals(PlayerStatus.SLOT)) {
					e.setCancelled(true);
				}
			}
		}

	}

}
