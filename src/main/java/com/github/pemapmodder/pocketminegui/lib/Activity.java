package com.github.pemapmodder.pocketminegui.lib;

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

import com.github.pemapmodder.pocketminegui.PocketMineGUI;
import lombok.Getter;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class Activity extends JFrame{
	@Getter
	private Activity parent, child = null;
	private boolean hasNewActivity = false;
	private boolean onStopCalled = false;

	public Activity(String title){
		this(title, null);
	}

	public Activity(String title, Activity parent){
		super(title);
		this.parent = parent;
		if(parent != null){
			parent.setChild(this);
		}
	}

	public final void init(){
		if(!hasParent()){
			if(PocketMineGUI.CURRENT_ROOT_ACTIVITY != null){
				throw new IllegalStateException("Cannot init root activity when PocketMineGUI.CURRENT_ROOT_ACTIVITY is not null");
			}
			PocketMineGUI.CURRENT_ROOT_ACTIVITY = this;
		}
		addWindowListener(new InternalWindowListener());
		setLocationRelativeTo(null);
		onStart();
		pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	protected void onStart(){
	}

	protected void onFocusGain(){
	}

	protected void onFocusLoss(){
	}

	protected void onStop(){
	}

	protected void onChildClosed(){
		child = null;
	}

	public boolean hasParent(){
		return parent != null;
	}

	public boolean hasChild(){
		return child != null;
	}

	public void setChild(Activity child){
		if(this.child != null){
			throw new IllegalStateException("Activity already has a child");
		}
		this.child = child;
	}

	public void initNewActivity(Activity successor){
		if(hasParent()){
			parent.initNewActivity(successor);
			return;
		}
		hasNewActivity = true;
		stopActivity();
		successor.init();
	}

	public void stopActivity(){
		if(child != null){
			child.stopActivity();
		}
		onStopCalled = true;
		onStop();
		dispose();
	}

	private class InternalWindowListener extends WindowAdapter{
		@Override
		public void windowClosed(WindowEvent e){
			if(!onStopCalled){
				onStopCalled = true;
				onStop();
			}
			if(hasParent()){
				parent.onChildClosed();
				parent.requestFocus();
			}else{
				PocketMineGUI.CURRENT_ROOT_ACTIVITY = null;
				if(!hasNewActivity){
					System.exit(0);
				}
			}
		}

		@Override
		public void windowGainedFocus(WindowEvent e){
			if(hasChild()){
				child.requestFocus();
			}else{
				onFocusGain();
			}
		}

		@Override
		public void windowLostFocus(WindowEvent e){
			onFocusLoss();
		}

	}
}
