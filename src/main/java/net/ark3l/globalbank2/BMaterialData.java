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

import org.bukkit.Material;

import java.io.Serializable;

public class BMaterialData implements Serializable {
	private static final long serialVersionUID = 1385103110780786554L;
	@SuppressWarnings("unused")
	private final int type;
	private byte data = 0;

	public BMaterialData(final int type) {
		this(type, (byte) 0);
	}

	public BMaterialData(final Material type) {
		this(type, (byte) 0);
	}

	public BMaterialData(final int type, final byte data) {
		this.type = type;
		this.data = data;
	}

	public BMaterialData(final Material type, final byte data) {
		this(type.getId(), data);
	}

	public byte getData() {
		return data;
	}

	public void setData(byte data) {
		this.data = data;
	}
}