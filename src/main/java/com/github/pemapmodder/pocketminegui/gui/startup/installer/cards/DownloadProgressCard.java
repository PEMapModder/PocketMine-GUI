package com.github.pemapmodder.pocketminegui.gui.startup.installer.cards;

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

import com.github.pemapmodder.pocketminegui.gui.startup.installer.InstallServerActivity;
import com.github.pemapmodder.pocketminegui.lib.card.Card;
import com.github.pemapmodder.pocketminegui.utils.GetUrlThread;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadProgressCard extends Card{
	private InstallServerActivity activity;
	private JProgressBar progressBar;
	private JLabel progressLabel;
	private boolean maxSet = false;
	private Timer progressCheck;
	private GetUrlThread thread;

	public DownloadProgressCard(InstallServerActivity activity){
		this.activity = activity;
		add(new JLabel("Downloading... "));
		add(progressBar = new JProgressBar());
		add(progressLabel = new JLabel());
	}

	@Override
	public void onEntry(){
		activity.getBackButton().setEnabled(false);
		activity.getNextButton().setEnabled(false);
		URL url;
		try{
			url = new URL(activity.getSelectedRelease().getPharUrl());
		}catch(MalformedURLException e){
			e.printStackTrace();
			return;
		}
		thread = new GetUrlThread(url);
		progressCheck = new Timer(100, ev -> {
			if(thread.getMax() > 0){
				if(!maxSet){
					progressBar.setMaximum(thread.getMax());
				}
				progressBar.setValue(thread.getProgress());
				progressLabel.setText(Math.round(thread.getProgress() / 102.4) / 10 + "KB / " +
						Math.round(thread.getMax() / 1024.0) / 10 + "KB");
				if(thread.getMax() == thread.getProgress()){
					progressCheck.stop();
					byte[] data = thread.bb.array();
					File home = activity.getSelectedHome();
					try{
						if(!home.mkdirs()){
//							throw new IOException("Cannot make directories");
						}
						File phar = new File(home, "PocketMine-MP.phar");
						OutputStream os = new FileOutputStream(phar);
						os.write(data);
						os.close();
					}catch(IOException e){
						e.printStackTrace();
					}
					activity.getNextButton().setEnabled(true);
					revalidate();
				}
			}
		});
		progressCheck.start();
		thread.start();
	}

	@Override
	public String getCardName(){
		return "Installing PocketMine-MP...";
	}
}
