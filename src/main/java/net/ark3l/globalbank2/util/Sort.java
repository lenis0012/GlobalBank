package net.ark3l.globalbank2.util;

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

import org.bukkit.inventory.ItemStack;

/**
 * @author Faye 'Veralapsa' Bickerton
 * Modded for GlobalBank2 by Arkel
 */
public class Sort {

	public ItemStack[] sortItemStack(ItemStack[] stack) {
		return sortItemStack(stack, 0, stack.length);
	}

	public ItemStack[] sortItemStack(ItemStack[] stack, int start, int end) {
		stack = stackItems(stack, start, end);

		recQuickSort(stack, start, end - 1);
		return stack;
	}

	private ItemStack[] stackItems(ItemStack[] items, int start, int end) {
		for (int i = start; i < end; i++) {
			ItemStack item = items[i];

			// Avoid infinite stacks and stacks with durability
			if (item == null || item.getAmount() <= 0 || ItemType.shouldNotStack(item.getTypeId())) {
				continue;
			}

			if (item.getAmount() < 64) {
				int needed = 64 - item.getAmount(); // Number of needed items
				// until 64

				// Find another stack of the same type
				for (int j = i + 1; j < end; j++) {
					ItemStack item2 = items[j];

					// Avoid infinite stacks and stacks with durability
					if (item2 == null || item2.getAmount() <= 0
							|| ItemType.shouldNotStack(item.getTypeId())) {
						continue;
					}

					// Same type?
					// Blocks store their color in the damage value
					if (item2.getTypeId() == item.getTypeId()
							&& (!ItemType.usesDamageValue(item.getTypeId()) || item
							.getDurability() == item2.getDurability())) {
						// This stack won't fit in the parent stack
						if (item2.getAmount() > needed) {
							item.setAmount(64);
							item2.setAmount(item2.getAmount() - needed);
							break;
							// This stack will
						} else {
							item.setAmount(item.getAmount() + item2.getAmount());
							needed = 64 - item.getAmount();
							items[j].setTypeId(0);
						}
					}
				}
			}
		}
		return items;
	}

	private void swap(ItemStack[] list, int first, int second) {
		ItemStack temp;
		temp = list[first];
		list[first] = list[second];
		list[second] = temp;
	}

	private class ComparableIS {

		private ItemStack item;

		public ComparableIS(ItemStack item) {
			this.item = item;
		}

		public int compareTo(ItemStack check) {
			// Type ID first
			if (item == null && check != null) {
				return -1;
			} else if (item != null && check == null) {
				return 1;
			} else if (item == null) {
				return 0;
			} else if (item.getTypeId() > check.getTypeId()) {
				return 1;
			} else if (item.getTypeId() < check.getTypeId()) {
				return -1;
			} else if (item.getTypeId() == check.getTypeId()) {
				// Wool, dye, slabs, and logs next
				if (ItemType.usesDamageValue(item.getTypeId())) {
					if (item.getDurability() < check.getDurability()) {
						return 1;
					} else if (item.getDurability() > check.getDurability()) {
						return -1;
					}
				}
				// Stack size
				if (item.getAmount() < check.getAmount()) {
					return -1;
				} else if (item.getAmount() > check.getAmount()) {
					return 1;
				}
			}
			return 0;
		}
	}

	private int partition(ItemStack[] list, int first, int last) {
		ItemStack pivot;

		int smallIndex;

		swap(list, first, (first + last) / 2);

		pivot = list[first];
		smallIndex = first;

		for (int index = first + 1; index <= last; index++) {
			ComparableIS compElem = new ComparableIS(list[index]);

			if (compElem.compareTo(pivot) < 0) {
				smallIndex++;
				swap(list, smallIndex, index);
			}

		}

		swap(list, first, smallIndex);

		return smallIndex;

	}

	private void recQuickSort(ItemStack[] list, int first, int last) {
		if (first < last) {
			int pivotLocation = partition(list, first, last);
			recQuickSort(list, first, pivotLocation - 1);
			recQuickSort(list, pivotLocation + 1, last);
		}
	}

}