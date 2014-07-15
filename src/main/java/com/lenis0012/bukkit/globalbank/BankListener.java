package com.lenis0012.bukkit.globalbank;

import com.lenis0012.bukkit.globalbank.banker.Banker;
import com.lenis0012.bukkit.npc.NPC;
import com.lenis0012.bukkit.npc.NPCInteractEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by Lenny on 7/14/2014.
 */
public class BankListener implements Listener {
    private final BankPlugin plugin;

    public BankListener(BankPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        final NPC npc = event.getNpc();
        final HumanEntity human = event.getEntity();
        Player player = (Player) human;
        Banker banker = plugin.getBankerManager().getBanker(npc);
        if(banker != null) {

        }
    }
}
