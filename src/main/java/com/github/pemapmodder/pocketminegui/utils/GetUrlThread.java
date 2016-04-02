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

public class GetUrlThread extends AsyncTask{
	private final static int STEP = 512;

	private final URL url;

	public byte[] array;

	public GetUrlThread(URL url){
		this.url = url;
	}

	@Override
	public void run(){
		try{
			URLConnection conn = url.openConnection();
			setMax(conn.getContentLength());
			array = new byte[getMax()];
			try(InputStream is = conn.getInputStream()){
				int progress = 0;
				int step = 0;
				int pointer = 0;
				while(true){
					byte[] buffer = new byte[STEP];
					int read = is.read(buffer);
					if(read == -1){
						setProgress(getMax());
						return;
					}
					System.arraycopy(buffer, 0, array, pointer, read);
					pointer += read;
					setProgress(progress += STEP);
					step++;
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
