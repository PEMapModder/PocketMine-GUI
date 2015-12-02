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

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;

public class ConsolePanel extends JPanel{
	private final ServerMainActivity activity;
	private final JTextArea console;

	public ConsolePanel(ServerMainActivity activity){
		this.activity = activity;
		console = new JTextArea();
		console.setEditable(false);
		// people NEED to see this to feel happy
		console.setForeground(Color.WHITE);
		console.setBackground(Color.BLACK);
		add(console);
	}
}
