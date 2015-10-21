package com.github.pemapmodder.pocketminegui.lib.card;

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

import com.github.pemapmodder.pocketminegui.lib.Activity;
import lombok.Getter;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.GridLayout;

public abstract class CardActivity extends Activity{
	@Getter
	private Card[] cards;
	@Getter
	private int currentStep = -1;
	@Getter
	private JPanel swapper, controls;
	@Getter
	private JButton backButton, nextButton;

	public CardActivity(String title){
		super(title);
	}

	public CardActivity(String title, Activity parent){
		super(title, parent);
	}

	public abstract Card[] getDefaultCards();

	@Override
	protected void onStart(){
		cards = getDefaultCards();
		setLayout(new GridLayout(2, 1));

		CardLayout cardLayout = new CardLayout();
		swapper = new JPanel(cardLayout);
		for(int i = 0; i < cards.length; i++){
			Card card = cards[i];
			swapper.add(card, "card#" + i);
		}
		add(swapper);

		controls = new JPanel();
		backButton = new JButton("Back");
		nextButton = new JButton("Next");
		backButton.addActionListener(e -> back());
		nextButton.addActionListener(e -> next());
		controls.add(backButton);
		controls.add(nextButton);

		add(controls);

		setCard(0, 0); // the second parameter simply doesn't matter
	}

	public void next(){
		if(currentStep + 1 == cards.length){
			stopActivity();
			return;
		}
		setCard(currentStep + 1, Card.EXIT_NEXT);
	}

	public void back(){
		setCard(currentStep - 1, Card.EXIT_BACK);
	}

	private void setCard(int index, int exitType){
		if(index >= cards.length){
			throw new IndexOutOfBoundsException("No such card");
		}
		if(currentStep == -1 || cards[currentStep].onExit(exitType)){
			((CardLayout) swapper.getLayout()).show(swapper, "card#" + index);
			currentStep = index;
			Card newCard = cards[currentStep];
			newCard.onEntry();
			setTitle(String.format("Step %d of %d: %s", currentStep + 1, cards.length, newCard.getCardName()));
			nextButton.setText(currentStep + 1 == cards.length ? "Finish" : "Next");
			backButton.setEnabled(currentStep > 0);
			revalidate();
			pack();
		}
	}

	@Override
	protected void onStop(){
		if(currentStep != -1){
			cards[currentStep].onExit(Card.EXIT_CLOSE);
		}
		for(Card card : cards){
			card.onStop();
		}
	}
}
