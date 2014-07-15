package com.lenis0012.bukkit.globalbank.util.sorting;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Lenny on 7/14/2014.
 */
public enum Sort {
    ALPHABETIC(AlphabeticSort.class);

    private ISort instance;

    private Sort(Class<? extends ISort> iClass) {
        try {
            this.instance = iClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sort an array of items
     *
     * @param contents Items
     * @return sorted Itens.
     */
    public ItemStack[] sort(ItemStack[] contents) {
        return instance.sort(contents);
    }
}
