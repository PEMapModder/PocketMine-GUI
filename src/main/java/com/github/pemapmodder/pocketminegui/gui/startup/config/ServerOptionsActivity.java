package com.github.pemapmodder.pocketminegui.gui.startup.config;

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

import com.github.pemapmodder.pocketminegui.gui.startup.config.adaptor.CheckBoxOptionAdaptor;
import com.github.pemapmodder.pocketminegui.gui.startup.config.adaptor.NumberFieldAdaptor;
import com.github.pemapmodder.pocketminegui.gui.startup.config.adaptor.OptionAdaptor;
import com.github.pemapmodder.pocketminegui.lib.Activity;
import com.github.pemapmodder.pocketminegui.lib.JNumberFilter;
import lombok.Getter;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

public abstract class ServerOptionsActivity extends Activity{
	@Getter private final Map<String, Object> values;
	@Getter private final Map<String, String> descMap;

	@Getter private JLabel descLabel;
	@Getter private JPanel editorPanel;

	@Getter private OptionAdaptor<?> adaptor;

	private int lastSelected = -1;

	public ServerOptionsActivity(String title, Activity activity, Map<String, Object> map, Map<String, String> descMap){
		super(title, activity);
		values = map;
		this.descMap = descMap;
	}

	@Override
	protected void onStart(){
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		JPanel properties = new JPanel();
		descLabel = new JLabel();
		editorPanel = new JPanel();
		JScrollPane pane = new JScrollPane();
		DefaultListModel<String> model = new DefaultListModel<>();
		JList<String> list = new JList<>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSize(list.getWidth(), 300);
		list.addListSelectionListener(e -> onSelect(list.getSelectedIndex()));
		values.keySet().forEach(model::addElement);
		list.setModel(model);
		list.revalidate();
		list.repaint();
		pane.getViewport().add(list);
		properties.add(pane);
		add(properties);
		JPanel detailsPanel = new JPanel(new GridLayout(1, 2));
		detailsPanel.add(descLabel);
		detailsPanel.add(editorPanel);
		add(detailsPanel);
		onSelect(0);
		setExtendedState(MAXIMIZED_BOTH);
	}

	private void onSelect(int index){
		if(lastSelected == index){
			return;
		}
		if(lastSelected != -1){
			onDeselect(lastSelected);
		}
		lastSelected = index;
		String key = (String) values.keySet().toArray()[index];
		descLabel.setText(descMap.get(key));
		editorPanel.removeAll();
		Object value = values.values().toArray()[index];
		if(value instanceof Boolean){
			JCheckBox checkBox = new JCheckBox("Enable");
			checkBox.setSelected((Boolean) value);
			editorPanel.add(checkBox);
			adaptor = new CheckBoxOptionAdaptor(checkBox);
		}else if(value instanceof Number){
			JTextField field = new JTextField();
			field.setSize(300, field.getHeight());
			field.setDocument(new JNumberFilter().setNegativeAccepted(true));
			editorPanel.add(field);
			adaptor = new NumberFieldAdaptor(field);
		}else if(value instanceof String){
			JTextField field = new JTextField();
			field.setSize(600, field.getHeight());
			editorPanel.add(field);
			adaptor = field::getText;
		}else if(value instanceof List){
			// TODO
			adaptor = () -> (List) value;
		}else{
			adaptor = () -> value;
		}
	}

	private void onDeselect(int lastSelected){
		String key = (String) values.keySet().toArray()[lastSelected];
		values.put(key, adaptor.getOption());
	}

	@Override
	protected void onStop(){
		onResult(values);
	}

	protected abstract void onResult(Map<String, Object> opts);
}
