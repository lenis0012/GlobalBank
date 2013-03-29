package net.ark3l.globalbank2.methods;

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

import net.ark3l.globalbank2.Bankventory;
import net.ark3l.globalbank2.GlobalBank;
import net.ark3l.globalbank2.util.Log;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;

public class MiscMethods {
	public static Location locFromString(String s) {
		String[] split = s.split(",");
		Location l = new Location(GlobalBank.plugin.getServer().getWorld(
				split[0]), Double.parseDouble(split[1]),
				Double.parseDouble(split[2]), Double.parseDouble(split[3]));
		if(split.length > 4)
			l.setYaw(Float.parseFloat(split[4]));

		return l;
	}

	public static String stringFromLoc(Location l) {
		return l.getWorld().getName() + "," + l.getX() + "," + l.getY() + ","
				+ l.getZ() + "," + l.getYaw();

	}

	public static Bankventory getAccount(Player p) {
		if (GlobalBank.plugin.bankventories.containsKey(p)) return GlobalBank.plugin.bankventories.get(p);
		File f = new File(GlobalBank.plugin.getDataFolder() + "/Data/"
				+ p.getName() + ".bankventory");
		if (f.exists()) {
			try {
				FileInputStream fis = new FileInputStream(
						GlobalBank.plugin.getDataFolder() + "/Data/"
								+ p.getName() + ".bankventory");
				ObjectInputStream ois = new ObjectInputStream(fis);
				Bankventory b = (Bankventory) ois.readObject();
				ois.close();
				fis.close();
				GlobalBank.plugin.bankventories.put(p, b);
				return b;
			} catch (Exception e) {
				Log.severe("Could not get account for " + p.getName());
				e.printStackTrace();
			}
		} else {
			Bankventory b = new Bankventory();
			try {
				FileOutputStream fos = new FileOutputStream(
						GlobalBank.plugin.getDataFolder() + "/Data/"
								+ p.getName() + ".bankventory");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(b);
				oos.close();
				fos.close();
			} catch (Exception e) {
				Log.severe("Could not get account for " + p.getName());
				e.printStackTrace();
			}
			GlobalBank.plugin.bankventories.put(p, b);
			return b;
		}
		return null;
	}

	public static void saveAll() {
		for (Player p : GlobalBank.plugin.bankventories.keySet()) {
			Bankventory b = GlobalBank.plugin.bankventories.get(p);

			try {
				FileOutputStream fos = new FileOutputStream(
						GlobalBank.plugin.getDataFolder() + "/Data/"
								+ p.getName() + ".bankventory");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(b);
				oos.close();
				fos.close();
			} catch (Exception e) {
				Log.severe("Could not save banks!");
				e.printStackTrace();
			}
		}
	}
}
