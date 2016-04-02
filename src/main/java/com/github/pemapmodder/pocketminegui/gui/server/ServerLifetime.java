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

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.io.IOException;

@RequiredArgsConstructor
public class ServerLifetime{
	@Getter private final ServerMainActivity activity;
	@Getter private ServerState state = ServerState.STATE_STOPPED;

	public void nextState(){
		JButton button = activity.getStartStopButton();
		if(state == ServerState.STATE_STOPPED){
			state = ServerState.STATE_STARTING;
			button.setText(state.getButtonName());
			button.setEnabled(false);
		}else if(state == ServerState.STATE_STARTING){
			state = ServerState.STATE_RUNNING;
			button.setText(state.getButtonName());
			button.setEnabled(true);
		}else if(state == ServerState.STATE_RUNNING){
			setStateStopping();
		}
	}

	public void setStateStopping(){
		state = ServerState.STATE_STOPPING;
		JButton button = activity.getStartStopButton();
		button.setText(state.getButtonName());
		button.setEnabled(false);
	}

	public void listen(ActionEvent e){
		if(state == ServerState.STATE_STOPPED){
			nextState();
			activity.startServer();
		}else if(state == ServerState.STATE_RUNNING){
			nextState();
			try{
				activity.getStdin().write("stop".concat(System.lineSeparator()).getBytes());
			}catch(IOException e1){
				e1.printStackTrace();
			}
		}
	}

	public void handleConsoleOutput(String clean){
		LineReader r = new LineReader(clean.trim());
		try{
			r.expect('[');
			int hour = Integer.parseInt(r.readThrough(':'));
			int minute = Integer.parseInt(r.readThrough(':'));
			int second = Integer.parseInt(r.readThrough(']'));
			r.readUntil('[');
			String thread = r.readThrough('/');
			String type = r.readThrough(']');
			r.expect(": ");
			String rest = r.readRemaining();
			// TODO message filtering
		}catch(RuntimeException e){
			System.err.println("Warning: Invalid console output: " + clean);
			e.printStackTrace();
		}
	}

	public void handlePluginMessage(InternalMessage message){
		switch(message.getType()){
			case SERVER_STARTED:
				if(state == ServerState.STATE_STARTING){
					nextState();
				}else{
					new RuntimeException("Unexpected SERVER_STARTED message received while server is in " + state.name()).printStackTrace();
				}
				break;
			case PLUGIN_DISABLED:
				setStateStopping();
				break;
			case PLAYER_JOIN:
				// TODO: handle
				break;
			case PLAYER_QUIT:
				// TODO: handle
				break;
		}
	}
}
