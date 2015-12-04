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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

public class GetUrlThread extends AsyncTask{
	private final static int STEP = 1024;

	private final URL url;

	public ByteBuffer bb;

	public GetUrlThread(URL url){
		this.url = url;
	}

	@Override
	public void run(){
		try{
			System.out.println("Downloading: " + url);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			setMax(conn.getContentLength());
			bb = ByteBuffer.allocate(getMax());
			for(setProgress(0); true; setProgress(Math.min(getProgress() + STEP, getMax()))){
				byte[] buffer = new byte[STEP];
				int len = is.read(buffer);
				if(len == -1){
					break;
				}
				bb.put(buffer, 0, len);
			}
			is.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
