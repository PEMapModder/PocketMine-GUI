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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.event.KeyEvent;
import java.io.*;

public class ServerMainActivity extends Activity{
	private Process process;
	@Getter private OutputStream stdin;
	@Getter private InputStream stdout, stderr;
	@Getter private BufferedReader stdoutBuffered, stderrBuffered;

	@Getter private ServerState serverState = ServerState.STATE_STOPPED;
	private File home;

	private File phpBinaries, pmEntry;
	@Getter private JButton startStopButton;
	@Getter private ConsolePanel consolePanel;

	public ServerMainActivity(File home, File phpBinaries){
		super("PocketMine-GUI @ " + home.getAbsolutePath());
		this.home = home;
		this.phpBinaries = phpBinaries;
		pmEntry = new File(home, "PocketMine-MP.phar");
		if(!pmEntry.isFile()){
			pmEntry = new File(home, "src");
			if(!pmEntry.isDirectory()){
				throw new RuntimeException("No PocketMine entry detected");
			}
		}
	}

	@Override
	protected void onStart(){
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JMenuBar bar = new JMenuBar();
		JMenu serverMenu = new JMenu("Server");
		serverMenu.setMnemonic(KeyEvent.VK_S);
		JMenu playerMenu = new JMenu("Players");
		playerMenu.setMnemonic(KeyEvent.VK_P);
		bar.add(serverMenu);
		bar.add(playerMenu);
		setJMenuBar(bar);
		startStopButton = new JButton("Start");
		startStopButton.addActionListener(e -> startServer());
		add(startStopButton);
		add(consolePanel = new ConsolePanel(this));
	}

	public boolean startServer(){
		if(serverState != ServerState.STATE_STOPPED){
			return false;
		}

		try{
			System.err.println("Starting server: " + phpBinaries.getAbsolutePath() + " " + pmEntry.getAbsolutePath());
			setProcess(new ProcessBuilder
					(phpBinaries.getAbsolutePath(), pmEntry.getAbsolutePath())
					.directory(home)
					.start());

			serverState = ServerState.STATE_STARTING;
			startStopButton.setEnabled(false);
			startStopButton.setText(serverState.getState());
		}catch(IOException e){
			e.printStackTrace();
		}
		return true;
	}

	public void setProcess(Process process){
		this.process = process;
		stdin = process.getOutputStream();
		stdout = process.getInputStream();
		stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
		stderr = process.getErrorStream();
		stderrBuffered = new BufferedReader(new InputStreamReader(stderr));
	}

	@Override
	public void pack(){
		super.pack();
		setExtendedState(MAXIMIZED_BOTH);
	}

	public enum ServerState{
		STATE_STOPPED("Stopped"),
		STATE_STARTING("Starting..."),
		STATE_RUNNING("Running"),
		STATE_STOPPING("Stopping...");

		@Getter private String state;

		ServerState(String state){
			this.state = state;
		}
	}
}
