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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LineReader{
	private final String src;
	private int pointer = 0;

	public void expect(String string){
		if(!src.substring(pointer, pointer + string.length()).equals(string)){
			throw new RuntimeException("Unexpected console output");
		}
		pointer += string.length();
	}

	public void expect(char ch){
		if(src.charAt(pointer) != ch){
			throw new RuntimeException("Unexpected console output");
		}
		++pointer;
	}

	public String readBefore(char until){
		int pos = src.indexOf(until, pointer);
		String substring = src.substring(pointer, pos);
		pointer = pos;
		return substring;
	}

	public String readThrough(char until){
		int pos = src.indexOf(until, pointer);
		String substring = src.substring(pointer, pos);
		pointer = pos + 1;
		return substring;
	}

	public String readUntil(char until){
		int pos = src.indexOf(until, pointer);
		String substring = src.substring(pointer, pos + 1);
		pointer = pos + 1;
		return substring;
	}

	public String readRemaining(){
		String substring = src.substring(pointer);
		pointer = src.length();
		return substring;
	}

	public boolean eof(){
		return pointer >= src.length();
	}
}
