package com.lenis0012.bukkit.globalbank.util.sorting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lenny on 7/14/2014.
 */
public class AlphabeticSort implements ISort {
    List<Material> alphabeticMaterialList = new ArrayList<>();

    public AlphabeticSort() {
        List<Material> list = new ArrayList<>(Arrays.asList(Material.values()));
        while(list.size() > 0) {
            Material highest = list.get(0);
            for (Material material : list) {
                if(material.toString().compareTo(highest.toString()) < 0) {
                    highest = material;
                }
            }

            alphabeticMaterialList.add(highest);
            list.remove(highest);
        }

        alphabeticMaterialList.remove(Material.AIR);
    }

    @Override
    public ItemStack[] sort(ItemStack[] items) {
        ItemStack[] contents = new ItemStack[items.length];
        int index = 0;

        List<ItemStack> itemList = Arrays.asList(items);
        for(Material mat : alphabeticMaterialList) {
            ItemStack current = new ItemStack(mat, 0);
            for(ItemStack it : itemList) {
                if(it != null && it.getType().equals(mat)) {
                    if (current.getAmount() + it.getAmount() > mat.getMaxStackSize()) {
                        int remaining = mat.getMaxStackSize() - current.getAmount();
                        current.setAmount(mat.getMaxStackSize());
                        contents[index] = current;
                        index += 1;

                        current = new ItemStack(mat, it.getAmount() - remaining);
                    } else {
                        current.setAmount(current.getAmount() + it.getAmount());
                    }
                }
            }

            if(current.getAmount() > 0) {
                contents[index] = current;
                index += 1;
            }
        }

        return contents;
    }
}
