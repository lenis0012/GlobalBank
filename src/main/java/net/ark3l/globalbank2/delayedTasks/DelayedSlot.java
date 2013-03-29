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

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DelayedSlot implements Runnable {
	private final int i;
	private final Player p;
	private final GlobalBank b;

	public DelayedSlot(Player p, int i, GlobalBank b) {
		this.p = p;
		this.i = i;
		this.b = b;
	}

	public void run() {
		b.removeContents(p);
		ItemStack[] content = new ItemStack[54];
		ArrayList<ItemStack> iss = new ArrayList<ItemStack>();
		ItemStack ChestBack = new ItemStack(Material.CHEST);
		ItemStack PaperSort = new ItemStack(Material.PAPER);
		iss.add(ChestBack);
		iss.add(PaperSort);
		b.isk.put(p, iss);
		content[0] = ChestBack;
		content[1] = PaperSort;
		ItemStack[] is;
		if (SlotDataMethods.getAccContent(p, i + 1) != null) {
			is = SlotDataMethods.getAccContent(p, i + 1).clone();
			for (int i = 0; i < is.length; i++) {
				if (is[i] != null) {
					content[i + 2] = is[i].clone();
				} else {
					content[i + 2] = null;
				}
			}
		}

		Inventory inv = Bukkit.createInventory(p, 54, "Slot: "+(i+1));
		//Inventory inv = b.getServer().createInventory(p, 54);
		inv.setContents(content);
		//p.closeInventory();
		p.openInventory(inv);
		//CustomInventory ci = new CustomInventory(content.clone(), "Slot: " + (i + 1));
		//sp.openInventoryWindow(ci);
		PlayerState.getPlayerState(p).setPs(PlayerStatus.SLOT);
		PlayerState.getPlayerState(p).setSlot(i + 1);
	}

}
