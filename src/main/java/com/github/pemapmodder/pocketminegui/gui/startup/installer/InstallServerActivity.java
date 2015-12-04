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


import com.github.pemapmodder.pocketminegui.gui.server.ServerMainActivity;
import com.github.pemapmodder.pocketminegui.gui.startup.installer.cards.*;
import com.github.pemapmodder.pocketminegui.lib.Activity;
import com.github.pemapmodder.pocketminegui.lib.card.Card;
import com.github.pemapmodder.pocketminegui.lib.card.CardActivity;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public class InstallServerActivity extends CardActivity{
	@Getter private File selectedHome;
	@Getter @Setter private File phpBinaries;

	@Getter @Setter private Release selectedRelease;

	public InstallServerActivity(Activity parent){
		super("Install server", parent);
		setExtendedState(MAXIMIZED_BOTH);
	}

	@Override
	public Card[] getDefaultCards(){
		return new Card[]{
				new ChooseLocationCard(this),
				new ChooseVersionCard(this),
				new DownloadProgressCard(this),
				new PhpInstallerCard(this),
				new ServerSetupCard(this),
		};
	}

	public void setSelectedHome(File selectedHome){
		this.selectedHome = selectedHome;
		selectedHome.mkdirs();
	}

	@Override
	protected void setCard(int index, int exitType){
		super.setCard(index, exitType);
		setExtendedState(MAXIMIZED_BOTH);
	}

	@Override
	protected void onFinish(){
		super.onFinish();
		getParent().initNewActivity(new ServerMainActivity(selectedHome, phpBinaries));
	}
}
