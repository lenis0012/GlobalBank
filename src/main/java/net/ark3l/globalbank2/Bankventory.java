package net.ark3l.globalbank2;

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

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Bankventory implements Serializable {
	private static final long serialVersionUID = 1L;
	private final HashMap<Integer, BItemStack[]> slots = new HashMap<Integer, BItemStack[]>();
	private ArrayList<Integer> slotIds = new ArrayList<Integer>();

	public Bankventory() {
	}

	public ItemStack[] getItemStacksFromSlot(int slot) {
		if (slots.containsKey(slot)) {
			ItemStack[] itemRet = new ItemStack[52];
			BItemStack[] convert = slots.get(slot);
			for (int i = 0; i < itemRet.length; i++) {
				if (convert[i] == null)
					itemRet[i] = null;
				else {
					BMaterialData data = convert[i].getData();
					if (data == null)
						itemRet[i] = new ItemStack(convert[i].getTypeId(),
								convert[i].getAmount(),
								convert[i].getDurability(), null);
					else
						itemRet[i] = new ItemStack(convert[i].getTypeId(),
								convert[i].getAmount(),
								convert[i].getDurability(), data.getData());

					try {
						if (!convert[i].getEnchantments().isEmpty())
							itemRet[i].addUnsafeEnchantments(convert[i]
									.getEnchantments());
					} catch (NullPointerException e) {
						// Don't worry about it, no enchantments because it
						// loaded an old style file
					}
				}
			}
			return itemRet;

		}
		return new ItemStack[52];
	}

	public void setItemStack(int slot, ItemStack[] is) {
		BItemStack[] convert = new BItemStack[52];
		for (int i = 0; i < convert.length; i++) {
			if (is[i] == null)
				convert[i] = null;
			else {

				MaterialData data = is[i].getData();

				try {
					if (data == null)
						convert[i] = new BItemStack(is[i].getTypeId(),
								is[i].getAmount(), is[i].getDurability(), null,
								is[i].getEnchantments());
					else
						convert[i] = new BItemStack(is[i].getTypeId(),
								is[i].getAmount(), is[i].getDurability(),
								data.getData(), is[i].getEnchantments());
				} catch (NullPointerException e) {
					if (data == null)
						convert[i] = new BItemStack(is[i].getTypeId(),
								is[i].getAmount(), is[i].getDurability(), null,
								new HashMap<Enchantment, Integer>());
					else
						convert[i] = new BItemStack(is[i].getTypeId(),
								is[i].getAmount(), is[i].getDurability(),
								data.getData(),
								new HashMap<Enchantment, Integer>());
				}
			}
		}
		slots.put(slot, convert);
	}

	public ArrayList<Integer> getSlotIds() {
		return slotIds;
	}

	public ArrayList<Integer> setSlotIds(ArrayList<Integer> slotIds) {
		this.slotIds = slotIds;
		return this.slotIds;
	}

}
