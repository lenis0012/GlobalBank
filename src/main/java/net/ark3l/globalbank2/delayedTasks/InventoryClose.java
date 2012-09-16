package net.ark3l.globalbank2.delayedTasks;

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
import net.ark3l.globalbank2.PlayerState;
import net.ark3l.globalbank2.PlayerState.PlayerStatus;
import net.ark3l.globalbank2.methods.SlotDataMethods;
import net.minecraft.server.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryClose implements Runnable {
	private final Player p;
	private final Inventory Inv;
	private final int slot;

	public InventoryClose(Player p, Inventory t, int slot) {
		this.p = p;
		this.Inv = t;
		this.slot = slot;
	}


	public void run() {
		CraftPlayer craftPlayer = (CraftPlayer) p;
		EntityPlayer entityPlayer = craftPlayer.getHandle();
		//p.sendMessage((entityPlayer == null) + "/" + (entityPlayer.activeContainer == entityPlayer.defaultContainer)+"/"+craftPlayer.getOpenInventory().getTitle());
		if (entityPlayer == null || entityPlayer.activeContainer == entityPlayer.defaultContainer) {
			PlayerState.getPlayerState(p).setBuyingSlot(0);
			if (PlayerState.getPlayerState(p).getPs() == PlayerStatus.SLOT) {
				SlotDataMethods.saveBank(p, Inv.getContents().clone(), slot);
				p.sendMessage(ChatColor.BLUE + "[B] " + ChatColor.WHITE
						+ GlobalBank.plugin.settings.getStringValue("Strings.Closed", "Have a great day!"));
				PlayerState.getPlayerState(p).setPs(PlayerStatus.DEFAULT);
			} else if (PlayerState.getPlayerState(p).getPs() == PlayerStatus.CHEST_SELECT) {
				p.sendMessage(ChatColor.BLUE + "[B] " + ChatColor.WHITE
						+ GlobalBank.plugin.settings.getStringValue("Strings.Closed", "Have a great day!"));
				PlayerState.getPlayerState(p).setPs(PlayerStatus.DEFAULT);
				GlobalBank.plugin.removeContents(p);
			}

		}
	}

}
