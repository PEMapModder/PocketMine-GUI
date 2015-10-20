package com.github.pemapmodder.pocketminegui;

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

import com.github.pemapmodder.pocketminegui.gui.startup.ChooseServerActivity;
import com.github.pemapmodder.pocketminegui.lib.Activity;

/**
 * Entry class to the program
 */
public class PocketMineGUI{
	public static Activity CURRENT_ROOT_ACTIVITY = null;

	public static void main(String[] args){
		new ChooseServerActivity().init();
	}
}
