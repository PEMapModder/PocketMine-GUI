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
import com.github.pemapmodder.pocketminegui.utils.NonBlockingANSIReader;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.io.*;

public class ServerMainActivity extends Activity{
	@Getter private Process process;
	@Getter private OutputStream stdin;
	@Getter private InputStream stdout;
	@Getter private NonBlockingANSIReader stdoutBuffered;

	//	@Getter private ServerState serverState = ServerState.STATE_STOPPED;
	@Getter private ServerLifetime lifetime;
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
			pmEntry = new File(pmEntry, "pocketmine/PocketMine.php");
		}
		File pluginPath = new File(home, "plugins/.pmgui.adaptor.php");
		pluginPath.getParentFile().mkdirs();
		try{
			IOUtils.copy(getClass().getClassLoader().getResourceAsStream("plugins/adaptor.php"), new FileOutputStream(pluginPath));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart(){
		lifetime = new ServerLifetime(this);

//		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLayout(new GridBagLayout());
		JMenuBar bar = new JMenuBar();
		JMenu serverMenu = new JMenu("Server");
		serverMenu.setMnemonic(KeyEvent.VK_S);
		JMenu playerMenu = new JMenu("Players");
		playerMenu.setMnemonic(KeyEvent.VK_P);
		bar.add(serverMenu);
		bar.add(playerMenu);
		setJMenuBar(bar);

		startStopButton = new JButton("Start");
		startStopButton.addActionListener(lifetime::listen);
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridx = 0;
		constr.gridy = 0;
		constr.fill = GridBagConstraints.VERTICAL;
		constr.weighty = 0.02;
		constr.weightx = 0.1;
		add(startStopButton, constr);
		constr.gridx = 1;
		constr.gridy = 1;
		constr.fill = GridBagConstraints.BOTH;
		constr.weightx = 0.8;
		constr.weighty = 0.9;
		add(consolePanel = new ConsolePanel(this), constr);
	}

	boolean startServer(){
		try{
			System.err.println("Starting server: " + phpBinaries.getAbsolutePath() + " " + pmEntry.getAbsolutePath());
			setProcess(new ProcessBuilder
					(phpBinaries.getAbsolutePath(), pmEntry.getAbsolutePath(),
							"--enable-ansi", "--disable-readline", "--disable-wizard", "--pmgui.enabled=1")
					.directory(home)
					.redirectErrorStream(true)
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
		stdoutBuffered = new NonBlockingANSIReader(stdout);
		stdoutBuffered.start();
	}

	@Override
	public void pack(){
		super.pack();
		setExtendedState(MAXIMIZED_BOTH);
	}

	public enum ServerState{
		STATE_STOPPED("Start"),
		STATE_STARTING("Starting..."),
		STATE_RUNNING("Stop"),
		STATE_STOPPING("Stopping...");

		@Getter private String buttonName;

		ServerState(String buttonName){
			this.buttonName = buttonName;
		}
	}
}
