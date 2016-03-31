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

import com.github.pemapmodder.pocketminegui.gui.server.ServerMainActivity.ServerState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.event.ActionEvent;

@RequiredArgsConstructor
public class ServerLifetime{
	@Getter private final ServerMainActivity activity;
	@Getter private ServerState state = ServerState.STATE_STOPPED;

	public void nextState(){
		if(state == ServerState.STATE_STOPPED){
			state = ServerState.STATE_STARTING;
			activity.getStartStopButton().setText(state.getButtonName());
			activity.getStartStopButton().setEnabled(false);
		}else if(state == ServerState.STATE_STARTING){
			state = ServerState.STATE_RUNNING;
			activity.getStartStopButton().setText(state.getButtonName());
			activity.getStartStopButton().setEnabled(true);
		}
	}

	public void listen(ActionEvent e){
		if(state == ServerState.STATE_STOPPED){
			nextState();
			activity.startServer();
		}else if(state == ServerState.STATE_RUNNING){
			// TODO stop server
		}
	}
}
