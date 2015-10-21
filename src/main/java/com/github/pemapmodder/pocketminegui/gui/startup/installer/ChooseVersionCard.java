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
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

public class ChooseVersionCard extends Card{
	private InstallServerActivity activity;
	@Getter
	private FetchVersionsThread versionFetch;
	@Getter
	private int nextIndex = 0;
	@Getter
	private Timer timer;
	private JList[] lists = new JList[ReleaseType.values().length];
	private Vector[] listsData = new Vector[ReleaseType.values().length];
	private ButtonGroup typeRadios;
	private JPanel cardPanel;
	private CardLayout cardLayout;
	private int choosenType = ReleaseType.STABLE.getTypeId();

	public ChooseVersionCard(InstallServerActivity activity){
		this.activity = activity;
		typeRadios = new ButtonGroup();
		JPanel radioPanel = new JPanel();
		cardPanel = new JPanel(cardLayout = new CardLayout());
		for(ReleaseType type : ReleaseType.values()){
			JRadioButton button = new JRadioButton();
			button.addActionListener(e -> onRadioClicked(type));
			typeRadios.add(button);
			radioPanel.add(button);
			int id = type.getTypeId();
			Vector<String> data = new Vector<>();
			listsData[id] = data;
			JList<String> list = new JList<>(data);
			lists[id] = list;
			JScrollPane pane = new JScrollPane();
			pane.add(lists[id]);
			cardPanel.add(pane, type.getName());
		}
		cardLayout.show(cardPanel, ReleaseType.STABLE.getName());
		add(radioPanel);
		add(cardPanel);

		versionFetch = new FetchVersionsThread(this);
		versionFetch.start();
		timer = new Timer(100, this::updateVersions);
		timer.start();
	}

	private void onRadioClicked(ReleaseType type){
		cardLayout.show(cardPanel, type.getName());
		choosenType = type.getTypeId();
	}

	private void updateVersions(ActionEvent event){
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
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addRelease(Release release){
		listsData[release.getType().getTypeId()].addElement(release.presentInHtml());
		lists[release.getType().getTypeId()].setListData(listsData[release.getType().getTypeId()]);
		revalidate();
	}

	public Release getSelectedRelease(){
		return versionFetch.getReleases().get(lists[choosenType].getSelectedIndex());
	}

	@Override
	public void onEntry(){
		activity.getNextButton().setText("Install");
		activity.revalidate();
	}

	@Override
	public boolean onExit(int type){
		activity.getNextButton().setText("Next");
		activity.revalidate();
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
