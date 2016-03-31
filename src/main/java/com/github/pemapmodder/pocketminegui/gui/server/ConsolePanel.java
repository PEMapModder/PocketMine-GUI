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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
		c.weighty = 0.1;
		add(title, c);
		stdout = new JEditorPane();
		stdout.setContentType("text/html");
		stdout.setText("<html><body style='font-family: monospace; color: #FFFFFF;'><p id='p'></p></body></html>");
		doc = (HTMLDocument) stdout.getDocument();
		para = doc.getElement("p");
//		stdout.setEditable(false);
		// People NEED to see this to feel happy
		stdout.setForeground(Color.WHITE);
		stdout.setBackground(Color.BLACK);
		Style style = doc.getStyleSheet().addStyle(null, null);
//		style.addAttribute(StyleConstants.Foreground, Color.WHITE);
//		style.addAttribute(StyleConstants.Background, Color.RED);
		doc.setParagraphAttributes(para.getStartOffset(), 1, style, true);
		stdout.setBorder(BorderFactory.createDashedBorder(new Color(0x80, 0x80, 0x80)));
		c.gridy = 1;
		c.weightx = 0.9;
		c.weighty = 0.9;
		c.weighty = 0.6;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;
		add(stdout, c);
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
			String line = TerminalCode.toHTML(entry.getLine());
//			System.out.println(line);
			String text = stdout.getText();
			consoleBuffer.add(line);

			try{
				doc.setInnerHTML(para, StringUtils.join(consoleBuffer, "<br>"));
			}catch(BadLocationException | IOException e){
				e.printStackTrace();
			}

			String clean = TerminalCode.clean(entry.getLine());

		}
	}
}
