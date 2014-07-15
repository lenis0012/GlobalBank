package com.lenis0012.bukkit.globalbank.util;

import com.lenis0012.bukkit.globalbank.BankPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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
    public static ItemStack item(Material type, int amount, String displayName, String... lore) {
        ItemStack item = new ItemStack(type, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        meta.setLore(lore(lore));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Get the price of an item slot
     *
     * @param slot Slot id
     * @return Price
     */
    public static double getSlotPrice(int slot) {
        BankPlugin plugin = JavaPlugin.getPlugin(BankPlugin.class);
        double base = plugin.getConfig().getDouble("settings.economy.slot-cost");
        double multiplier = plugin.getConfig().getDouble("settings.economy.progressive-multiplier");
        return base + (base * slot * multiplier);
    }

    private static List<String> lore(String... lore) {
        List<String> list = new ArrayList<>();
        for(String str : lore) {
            list.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        return list;
    }
}
