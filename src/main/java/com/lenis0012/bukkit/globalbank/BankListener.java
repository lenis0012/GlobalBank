package com.lenis0012.bukkit.globalbank;

import com.lenis0012.bukkit.globalbank.banker.Banker;
import com.lenis0012.bukkit.globalbank.storage.BPlayer;
import com.lenis0012.bukkit.npc.NPC;
import com.lenis0012.bukkit.npc.NPCFactory;
import com.lenis0012.bukkit.npc.NPCInteractEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

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
                bPlayer.openBank(player);
                bPlayer.setStatus(BPlayer.PlayerStatus.IN_BANK);
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
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        BPlayer.save(player.getUniqueId());
    }
}
