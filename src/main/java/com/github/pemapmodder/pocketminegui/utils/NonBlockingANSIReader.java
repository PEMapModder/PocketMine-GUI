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

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

@RequiredArgsConstructor
public class NonBlockingANSIReader extends Thread{
	@Getter private final InputStream input;
	@Getter private final LinkedList<Entry> output = new LinkedList<>();
	@Getter private boolean closed = false;
	private final StringBuilder buffer = new StringBuilder();
	private EntryType tmpEntryType;

	@Override
	public void run(){
		try{
			while(!closed){
				char ch = (char) input.read();
				boolean matched = false;
				for(char match : getTerminatingChar()){
					if(match == ch){
						matched = true;
						break;
					}
				}
				if(matched){
					if(buffer.length() == 0){
						continue;
					}
					Entry entry = new Entry(tmpEntryType, buffer.toString());
					synchronized(output){
						output.add(entry);
					}
					buffer.setLength(0);
					continue;
				}
				buffer.append(ch);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				input.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	private char[] getTerminatingChar(){
		String title = "\u001b]0;";
		String pmgui = "\u001b]PMGUI;";
		tmpEntryType = EntryType.CONSOLE;
		if(buffer.length() >= title.length() && buffer.substring(0, title.length()).equals(title)){
			tmpEntryType = EntryType.TITLE;
			return new char[]{'\u0007'};
		}else if(buffer.length() >= pmgui.length() && buffer.substring(0, pmgui.length()).equals(pmgui)){
			tmpEntryType = EntryType.PMGUI;
			return new char[]{'\u0007'};
		}
		return System.lineSeparator().toCharArray();
	}

	public Entry nextOutput(){
		synchronized(output){
			try{
				return output.remove(0);
			}catch(IndexOutOfBoundsException e){
				return null;
			}
		}
	}

	@Getter
	public static class Entry{
		private EntryType type;
		private String line;

		public Entry(EntryType type, String line){
			if(type == EntryType.TITLE){
				line = line.substring(4);
			}
			this.type = type;
			this.line = line;
		}
	}

	public enum EntryType{
		CONSOLE,
		TITLE,
		PMGUI
	}

	public void close(){
		closed = true;
	}
}
