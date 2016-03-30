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
	COLOR_BLACK("\u001b[38;5;16m", "<font color='#000'>"),
	COLOR_DARK_BLUE("\u001b[38;5;19m", "<font color='#00A'>"),
	COLOR_DARK_GREEN("\u001b[38;5;34m", "<font color='#0A0'>"),
	COLOR_DARK_AQUA("\u001b[38;5;37m", "<font color='#0AA'>"),
	COLOR_DARK_RED("\u001b[38;5;124m", "<font color='#A00'>"),
	COLOR_PURPLE("\u001b[38;5;127m", "<font color='#A0A'>"),
	COLOR_GOLD("\u001b[38;5;214m", "<font color='#FA0'>"),
	COLOR_GRAY("\u001b[38;5;145m", "<font color='#AAA'>"),
	COLOR_DARK_GRAY("\u001b[38;5;59m", "<font color='#555'>"),
	COLOR_BLUE("\u001b[38;5;63m", "<font color='#55F'>"),
	COLOR_GREEN("\u001b[38;5;83m", "<font color='#5F5'>"),
	COLOR_AQUA("\u001b[38;5;87m", "<font color='#5FF'>"),
	COLOR_RED("\u001b[38;5;203m", "<font color='#F55'>"),
	COLOR_LIGHT_PURPLE("\u001b[38;5;207m", "<font color='#F5F'>"),
	COLOR_YELLOW("\u001b[38;5;227m", "<font color='#FF5'>"),
	COLOR_WHITE("\u001b[38;5;231m", "<font color='#FFF'>");

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
