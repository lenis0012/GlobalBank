package com.lenis0012.bukkit.globalbank.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Lenny on 7/15/2014.
 */
public class Simple {

    /**
     * Get an item with custom display name
     *
     * @param type Item type
     * @param amount Item amount
     * @param displayName Item name
     * @return Item
     */
    public static ItemStack item(Material type, int amount, String displayName) {
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        item.setItemMeta(meta);
        return item;
    }
}
