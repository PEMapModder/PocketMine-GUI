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

import com.github.pemapmodder.pocketminegui.lib.card.Card;

import javax.swing.JLabel;

public class ChooseVersionCard extends Card{
	private InstallServerActivity activity;

	public ChooseVersionCard(InstallServerActivity activity){
		this.activity = activity;
		add(new JLabel("Loading..."));
	}

	@Override
	public void onEntry(){
		activity.getNextButton().setText("Install");
		activity.validate();
	}

	@Override
	public boolean onExit(int type){
		activity.getNextButton().setText("Next");
		activity.validate();
		return true;
	}

	@Override
	public String getCardName(){
		return "Choose PocketMine-MP version";
	}
}
