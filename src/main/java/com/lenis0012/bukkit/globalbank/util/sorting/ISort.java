package com.lenis0012.bukkit.globalbank.util.sorting;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Lenny on 7/14/2014.
 */
public interface ISort {

    /**
     * Sort contents
     *
     * @param items
     * @return
     */
    public ItemStack[] sort(ItemStack[] items);
}
