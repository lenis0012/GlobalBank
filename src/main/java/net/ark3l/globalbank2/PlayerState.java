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

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerState {
	public static HashMap<Player, PlayerState> m = new HashMap<Player, PlayerState>();
	private Player p;
	private PlayerStatus ps = PlayerStatus.DEFAULT;
	private int slot = 0;
	private int BuyingSlot = 0;

	public enum PlayerStatus {
		CHEST_SELECT, SLOT, DEFAULT
	}

	public PlayerState(Player p) {
		this.p = p;
		PlayerState.m.put(p, this);
	}

	public Player getP() {
		return p;
	}

	public PlayerStatus getPs() {
		return ps;
	}

	public void setPs(PlayerStatus ps) {
		this.ps = ps;
	}

	public static PlayerState getPlayerState(Player p) {
		if (PlayerState.m.containsKey(p)) {
			return PlayerState.m.get(p);
		} else {
			return new PlayerState(p);
		}
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getBuyingSlot() {
		return BuyingSlot;
	}

	public void setBuyingSlot(int buyingSlot) {
		BuyingSlot = buyingSlot;
	}
}
