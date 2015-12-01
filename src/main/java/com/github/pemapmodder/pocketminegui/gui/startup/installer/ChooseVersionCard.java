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
import lombok.Getter;

import javax.swing.*;
import java.awt.CardLayout;
import java.util.List;

public class ChooseVersionCard extends Card{
	private InstallServerActivity activity;
	@Getter
	private FetchVersionsThread versionFetch;
	@Getter
	private int nextIndex = 0;
	@Getter
	private Timer timer;
	private ButtonGroup typeRadios;
	private final JLabel loadingLabel;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	private int choosenType = ReleaseType.STABLE.getTypeId();
	private JList[] lists = new JList[ReleaseType.values().length];
	private DefaultListModel[] listModels = new DefaultListModel[ReleaseType.values().length];
	@Getter
	private Release selectedRelease = null;

	public ChooseVersionCard(InstallServerActivity activity){
		this.activity = activity;
		typeRadios = new ButtonGroup();
		JPanel radioPanel = new JPanel();
		cardPanel = new JPanel(cardLayout = new CardLayout());
		for(ReleaseType type : ReleaseType.values()){
			JRadioButton button = new JRadioButton(type.getName());
			if(type == ReleaseType.STABLE){
				button.setSelected(true);
			}
			button.addActionListener(e -> onRadioClicked(type));
			typeRadios.add(button);
			radioPanel.add(button);
//			int id = type.getTypeId();
			JScrollPane pane = new JScrollPane();
			DefaultListModel<Release> model = new DefaultListModel<>();
			listModels[type.getTypeId()] = model;
			JList<String> list = new JList<>();
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.addListSelectionListener(e -> {
				Release release = model.get(list.getSelectedIndex());
				activity.setSelectedRelease(release);
				activity.getNextButton().setEnabled(true);
			});
			pane.getViewport().add(lists[type.getTypeId()] = list);
			cardPanel.add(pane, type.getName());
		}
		cardLayout.show(cardPanel, ReleaseType.STABLE.getName());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(radioPanel);
		add(loadingLabel = new JLabel("Loading..."));
		add(cardPanel);

		versionFetch = new FetchVersionsThread(this);
		versionFetch.start();
		timer = new Timer(100, e -> updateVersions());
		timer.start();
	}

	private void onRadioClicked(ReleaseType type){
		cardLayout.show(cardPanel, type.getName());
		choosenType = type.getTypeId();
		activity.getNextButton().setEnabled(lists[choosenType].getSelectedIndex() != -1);
	}

	private void updateVersions(){
		List<Release> releases = versionFetch.getReleases();
		int size = releases.size();
		for(int i = nextIndex; i < size; i++){
			Release release = releases.get(i);
			addRelease(release);
		}
		nextIndex = size;
		if(versionFetch.isDone()){
			if(timer != null){
				timer.stop();
				timer = null;
				loadingLabel.setText("");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addRelease(final Release release){
		int id = release.getType().getTypeId();
		listModels[id].addElement(release);
		lists[id].setModel(listModels[id]);
		lists[id].revalidate();
		lists[id].repaint();
	}

	@Override
	public void onEntry(){
		activity.getNextButton().setText("Install");
		activity.getNextButton().setEnabled(false);
		activity.revalidate();
	}

	@Override
	public boolean onExit(int type){
		activity.getNextButton().setText("Next");
		activity.revalidate();
		activity.getNextButton().setEnabled(true);
		return true;
	}

	@Override
	public void onStop(){
		if(timer != null){
			timer.stop();
			timer = null;
		}
	}

	@Override
	public String getCardName(){
		return "Choose PocketMine-MP version";
	}
}
