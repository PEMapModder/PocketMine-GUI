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

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Release{
	public final static int LIST_STABLE = 0, LIST_BETA = 1, LIST_DEVELOPMENT = 2, LIST_BLEEDING = 3;

	@Getter
	private String name;
	@Getter
	private ReleaseType type;
	@Getter
	private long publishTime;
	@Getter
	private String pharUrl;
	private String zipEntryName;

	public Release(String name, ReleaseType type, long publishTime, String pharUrl, String zipEntryName){
		this.name = name;
		this.type = type;
		this.publishTime = publishTime;
		this.pharUrl = pharUrl;
		this.zipEntryName = zipEntryName;
	}

	@Override
	public String toString(){
		return "<html><strong>" + name + "</strong><br>" + "<table><tr>" +
				"<td align='left'>" + type.getName() + " version</td><td align='right'>" +
				new SimpleDateFormat("YYYY-MM-dd HH:mm:ss z").format(new Date(publishTime)) +
				"</td></tr></table></html>";
	}


}
