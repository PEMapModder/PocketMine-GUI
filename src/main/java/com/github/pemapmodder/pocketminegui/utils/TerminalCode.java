package com.github.pemapmodder.pocketminegui.utils;

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

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TerminalCode{
	FORMAT_BOLD("\u001b[1m", "<font style=font-weight:bold>"),
	FORMAT_ITALIC("\u001b[3m", "<font style=font-style:italic>"),
	FORMAT_UNDERLINE("\u001b[4m", "<font style=text-decoration:underline>"),
	FORMAT_STRIKETHROUGH("\u001b[9m", "<font style=text-decoration:line-through>"),
	FORMAT_RESET("\u001b[m", "<font>"),
	COLOR_BLACK("\u001b[38;5;16m", "<font color='#000000'>"),
	COLOR_DARK_BLUE("\u001b[38;5;19m", "<font color='#0000AA'>"),
	COLOR_DARK_GREEN("\u001b[38;5;34m", "<font color='#00AA00'>"),
	COLOR_DARK_AQUA("\u001b[38;5;37m", "<font color='#00AAAA'>"),
	COLOR_DARK_RED("\u001b[38;5;124m", "<font color='#AA0000'>"),
	COLOR_PURPLE("\u001b[38;5;127m", "<font color='#AA00AA'>"),
	COLOR_GOLD("\u001b[38;5;214m", "<font color='#FFAA00'>"),
	COLOR_GRAY("\u001b[38;5;145m", "<font color='#AAAAAA'>"),
	COLOR_DARK_GRAY("\u001b[38;5;59m", "<font color='#555555'>"),
	COLOR_BLUE("\u001b[38;5;63m", "<font color='#5555FF'>"),
	COLOR_GREEN("\u001b[38;5;83m", "<font color='#55FF55'>"),
	COLOR_AQUA("\u001b[38;5;87m", "<font color='#55FFFF'>"),
	COLOR_RED("\u001b[38;5;203m", "<font color='#FF5555'>"),
	COLOR_LIGHT_PURPLE("\u001b[38;5;207m", "<font color='#FF55FF'>"),
	COLOR_YELLOW("\u001b[38;5;227m", "<font color='#FFFF55'>"),
	COLOR_WHITE("\u001b[38;5;231m", "<font color='#FFFFFF'>");

	private String ansi, html;

	String replace(String src){
		return src.replace(ansi, "</font>" + html);
	}

	public static String toHTML(String ansi){
		for(TerminalCode code : values()){
			ansi = code.replace(ansi);
		}
		return "<font>" + ansi + "</font>";
	}
}
