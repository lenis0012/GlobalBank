package net.ark3l.globalbank2.banker;

	/*
	GlobalBank2 - RuneScape/WoW style banking for Bukkit
	Copyright (C) 2012  Oliver 'Arkel' Brown

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
	*/

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import net.ark3l.globalbank2.banker.entity.Banker;
import net.ark3l.globalbank2.banker.nms.NPCEntity;
import net.ark3l.globalbank2.banker.nms.NPCNetworkManager;
import net.ark3l.globalbank2.util.Log;
import net.minecraft.server.v1_5_R2.Entity;
import net.minecraft.server.v1_5_R2.PlayerInteractManager;
import net.minecraft.server.v1_5_R2.MinecraftServer;
import net.minecraft.server.v1_5_R2.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R2.CraftServer;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 *
 * @author martin
 */
public class NPCManager {

	private final HashMap<String, Banker> bankers = new HashMap<String, Banker>();
	private final int taskid;
	private final NPCNetworkManager npcNetworkManager;
	private final MinecraftServer mcServer;

	public static JavaPlugin plugin;

	public NPCManager(JavaPlugin plugin) throws IOException {
		mcServer = ((CraftServer)plugin.getServer()).getServer();

		npcNetworkManager = new NPCNetworkManager();
		NPCManager.plugin = plugin;
		taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				HashSet<String> toRemove = new HashSet<String>();
				for (String i : bankers.keySet()) {
					Entity j = bankers.get(i).getEntity();
					j.x();
					if (j.dead) {
						toRemove.add(i);
					}
				}
				for (String n : toRemove) {
					bankers.remove(n);
				}
			}
		}, 1L, 1L);
		Bukkit.getServer().getPluginManager().registerEvents(new SL(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(new WL(), plugin);
	}

	private WorldServer getWorldServer(World world) {
		return ((CraftWorld)world).getHandle();
	}

	public MinecraftServer getMcServer() {
		return mcServer;
	}

	private class SL implements Listener {
		@EventHandler
		public void onPluginDisable(PluginDisableEvent event) {
			if (event.getPlugin() == plugin) {
				despawnAll();
				Bukkit.getServer().getScheduler().cancelTask(taskid);
			}
		}
	}

	private class WL implements Listener {
		@EventHandler
		public void onChunkLoad(ChunkLoadEvent event) {
			for (Banker banker : bankers.values()) {
				if (banker != null && event.getChunk() == banker.getBukkitEntity().getLocation().getBlock().getChunk()) {
					getWorldServer(event.getWorld()).addEntity(banker.getEntity());
				}
			}
		}
	}

	public Banker spawnBanker(Location l, String bankName) {
		String name = "Banker ";
		int i = 0;
		String id = name;
		while (bankers.containsKey(id)) {
			id = name + i;
			i++;
		}
		return spawnBanker(id, l, id, bankName);
	}

	public Banker spawnBanker(String name, Location l, String id, String bankName) {
		if (bankers.containsKey(id)) {
			Log.warning("NPC with that id already exists, existing NPC returned");
			return bankers.get(id);
		} else {
			if (name.length() > 16) { // Check and nag if name is too long, spawn NPC anyway with shortened name.
				String tmp = name.substring(0, 16);
				Log.warning("NPCs can't have names longer than 16 characters,");
				Log.warning(name + " has been shortened to " + tmp);
				name = tmp;
			}
			WorldServer ws = getWorldServer(l.getWorld());
			NPCEntity npcEntity = new NPCEntity(this, ws, name, new PlayerInteractManager(ws));
			npcEntity.setPositionRotation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
			ws.addEntity(npcEntity); //the right way
			Banker npc = new Banker(npcEntity, bankName);
			npc.setYaw(l.getYaw());

			if(Bukkit.getPluginManager().isPluginEnabled("Spout")) {
				SpoutPlayer sp = npc.getSpoutPlayer();
				sp.setSkin("http://dl.dropbox.com/u/18216599/images/bankersskin.png");
				sp.setTitle(ChatColor.GOLD + "Banker\n" + ChatColor.WHITE + "[" + bankName + "]");
			}

			bankers.put(id, npc);
			return npc;
		}
	}

	public void despawnById(String id) {
		Banker npc = bankers.get(id);
		if (npc != null) {
			bankers.remove(id);
			npc.removeFromWorld();
		}
	}


	public void despawnAll() {
		for (Banker npc : bankers.values()) {
			if (npc != null) {
				npc.removeFromWorld();
			}
		}
		bankers.clear();
	}

	public Banker getBanker(String id) {
		return bankers.get(id);
	}

	public boolean isNPC(org.bukkit.entity.Entity e) {
		return ((CraftEntity) e).getHandle() instanceof NPCEntity;
	}

	public String getNPCIdFromEntity(org.bukkit.entity.Entity e) {
		if (e instanceof HumanEntity) {
			for (String i : bankers.keySet()) {
				if (bankers.get(i).getBukkitEntity().getEntityId() == e.getEntityId()) {
					return i;
				}
			}
		}
		return null;
	}

	public NPCNetworkManager getNPCNetworkManager() {
		return npcNetworkManager;
	}

}