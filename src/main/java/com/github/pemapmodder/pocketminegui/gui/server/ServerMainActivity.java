package com.github.pemapmodder.pocketminegui.gui.server;

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

import com.github.pemapmodder.pocketminegui.lib.Activity;
import lombok.Getter;

import javax.swing.JMenuBar;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ServerMainActivity extends Activity{
	private Process process;
	private OutputStream stdin;
	private InputStream stdout, stderr;

	@Getter
	private ServerState serverState = ServerState.STATE_STOPPED;
	private File home;
	private File phpBinaries, pmEntry;

	public ServerMainActivity(File home){
		super("PocketMine-GUI @ " + home.getAbsolutePath());
		this.home = home;
		phpBinaries = new File(home, "bin/php/php");
		pmEntry = new File(home, "PocketMine-MP.phar");
		if(!pmEntry.isFile()){
			pmEntry = new File(home, "src");
			assert pmEntry.isDirectory();
		}
	}

	@Override
	protected void onStart(){
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
	}

	public boolean startServer(){
		if(serverState != ServerState.STATE_STOPPED){
			return false;
		}

		try{
			setProcess(new ProcessBuilder
					(phpBinaries.getAbsolutePath(), pmEntry.getAbsolutePath(), "--disable-ansi")
					.directory(home)
					.start());
		}catch(IOException e){
			e.printStackTrace();
		}
		return true;
	}

	public void setProcess(Process process){
		this.process = process;
		stdin = process.getOutputStream();
		stdout = process.getInputStream();
		stderr = process.getErrorStream();
	}

	public enum ServerState{
		STATE_STOPPED("Stopped"),
		STATE_STARTING("Starting"),
		STATE_RUNNING("Running"),
		STATE_STOPPING("Stopping");

		@Getter
		private String state;

		ServerState(String state){
			this.state = state;
		}
	}
}
