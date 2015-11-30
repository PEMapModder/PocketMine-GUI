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

import com.github.pemapmodder.pocketminegui.lib.Activity;
import lombok.Getter;

import java.util.Map;

public class ServerOptionsActivity extends Activity{
	@Getter
	private final Map<String, Object> values;

	public ServerOptionsActivity(String title, Activity activity, Map<String, Object> map){
		super(title, activity);
		values = map;
	}
}
