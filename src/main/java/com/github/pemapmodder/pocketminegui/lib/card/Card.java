package com.github.pemapmodder.pocketminegui.lib.card;

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

import javax.swing.JPanel;

public abstract class Card extends JPanel{
	/**
	 * The {@link CardActivity} is closed.
	 */
	public final static int EXIT_CLOSE = 0;
	/**
	 * The "Back" button is triggered.
	 */
	public final static int EXIT_BACK = 1;
	/**
	 * The "Next" button is triggered.
	 */
	public final static int EXIT_NEXT = 2;

	public void onEntry(){
	}

	/**
	 * Triggered when card is swapped
	 *
	 * @param type one of {@link #EXIT_CLOSE}, {@link #EXIT_BACK} and {@link #EXIT_NEXT}
	 * @return whether to allow this action
	 */
	public boolean onExit(int type){
		return true;
	}

	public abstract String getCardName();
}
