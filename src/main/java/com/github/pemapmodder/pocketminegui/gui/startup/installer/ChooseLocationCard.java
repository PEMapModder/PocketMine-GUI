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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ChooseLocationCard extends Card implements ActionListener{
	private JTextField pathField;
	private InstallServerActivity activity;

	public ChooseLocationCard(InstallServerActivity activity){
		this.activity = activity;
		JLabel installLabel = new JLabel("Install server at: ");
		add(installLabel);
		JPanel pathPanel = new JPanel();
		pathField = new JTextField();
		pathField.setText(new File(".", "PocketMine-MP/").getAbsolutePath());
		pathPanel.add(pathField);
		JButton pathButton = new JButton("...");
		pathButton.addActionListener(this);
		pathPanel.add(pathButton);
		add(pathPanel);
	}

	@Override
	public String getCardName(){
		return "Choose install location";
	}

	@Override
	public void actionPerformed(ActionEvent e){
		JFileChooser chooser = new JFileChooser(new File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showDialog(this, "Choose");
		if(result == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			pathField.setText(file.getAbsolutePath());
			activity.pack();
		}
	}
}
