package com.lenis0012.bukkit.globalbank.util.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
        LinkedHashMap<ItemStack, LinkedList<Integer>> itemMap = new LinkedHashMap<ItemStack, LinkedList<Integer>>();
        Map<ItemStack, Map<Integer, ItemStack>> itemStackMap = new HashMap<ItemStack, Map<Integer, ItemStack>>();

        for(Material mat : alphabeticMaterialList) {
            for(ItemStack it : itemList) {
                if(it != null && it.getType().equals(mat) && it.getAmount() > 0) {
                    LinkedList<Integer> currents;
                    if(itemMap.containsKey(getEmptyItemStack(it))) {
                        currents = itemMap.get(getEmptyItemStack(it));
                    } else {
                        currents = new LinkedList<Integer>();
                    }

                    if(currents.size() == 0 || it.getAmount() >= mat.getMaxStackSize() ||currents.getLast() >= mat.getMaxStackSize()) {
                        currents.add(it.getAmount());
                    } else {
                        int remaining;

                        if(currents.getLast() + it.getAmount() > mat.getMaxStackSize()) {
                            remaining = currents.getLast() + it.getAmount();
                            currents.removeLast();

                            do {
                                remaining = remaining - mat.getMaxStackSize();

                                currents.add(mat.getMaxStackSize());
                            } while(remaining > mat.getMaxStackSize());
                        } else {
                            remaining = currents.getLast() + it.getAmount();
                            currents.removeLast();
                        }

                        if(remaining > 0) {
                            currents.add(remaining);
                        }
                    }

                    itemMap.put(getEmptyItemStack(it), currents);
                    Map<Integer, ItemStack> sizes = itemStackMap.get(getEmptyItemStack(it));

                    if(sizes == null) {
                        sizes = new HashMap<Integer, ItemStack>();
                    }

                    sizes.put(it.getAmount(), it);
                    itemStackMap.put(getEmptyItemStack(it), sizes);
                }
            }
        }

        for(Entry<ItemStack, LinkedList<Integer>> entry : itemMap.entrySet()) {
            LinkedList<Integer> currents = entry.getValue();

            Collections.sort(currents);
            Collections.reverse(currents);

            for(int amount : currents) {
                Map<Integer, ItemStack> sizes = itemStackMap.get(getEmptyItemStack(entry.getKey()));

                if(sizes.containsKey(amount)) {
                    contents[index] = sizes.get(amount);
                } else {
                    entry.getKey().setAmount(amount);
                    contents[index] = entry.getKey();
                }

                index++;
            }
        }

        return contents;
    }

    public ItemStack getEmptyItemStack(ItemStack item) {
        ItemStack emptyItemStack = new ItemStack(item);

        // Not really empty but setting amount to 0 causes Bukkit to change the material to AIR.
        emptyItemStack.setAmount(1);

        return emptyItemStack;
    }
}
