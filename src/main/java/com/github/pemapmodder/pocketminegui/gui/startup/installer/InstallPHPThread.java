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

import com.github.pemapmodder.pocketminegui.utils.AsyncTask;
import com.github.pemapmodder.pocketminegui.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class InstallPHPThread extends AsyncTask{
	private final File home;
	@Getter private File result = null;
	@Getter private boolean initialized;

	@Override
	public void run(){
		setMax(100);
		setProgress(0);
		initialized = true;
		Utils.installPHP(home, new Utils.InstallProgressReporter(){
			@Override
			public void report(double fraction){
				setProgress((int) (fraction * 100));
			}

			@Override
			public void completed(File result){
				setProgress(100);
				InstallPHPThread.this.result = result;
			}

			@Override
			public void errored(){
				setProgress(100);
			}
		});
	}
}
