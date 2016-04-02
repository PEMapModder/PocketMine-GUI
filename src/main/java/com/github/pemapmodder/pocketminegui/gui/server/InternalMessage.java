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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

@Getter
public class InternalMessage{
	@RequiredArgsConstructor
	@Getter
	public enum Type{
		SERVER_STARTED(0),
		PLAYER_JOIN(1),
		PLAYER_QUIT(2),
		PLUGIN_DISABLED(3);

		private final int type;

		public static Type get(int id){
			for(Type type : values()){
				if(type.type == id){
					return type;
				}
			}
			return null;
		}
	}

	private final Type type;
	private final JSONObject data;

	public InternalMessage(String line){
		line = line.substring(8);
		int index = line.indexOf(':');
		type = Type.get(Integer.parseInt(line.substring(0, index)));
		String json = line.substring(index + 1);
		data = new JSONObject(json);
	}
}
