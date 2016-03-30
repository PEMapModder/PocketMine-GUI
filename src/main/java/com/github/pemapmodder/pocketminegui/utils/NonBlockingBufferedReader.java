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

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class NonBlockingBufferedReader extends Thread implements Closeable{
	private final BufferedReader reader;
	private final List<String> buffer = new LinkedList<>();
	private boolean closed = false;

	@Override
	public void run(){
		while(!closed){
			try{
				String line = reader.readLine();
				if(line != null){
					synchronized(buffer){
						buffer.add(line);
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public String readLine(){
		synchronized(buffer){
			try{
				return buffer.remove(0);
			}catch(IndexOutOfBoundsException e){
				return null;
			}
		}
	}

	@Override
	public void close() throws IOException{
		closed = true;
		reader.close();
	}
}
