package net.ark3l.globalbank2;

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

import net.ark3l.globalbank2.banker.NPCManager;
import net.ark3l.globalbank2.listeners.BEntityListener;
import net.ark3l.globalbank2.listeners.BInventoryListener;
import net.ark3l.globalbank2.listeners.BPlayerListener;
import net.ark3l.globalbank2.util.Log;
import net.ark3l.globalbank2.util.Metrics;
import net.ark3l.globalbank2.util.Sort;
import net.ark3l.globalbank2.util.SqliteDB;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GlobalBank extends JavaPlugin {
	public static GlobalBank plugin;

	public final BPlayerListener playerListener = new BPlayerListener(this);
	public final BEntityListener entityListener = new BEntityListener(this);
	public final BInventoryListener inventoryListener = new BInventoryListener(this);
	public final SettingsManager settings = new SettingsManager(this);

	public final HashMap<Player, ArrayList<ItemStack>> isk = new HashMap<Player, ArrayList<ItemStack>>();
	public final HashMap<Player, Bankventory> bankventories = new HashMap<Player, Bankventory>();

	public final ArrayList<Player> playersDeletingBankers = new ArrayList<Player>();
	public final ArrayList<Player> playersChangingBankersDirection = new ArrayList<Player>();
	public final Sort sort = new Sort();

	public Economy economy = null;
	public NPCManager manager = null;

	private String noPermission = ChatColor.DARK_RED + "You don't have permission to use that command!";

	public void onEnable() {
		plugin = this;
		try {
			manager = new NPCManager(this);
		} catch (IOException e) {
		}

		setupConfig();
		Log.info("Loaded settings");
		setupData();
		Log.info("Loaded SQLite database");
		npcSetup();
		Log.info("Loaded NPCs");

		if (getServer().getPluginManager().getPlugin("Vault") != null && settings.useEconomy) {
			setupEconomy();
			Log.info("Economy enabled");
		}

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e1) {
			Log.warning("Error submitting usage statistics");
		}

		registerListeners();

		Log.info(this + " enabled!");
	}

	private void setupConfig() {
		settings.loadSettings();
		settings.getSettings();
	}

	private void npcSetup() {
		try {
			this.manager = new NPCManager(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<Location, String> hm = SqliteDB.getBankers();
		for (Map.Entry<Location, String> entry : hm.entrySet()) {
			manager.spawnBanker(entry.getKey(), entry.getValue());
		}
	}

	public void onDisable() {
		manager.despawnAll();
		Log.info(this + " disabled!");
	}

	private Boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("gb") && sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("create") && args.length > 1) {
					if (!player.hasPermission("gb.create")) {
						player.sendMessage(noPermission);
						return true;
					}
					// strip the pitch and yaw off of the location so the NPC isn't funneh
					Location location = player.getLocation().clone();
					location.setPitch(0);
					location.setYaw(0);

					if (args[1].length() > 16) {
						player.sendMessage(ChatColor.BLUE + "[GlobalBank2] " + ChatColor.WHITE + "Bank names must be no longer than 16 letters");
						return true;
					}

					SqliteDB.newBanker(args[1], location);
					manager.spawnBanker(location, args[1]);

					player.sendMessage(ChatColor.BLUE + "[GlobalBank2] " + ChatColor.WHITE + "Bank: " + ChatColor.GOLD + args[1] + ChatColor.WHITE + " has been created.");
				} else if (args[0].equalsIgnoreCase("delete")) {
					if (!player.hasPermission("gb.delete")) {
						player.sendMessage(noPermission);
						return true;
					}

					this.playersDeletingBankers.add(player);
				
					player.sendMessage(ChatColor.BLUE + "[GlobalBank2] " + ChatColor.WHITE + "Please punch a Banker to remove them.");
				} else if (args[0].equalsIgnoreCase("help")) {
					player.sendMessage(ChatColor.BLUE + "[GlobalBank2]" + ChatColor.WHITE + " ~" + ChatColor.AQUA + " AdminHelpMenu" + ChatColor.WHITE + " ~");
					player.sendMessage("/gb create [name]" + ChatColor.GOLD + "Creates An NPC");
					player.sendMessage("/gb delete" + ChatColor.GOLD + "Deletes An NPC");
					player.sendMessage("/gb face" + ChatColor.GOLD + "Changes Facing of an NPC");
					player.sendMessage("/gb info" + ChatColor.GOLD + "Show Info on Plugin");
					
					
					if (!player.hasPermission("gb.help")) {
						player.sendMessage(noPermission);
						return true;
					}
					
				} else if (args[0].equalsIgnoreCase("info")) {
					player.sendMessage("Authors: Lenis0012 and horse2950 || Former Authors: Samkio and xTorrent || Tester: xH3LLRA1Z3Rx");	
				
					
				} else if (args[0].equalsIgnoreCase("face")) {
					if (!player.hasPermission("gb.face")) {
						player.sendMessage(noPermission);
						return true;
					}
					
					this.playersChangingBankersDirection.add(player);
					player.sendMessage(ChatColor.BLUE + "[GlobalBank2] " + ChatColor.WHITE + "Please punch a Banker to make them face towards you.");

				} else {
					player.sendMessage(ChatColor.BLUE + "[GlobalBank2] " + ChatColor.RED + "INVAILD COMMAND" + ChatColor.AQUA + "Type /gb help");
				}
				
			} else {
				player.sendMessage(ChatColor.BLUE + this.toString());
			}

			return true;
		} else {
			return false;
		}
	}

	public void removeContents(Player p) {
		if (isk.containsKey(p)) {
			isk.remove(p);
		}
	}

	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(inventoryListener, this);
		pm.registerEvents(entityListener, this);
	}

	private void setupData() {
		File maindir = new File(this.getDataFolder() + "/Data/");
		maindir.mkdirs();
		SqliteDB.prepare();
	}
}