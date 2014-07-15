package com.lenis0012.bukkit.globalbank.test;

import com.lenis0012.bukkit.globalbank.util.sorting.Sort;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Lenny on 7/15/2014.
 */
public class SortingTest {

    @Test
    public void testAlphabeticSorting() {
        ItemStack[] items = new ItemStack[4];
        items[0] = new ItemStack(Material.LEATHER, 1);
        items[2] = new ItemStack(Material.ANVIL, 1);
        items = Sort.ALPHABETIC.sort(items);
        Assert.assertTrue(items[2] == null);
        Assert.assertTrue(items[3] == null);
        Assert.assertTrue(items[0] != null);
        Assert.assertTrue(items[1] != null);
        if(items[0] != null) {
            Assert.assertTrue(items[0].getType() == Material.ANVIL);
        } if(items[1] != null) {
            Assert.assertTrue(items[1].getType() == Material.LEATHER);
        }
    }
}
