package net.ark3l.globalbank2.listeners;

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

import net.ark3l.globalbank2.GlobalBank;
import net.ark3l.globalbank2.PlayerState;
import net.ark3l.globalbank2.methods.SimpleMethods;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BPlayerListener implements Listener {
	private final GlobalBank b;

	public BPlayerListener(GlobalBank b) {
		this.b = b;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		if (b.manager.isNPC(e.getRightClicked())) {
			Player p = e.getPlayer();
			if (!p.hasPermission("gb.use")) {
				p.sendMessage(ChatColor.BLUE
						+ "[B] "
						+ ChatColor.RED
						+ b.settings.getStringValue("Strings.Noperm", "You do not have permission to use banks!"));
				return;
			}
			SimpleMethods.openBank(b, p);
			p.sendMessage(ChatColor.BLUE
					+ "[B] "
					+ ChatColor.WHITE
					+ b.settings.getStringValue("Strings.Open", "Welcome to")
					+ " "
					+ ChatColor.GOLD
					+ (b.manager.getBanker(b.manager.getNPCIdFromEntity(e.getRightClicked()))).bankName
					+ ".");
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent e) {
		new PlayerState(e.getPlayer());
	}

}
