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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DelayedBank implements Runnable {
	private final Player p;
	private final GlobalBank b;
	private final int maxSlots;

	public DelayedBank(Player p, GlobalBank b, int maxSlots) {
		this.p = p;
		this.b = b;
		this.maxSlots = maxSlots;
	}

	public void run() {
		b.removeContents(p);
		ItemStack[] content = new ItemStack[54];
		ArrayList<ItemStack> iss = new ArrayList<ItemStack>();
		for (int i = 0; (i < content.length); i++) {
			ItemStack is;
			if(i < maxSlots) // fill with chests only up to the max slots for the player
		        is = new ItemStack(Material.CHEST, i + 1);
			else
				is = new ItemStack(Material.STONE);

			content[i] = is;
			iss.add(is);
		}
		b.isk.put(p, iss);
		String s = p.getName();
		if (p.getName().length() > 11)
			s = s.substring(0, 10);
		Inventory inv = b.getServer().createInventory(p, 54, s);
		inv.setContents(content);
		p.openInventory(inv);
		//CustomInventory ci = new CustomInventory(content, "Bank:" + s);
		//p.openInventoryWindow(ci);
		PlayerState.getPlayerState(p).setPs(PlayerStatus.CHEST_SELECT);
	}

}
