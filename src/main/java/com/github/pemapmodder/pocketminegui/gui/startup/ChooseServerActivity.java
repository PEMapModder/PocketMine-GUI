package com.github.pemapmodder.pocketminegui.gui.startup;

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
import com.github.pemapmodder.pocketminegui.gui.startup.installer.InstallServerActivity;
import com.github.pemapmodder.pocketminegui.lib.Activity;
import com.github.pemapmodder.pocketminegui.utils.Utils;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.github.pemapmodder.pocketminegui.utils.Utils.OperatingSystem.WINDOWS;
import static com.github.pemapmodder.pocketminegui.utils.Utils.exec;
import static com.github.pemapmodder.pocketminegui.utils.Utils.validatePhpBinaries;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.CANCEL_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;
import static javax.swing.JFileChooser.ERROR_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class ChooseServerActivity extends Activity{
	public ChooseServerActivity(){
		super("PocketMine-GUI");
	}

	@Override
	protected void onStart(){
		setLayout(new GridLayout(3, 1));
		JLabel titleLabel = new JLabel("PocketMine-GUI");
		titleLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLabel);
		JButton chooseServer = new JButton("Choose server directory");
		chooseServer.addActionListener(new ChooseServerOnClickListener());
		add(chooseServer);
		JButton installServer = new JButton("Install server into new directory");
		installServer.addActionListener(new InstallServerOnClickListener());
		add(installServer);
	}

	private class ChooseServerOnClickListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser(new File("."));
			chooser.setFileSelectionMode(DIRECTORIES_ONLY);
			int ret = chooser.showOpenDialog(ChooseServerActivity.this);
			if(ret == CANCEL_OPTION || ret == ERROR_OPTION){
				return;
			}
			File home = chooser.getSelectedFile();
			if(!home.isDirectory()){
				showMessageDialog(ChooseServerActivity.this, "Not a directory!",
						"Invalid selection", ERROR_MESSAGE);
				return;
			}
			if(!new File(home, "PocketMine-MP.phar").isFile() && !new File(home, "src").isDirectory()){
				showMessageDialog(ChooseServerActivity.this, "Could not find a phar or " +
						"source installation of PocketMine-MP in directory! " +
						"Please click the \"Install server into new directory\" button if " +
						"you wish to install a server there instead.", "Invalid selection", ERROR_MESSAGE);
				return;
			}
			File phpBinaries = new File(home, "bin/php/php.exe");
			if(!phpBinaries.isFile()){
				phpBinaries = new File(home, "bin/php5/bin/php");
				if(!phpBinaries.isFile()){
					String pathPhp = exec(Utils.getOS() == WINDOWS ? "where php" : "which php");
					if(pathPhp != null){
						phpBinaries = new File(pathPhp);
						if(!phpBinaries.isFile() || !validatePhpBinaries(phpBinaries)){
							showMessageDialog(ChooseServerActivity.this, "Could not autodetect PHP binaries. " +
											"Please choose the PHP binaries to run with this server.",
									"Binaries not found", WARNING_MESSAGE);
							while(true){
								JFileChooser binChooser = new JFileChooser(new File(
										System.getProperty("os.name").toLowerCase().contains("win") ?
												System.getenv("ProgramFiles") : "/Applications"));
								binChooser.setFileSelectionMode(FILES_ONLY);
								ret = binChooser.showOpenDialog(ChooseServerActivity.this);
								if(ret != APPROVE_OPTION){
									return;
								}
								phpBinaries = binChooser.getSelectedFile();
								if(validatePhpBinaries(phpBinaries)){
									break;
								}
								showMessageDialog(ChooseServerActivity.this, "Invalid PHP binaries! " +
										"Please choose again.", "Invalid binaries", ERROR_MESSAGE);
							}
						}
					}
				}
			}
			ServerMainActivity server = new ServerMainActivity(home, phpBinaries);
			initNewActivity(server);
		}
	}

	private class InstallServerOnClickListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			new InstallServerActivity(ChooseServerActivity.this).init();
		}
	}
}
