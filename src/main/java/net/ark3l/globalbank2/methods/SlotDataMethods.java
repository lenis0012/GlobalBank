package net.ark3l.globalbank2.methods;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SlotDataMethods {

	public static void saveBank(Player p, ItemStack[] is, int slot) {
		is = removeFirst2(is);
		MiscMethods.getAccount(p).setItemStack(slot, is);
		MiscMethods.saveAll();
	}

	public static ItemStack[] removeFirst2(ItemStack[] is) {
		ItemStack[] sendMe = new ItemStack[52];
		System.arraycopy(is, 2, sendMe, 0, 54 - 2);
		return sendMe;
	}

	public static ItemStack[] getAccContent(Player player, int slot) {
		return MiscMethods.getAccount(player).getItemStacksFromSlot(slot);
	}

}
