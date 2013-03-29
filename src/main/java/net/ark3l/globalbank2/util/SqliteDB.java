package net.ark3l.globalbank2.util;

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
import net.ark3l.globalbank2.methods.MiscMethods;
import org.bukkit.Location;

import java.sql.*;
import java.util.HashMap;

public class SqliteDB {
	private static Connection connection;

	public static synchronized Connection getConnection() {
		if (connection == null) {
			connection = createConnection();
		}
		return connection;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			Log.severe("Could not close connection to database");
			Log.severe(e.getMessage());
		}
	}

	private static Connection createConnection() {
		try {

			Class.forName("org.sqlite.JDBC");
			Connection ret = DriverManager.getConnection("jdbc:sqlite:"
					+ GlobalBank.plugin.getDataFolder() + "/Data/GlobalBank.sqlite");
			ret.setAutoCommit(false);
			return ret;
		} catch (ClassNotFoundException e) {
			Log.severe("Could not create connection to database");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			Log.severe("Could not create connection to database");
			e.printStackTrace();
		}
		return null;
	}

	public static void prepare() {
		Connection conn;
		Statement st;
		try {
			conn = SqliteDB.getConnection();
			st = conn.createStatement();
			st.executeUpdate("CREATE TABLE IF NOT EXISTS NPCLocations (id INTEGER PRIMARY KEY, bankName VARCHAR(80) NOT NULL, loc TEXT NOT NULL);");
			conn.commit();
		} catch (SQLException e) {
			Log.severe("Cannot connect to SQLite database!");
			e.printStackTrace();
		}
	}


	public static boolean newBanker(String bankname, Location l) {
		Connection conn;
		Statement st;
		try {
			conn = getConnection();
			st = conn.createStatement();
			st.executeUpdate("INSERT INTO NPCLocations (bankName,loc) VALUES ('"
					+ bankname + "','" + MiscMethods.stringFromLoc(l) + "')");
			conn.commit();
		} catch (SQLException e) {
			Log.severe("Unable to add row to database!");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static boolean delBanker(String bankname) {
		Connection conn;
		Statement st;
		try {
			conn = getConnection();
			st = conn.createStatement();
			st.executeUpdate("DELETE FROM NPCLocations WHERE bankName='"
					+ bankname + "'");
			conn.commit();
		} catch (SQLException e) {
			Log.severe("Unable to remove row from database!");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public static HashMap<Location, String> getBankers() {
		Connection conn;
		Statement st;
		HashMap<Location, String> npcs = new HashMap<Location, String>();
		try {
			conn = getConnection();
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM NPCLocations");
			while (rs.next()) {
				npcs.put(MiscMethods.locFromString(rs.getString("loc")), rs.getString("bankName"));
			}
			conn.commit();
		} catch (SQLException e) {
			Log.severe("Unable to fetch row from database!");
			e.printStackTrace();
			return npcs;
		}
		return npcs;

	}
}
