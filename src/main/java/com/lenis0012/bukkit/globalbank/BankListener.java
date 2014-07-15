package com.lenis0012.bukkit.globalbank;

import com.lenis0012.bukkit.globalbank.banker.Banker;
import com.lenis0012.bukkit.globalbank.storage.BPlayer;
import com.lenis0012.bukkit.globalbank.util.Simple;
import com.lenis0012.bukkit.npc.NPC;
import com.lenis0012.bukkit.npc.NPCFactory;
import com.lenis0012.bukkit.npc.NPCInteractEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lenny on 7/14/2014.
 */
public class BankListener implements Listener {
    private final BankPlugin plugin;

    public BankListener(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNPCInteract(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final Entity entity =  event.getRightClicked();
        final NPCFactory npcFactory = plugin.getBankerManager().getNPCFactory();
        if(npcFactory.isNPC(entity)) {
            NPC npc = npcFactory.getNPC(entity);
            Banker banker = plugin.getBankerManager().getBanker(npc);
            if (banker != null) {
                BPlayer bPlayer = BPlayer.get(player.getUniqueId());
                switch(bPlayer.getStatus()) {
                    case NONE:
                        bPlayer.openBank(player);
                        bPlayer.setStatus(BPlayer.PlayerStatus.IN_BANK);
                        break;
                    case DELETE:
                        plugin.getBankerManager().deleteBanker(banker);
                        bPlayer.setStatus(BPlayer.PlayerStatus.NONE);
                        player.sendMessage(ChatColor.GREEN + "Banker " + banker.getName() + " has been deleted.");
                        break;
                    case FACE:
                        banker.getNpc().lookAt(player.getEyeLocation());
                        bPlayer.setStatus(BPlayer.PlayerStatus.NONE);
                        player.sendMessage(ChatColor.GREEN + "Banker " + banker.getName() + " now faces at your location.");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final ItemStack item = event.getCurrentItem();
        final BPlayer bPlayer = BPlayer.get(player.getUniqueId());
        final Inventory inventory = event.getInventory();
        if(bPlayer.getStatus() == BPlayer.PlayerStatus.IN_BANK) {
            final int slot = Integer.parseInt(item.getItemMeta().getDisplayName().substring("Slot ".length())) - 1;
            event.setCancelled(true);
            switch(item.getType()) {
                case CHEST:
                    player.closeInventory();
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            bPlayer.openSlot(player, slot);
                            bPlayer.setStatus(BPlayer.PlayerStatus.IN_SLOT);
                        }
                    }, 2L);
                    break;
                case PAPER:
                    player.closeInventory();
                    Economy economy = plugin.getEconomy();
                    if(economy != null) {
                        if(slot <= bPlayer.getOwnedSlots()) {
                            double price = Simple.getSlotPrice(slot);
                            if (economy.has(player, price)) {
                                economy.withdrawPlayer(player, price);
                                bPlayer.setOwnedSlots(bPlayer.getOwnedSlots() + 1);
                                player.sendMessage(ChatColor.GREEN + "You have vought slot " + (slot + 1) + " for $" + price);
                            } else {
                                player.sendMessage(ChatColor.RED + "You don't have enough money to afford a new slot.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You need to unlock slot " + (bPlayer.getOwnedSlots() + 1) + " first.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You are not able tp purchase bank slots in this server.");
                    }
                default:
                    break;
            }
        } else if(bPlayer.getStatus() == BPlayer.PlayerStatus.IN_SLOT) {
            if(item.getType() == Material.PAPER && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals("Sort items")) {
                event.setCancelled(true);
                ItemStack[] items = new ItemStack[inventory.getSize() - 2];
                for(int i = 0; i < items.length; i++) {
                    items[i] = inventory.getItem(i + 2);
                }

                items = plugin.getSort().sort(items);
                for(int i = 0; i < items.length; i++) {
                    inventory.setItem(i + 2, items[i]);
                }

                player.updateInventory();
                player.sendMessage(ChatColor.GREEN + "Your bank slot has been sorted.");
            } else if(item.getType() == Material.CHEST && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals("Back to bank")) {
                player.closeInventory();
                Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

                    @Override
                    public void run() {
                        bPlayer.openBank(player);
                        bPlayer.setStatus(BPlayer.PlayerStatus.IN_BANK);
                    }
                }, 2L);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        final Inventory inventory = event.getInventory();
        BPlayer bPlayer = BPlayer.get(player.getUniqueId());
        if(bPlayer.getStatus() == BPlayer.PlayerStatus.IN_SLOT) {
            int slot = Integer.parseInt(inventory.getTitle().substring("Slot ".length())) - 1;
            bPlayer.closeSlot(event.getInventory(), slot);
            bPlayer.setStatus(BPlayer.PlayerStatus.NONE);
        } else if(bPlayer.getStatus() == BPlayer.PlayerStatus.IN_BANK) {
            bPlayer.setStatus(BPlayer.PlayerStatus.NONE);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        BPlayer.save(player.getUniqueId());
    }
}
