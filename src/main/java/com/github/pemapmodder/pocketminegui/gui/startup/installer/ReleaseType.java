package com.github.pemapmodder.pocketminegui.gui.startup.installer;

/*
 * This file is part of PocketMine-GUI.
 *
 * PocketMine-GUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PocketMine-GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PocketMine-GUI.  If not, see <http://www.gnu.org/licenses/>.
 */

import lombok.Getter;

import java.awt.event.KeyEvent;

public enum ReleaseType{
	STABLE("Stable", Release.LIST_STABLE, KeyEvent.VK_S),
	BETA("Beta", Release.LIST_BETA, KeyEvent.VK_B),
	DEVELOPMENT("Deveopment", Release.LIST_DEVELOPMENT, KeyEvent.VK_D),
	BLEEDING("Bleeding", Release.LIST_BLEEDING, KeyEvent.VK_L);

	@Getter
	private final String name;
	@Getter
	private final int typeId;
	@Getter
	private final int keyMap;

	ReleaseType(String name, int typeId, int keyMap){
		this.name = name;
		this.typeId = typeId;
		this.keyMap = keyMap;
	}
}
