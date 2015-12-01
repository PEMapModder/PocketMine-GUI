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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class GetUrlThread extends Thread{
	private final static int STEP = 256;

	private URL url;
	@Getter
	private int progress = 0, max = 0;

	public ByteBuffer bb;

	public GetUrlThread(URL url){
		this.url = url;
	}

	@Override
	public void run(){
		try{
			System.out.println("Downloading: " + url);
			InputStream is = url.openStream();
			max = is.available();
			bb = ByteBuffer.allocate(max);
			for(progress = 0; progress < max; progress = Math.min(progress + STEP, max)){
				int read = Math.min(max - progress, STEP);
				byte[] buffer = new byte[read];
				is.read(buffer);
				bb.put(buffer);
				System.out.println(progress);
			}
			is.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
