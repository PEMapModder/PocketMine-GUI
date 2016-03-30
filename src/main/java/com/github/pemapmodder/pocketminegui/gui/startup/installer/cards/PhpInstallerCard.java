package com.github.pemapmodder.pocketminegui.gui.startup.installer.cards;

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

import com.github.pemapmodder.pocketminegui.gui.startup.installer.InstallPHPThread;
import com.github.pemapmodder.pocketminegui.gui.startup.installer.InstallServerActivity;
import com.github.pemapmodder.pocketminegui.lib.card.Card;
import com.github.pemapmodder.pocketminegui.utils.Utils;

import javax.swing.*;
import java.awt.GridLayout;
import java.io.File;

public class PhpInstallerCard extends Card{
	private final InstallServerActivity activity;
	private final JProgressBar progressBar;
	private Timer timer;
	private InstallPHPThread thread;

	public PhpInstallerCard(InstallServerActivity activity){
		this.activity = activity;
		setLayout(new GridLayout(4, 1));
		add(new JLabel("Do you have PHP binaries on your computer?"));
		JButton positive = new JButton("Yes, let me point it to you");
		positive.addActionListener(e -> {
//			JOptionPane.showMessageDialog(activity, "Could not autodetect PHP binaries. " +
//							"Please choose the PHP binaries to run with this server.",
//					"Binaries not found", JOptionPane.WARNING_MESSAGE);
			while(true){
				JFileChooser binChooser = new JFileChooser(
//						new File(System.getProperty("os.name").toLowerCase().contains("win") ?
//								System.getenv("ProgramFiles") : "/Applications")
						activity.getSelectedHome()
				);
				binChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int ret = binChooser.showOpenDialog(activity);
				if(ret != JFileChooser.APPROVE_OPTION){
					return;
				}
				File phpBinaries = binChooser.getSelectedFile();
				if(Utils.validatePhpBinaries(phpBinaries)){
					activity.setPhpBinaries(phpBinaries);
					activity.next();
					break;
				}
				JOptionPane.showMessageDialog(activity, "Invalid PHP binaries! " +
						"Please choose again.", "Invalid binaries", JOptionPane.ERROR_MESSAGE);
			}
		});
		add(positive);
		JButton negative = new JButton("No, install it for me");
		negative.addActionListener(e -> {
			positive.setEnabled(false);
			negative.setEnabled(false);
			startInstallation();
		});
		add(negative);
		progressBar = new JProgressBar(0, 100);
		add(progressBar);
		progressBar.setVisible(false);
	}

	private void startInstallation(){
		thread = new InstallPHPThread(activity.getSelectedHome());
		timer = new Timer(500, e -> onTimerTick());
		timer.start();
		thread.start();
		progressBar.setVisible(true);
	}

	private void onTimerTick(){
		if(thread.isInitialized()){
			progressBar.setValue(thread.getProgress());
			if(thread.getProgress() == thread.getMax()){
				File php = thread.getResult();
				if(php != null){
					activity.setPhpBinaries(php);
				}
				timer.stop();
				activity.next();
			}
		}
	}

	@Override
	public void onEntry(){
		activity.getNextButton().setEnabled(false);
		File home = activity.getSelectedHome();
		String[] paths = System.getProperty("java.library.path").split(";");
		for(String path : paths){
			File file = new File(path, Utils.getOS() == Utils.OperatingSystem.WINDOWS ? "php.exe" : "php");
			if(file.isFile() && Utils.validatePhpBinaries(file)){
				activity.setPhpBinaries(file);
			}
		}
	}

	@Override
	public boolean onExit(int type){
		activity.getNextButton().setEnabled(true);
		return true;
	}

	@Override
	public String getCardName(){
		return "PHP Installer";
	}
}
