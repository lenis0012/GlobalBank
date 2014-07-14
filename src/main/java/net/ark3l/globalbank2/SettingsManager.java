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

import net.ark3l.globalbank2.util.Log;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsManager {
	private GlobalBank m;
	private YamlConfiguration y;

	public SettingsManager(GlobalBank m) {
		this.m = m;
	}

	public double costPerSlot = 20;
	public double multiplier = 2;
	public boolean useEconomy = true;
	public int startWithSlots = 5;

	public void loadSettings() {
		File f = new File(m.getDataFolder() + "/Config.yml");
		this.y = YamlConfiguration.loadConfiguration(f);
		y.addDefaults(YamlConfiguration.loadConfiguration(m.getResource("src/main/resources/config.yml")));
		y.options().copyDefaults(true);
		try {
			y.save(f);
		} catch (IOException e) {
			Log.severe("Error loading settings.");
		}
	}

	public void getSettings() {
		this.costPerSlot = y.getDouble("Economy.CostPerSlot");
		this.useEconomy = y.getBoolean("Economy.UseEconomy");
		this.multiplier = y.getDouble("Economy.ProgressiveSlotMultiplier");
		this.startWithSlots = y.getInt("Slot.BeginWith");
	}

	public Object getValue(String s, Object o) {
		if (!this.y.contains(s)) {
			y.set(s, o);
		}
		return y.get(s, o);
	}

	public Integer getIntegerValue(String s, Integer i) {
		Object o = this.getValue(s, i);
		return (o instanceof Integer) ? (Integer) o : i;
	}

	public String getStringValue(String s, String def) {
		Object o = this.getValue(s, def);
		return (o instanceof String) ? (String) o : def;
	}

	public Boolean getBooleanValue(String s, Boolean i) {
		Object o = this.getValue(s, i);
		return (o instanceof Boolean) ? (Boolean) o : i;
	}
}