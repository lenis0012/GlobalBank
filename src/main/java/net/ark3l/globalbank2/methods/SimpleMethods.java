package net.ark3l.globalbank2.methods;

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

import net.ark3l.globalbank2.Bankventory;
import net.ark3l.globalbank2.GlobalBank;
import net.ark3l.globalbank2.PlayerState;
import net.ark3l.globalbank2.delayedTasks.DelayedBank;
import net.ark3l.globalbank2.delayedTasks.DelayedSlot;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SimpleMethods {

	public static void openSlot(GlobalBank b, Player p, int i) {
		b.getServer().getScheduler().scheduleSyncDelayedTask(b, new DelayedSlot(p, i, b));
	}

	public static void openBank(GlobalBank b, Player p) {
		b.getServer().getScheduler().scheduleSyncDelayedTask(b, new DelayedBank(p, b, getMaxSlotsForPlayer(p)));
	}

	public static boolean handleSlot(ItemStack i, Player p, Inventory inv,
	                                 GlobalBank b) {
		if (i.getType() == Material.CHEST) {
			SlotDataMethods.saveBank(p, inv.getContents().clone(), PlayerState
					.getPlayerState(p).getSlot());
			SimpleMethods.openBank(b, p);
			return true;
		} else if (i.getType() == Material.PAPER) {
			ItemStack[] is = b.sort.sortItemStack(inv.getContents().clone(), 2, inv.getSize());
			inv.setContents(is);

			p.sendMessage(ChatColor.BLUE + "[B]" + ChatColor.WHITE
					+ " Slot " + PlayerState.getPlayerState(p).getSlot()
					+ " Sorted.");

			SlotDataMethods.saveBank(p, inv.getContents().clone(), PlayerState
					.getPlayerState(p).getSlot());
			SimpleMethods.openSlot(b, p, PlayerState.getPlayerState(p)
					.getSlot() - 1);

			return true;
		}
		return false;
	}

	public static boolean handleBank(GlobalBank b, Player p, int slot) {
		if (b.economy == null) {
			SimpleMethods.openSlot(b, p, slot);
			return true;
		}
		Bankventory ba = MiscMethods.getAccount(p);
		for (int z = 1; z <= (b.settings.startWithSlots); z++) {
			ba.getSlotIds().add(z);
		}
		if (ba.getSlotIds().contains((slot + 1))) {
			SimpleMethods.openSlot(b, p, slot);
			PlayerState.getPlayerState(p).setBuyingSlot(0);
			return true;
		} else if (PlayerState.getPlayerState(p).getBuyingSlot() == (slot + 1)) {
			EconomyResponse r = b.economy.withdrawPlayer(p.getName(), SimpleMethods.costOfSlot(slot));
			if (r.transactionSuccess()) {
				p.sendMessage(ChatColor.BLUE + "[B]" + ChatColor.WHITE
						+ " Slot Purchased!");
				ba.getSlotIds().add(slot + 1);
				SimpleMethods.openSlot(b, p, slot);
				PlayerState.getPlayerState(p).setBuyingSlot(0);
				return true;
			} else {
				PlayerState.getPlayerState(p).setBuyingSlot(0);
				p.sendMessage(ChatColor.BLUE + "[B]" + ChatColor.WHITE
						+ " Error occured: " + r.errorMessage);
				return true;
			}
		} else {
			PlayerState.getPlayerState(p).setBuyingSlot(slot + 1);
			p.sendMessage(ChatColor.BLUE + "[B]" + ChatColor.WHITE
					+ " Click Slot again to purchase.");
			p.sendMessage(ChatColor.BLUE + "[B]" + ChatColor.WHITE
					+ " Cost: " + SimpleMethods.costOfSlot(slot));
			return true;
		}
	}

	public static double costOfSlot(int slot) {
		return (GlobalBank.plugin.settings.costPerSlot * (GlobalBank.plugin.settings.multiplier * (slot - GlobalBank.plugin.settings.startWithSlots)));
	}

	public static int getMaxSlotsForPlayer(Player player) {
		if(player.hasPermission("gb.slots.54")) {
			return 54;
		} else if(player.hasPermission("gb.slots.45")) {
			return 45;
		} else if(player.hasPermission("gb.slots.36")) {
			return 36;
		} else if(player.hasPermission("gb.slots.27")) {
			return 27;
		} else if(player.hasPermission("gb.slots.18")) {
			return 18;
		} else if(player.hasPermission("gb.slots.9")) {
			return 9;
		} else {
			return 1;
		}
	}
}
