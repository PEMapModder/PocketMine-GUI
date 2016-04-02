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

import com.github.pemapmodder.pocketminegui.utils.NonBlockingANSIReader;
import com.github.pemapmodder.pocketminegui.utils.Ring;
import com.github.pemapmodder.pocketminegui.utils.TerminalCode;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.html.HTMLDocument;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ConsolePanel extends JPanel{
	private final static int BUFFER_SIZE = 100;
	private final ServerMainActivity activity;
	private final JLabel title;
	private final JEditorPane stdout;
	private final HTMLDocument doc;
	private final Element para;
	private final Ring<String> consoleBuffer = new Ring<>(new String[BUFFER_SIZE]);

	public ConsolePanel(ServerMainActivity activity){
		this.activity = activity;
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		title = new JLabel("PocketMine-MP");
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.0;
		add(title, c);
		stdout = new JEditorPane(){
			@Override
			public boolean getScrollableTracksViewportHeight(){
				return true;
			}

			@Override
			public boolean getScrollableTracksViewportWidth(){
				return true;
			}
		};
		stdout.setContentType("text/html");
		stdout.setText("<html><body style='color: #FFFFFF;'><p id='p' style='font-family: monospace;'><br><br><br><br>" +
				"<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br></p></body></html>");
		doc = (HTMLDocument) stdout.getDocument();
		para = doc.getElement("p");
		JScrollPane scrollPane = new JScrollPane();
		stdout.setEditable(false);
		// People NEED to see this to feel happy
		stdout.setForeground(Color.WHITE);
		stdout.setBackground(Color.BLACK);
		Style style = doc.getStyleSheet().addStyle(null, null);
		doc.setParagraphAttributes(para.getStartOffset(), 1, style, true);
		scrollPane.getViewport().add(stdout);
		scrollPane.setMinimumSize(new Dimension(200, 200));
		scrollPane.setPreferredSize(new Dimension(500, 400));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		c.gridy = 1;
		c.weightx = 0.9;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		add(scrollPane, c);
		JTextField stdin = new JTextField();
		stdin.addKeyListener(new MyKeyAdapter(stdin, activity));
		c.gridy = 2;
		c.weighty = 0.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(stdin, c);
		Timer timer = new Timer(50, e -> updateConsole());
		timer.start();
	}

	private void updateConsole(){
		NonBlockingANSIReader reader = activity.getStdoutBuffered();
		if(reader == null){
			return;
		}
		NonBlockingANSIReader.Entry entry;
		while((entry = reader.nextOutput()) != null){
			if(entry.getType() == NonBlockingANSIReader.EntryType.TITLE){
				title.setText(entry.getLine());
				continue;
			}
			if(entry.getType() == NonBlockingANSIReader.EntryType.PMGUI){
				activity.getLifetime().handlePluginMessage(new InternalMessage(entry.getLine()));
				continue;
			}
			String line = TerminalCode.toHTML(entry.getLine());
			String text = stdout.getText();
			consoleBuffer.add(line);

			try{
				doc.setInnerHTML(para, StringUtils.join(consoleBuffer, "<br>"));
			}catch(BadLocationException | IOException e){
				e.printStackTrace();
			}

			String clean = TerminalCode.clean(entry.getLine());
			activity.getLifetime().handleConsoleOutput(clean);
		}
	}

	private static class MyKeyAdapter extends KeyAdapter{
		private final JTextField stdin;
		private final ServerMainActivity activity;
		Ring<String> consoleLog = new Ring<>(new String[50]);
		int neg = 0;
		String tmpText = "";

		public MyKeyAdapter(JTextField stdin, ServerMainActivity activity){
			this.stdin = stdin;
			this.activity = activity;
		}

		@Override
		public void keyPressed(KeyEvent e){
			String newText = null;
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if(activity.getProcess() == null){
					return;
				}
				try{
					String line = stdin.getText();
					activity.getStdin().write(line.concat(System.lineSeparator()).getBytes());
					newText = "";
					neg = 0;
					consoleLog.add(line);
				}catch(IOException e1){
					e1.printStackTrace();
					return;
				}
			}else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == KeyEvent.VK_KP_UP){
				++neg;
				if(neg == 1){
					tmpText = stdin.getText();
				}
				try{
					newText = consoleLog.get(consoleLog.getSize() - neg);
				}catch(IndexOutOfBoundsException e1){
					--neg;
					return;
				}
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN){
				if(neg <= 0){
					neg = 0;
					return;
				}
				--neg;
				if(neg == 0){
					newText = tmpText;
				}else{
					newText = consoleLog.get(consoleLog.getSize() - neg);
				}
			}
			if(newText != null){
				stdin.setText(newText);
			}
		}
	}
}
