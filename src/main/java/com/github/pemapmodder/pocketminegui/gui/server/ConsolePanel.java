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
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;

public class ConsolePanel extends JPanel{
	private final ServerMainActivity activity;
	private final JTextArea stdout;

	public ConsolePanel(ServerMainActivity activity){
		this.activity = activity;
		stdout = new JTextArea();
		stdout.setEditable(false);
		// people NEED to see this to feel happy
		stdout.setForeground(Color.WHITE);
		stdout.setBackground(Color.BLACK);
		stdout.setPreferredSize(new Dimension(getWidth(), getHeight()));
		add(stdout);
		Timer timer = new Timer(100, e -> updateConsole());
		timer.start();
	}

	private void updateConsole(){
		BufferedReader stdout = activity.getStdoutBuffered();
		BufferedReader stderr = activity.getStderrBuffered();
		if(stdout == null){
			return;
		}
		try{
			String line = stdout.readLine();
			if(line != null){
				System.out.println(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			String line = stderr.readLine();
			if(line != null){
				System.out.println(line);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
