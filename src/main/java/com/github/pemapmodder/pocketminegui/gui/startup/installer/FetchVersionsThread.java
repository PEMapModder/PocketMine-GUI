package com.github.pemapmodder.pocketminegui.gui.startup.installer;

import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
public class FetchVersionsThread extends Thread{
	@Getter
	private ChooseVersionCard card;
	@Getter
	private final List<Release> releases = new ArrayList<>();

	public FetchVersionsThread(ChooseVersionCard card){
		this.card = card;
	}

	@Override
	public void run(){
		fetchGitHubReleases();

	}

	private void fetchGitHubReleases(){
		try{
			URL url = new URL("https://api.github.com/repos/PocketMine/PocketMine-MP/releases");
			try{
				String jsonString = IOUtils.toString(url);
				try{
					JSONArray releasesArray = new JSONArray(jsonString);
					for(Object object : releasesArray){
						JSONObject jo = (JSONObject) object;
						DateFormat date = new SimpleDateFormat("YYYY-MM-DDTHH:MM:SSZ");
						JSONArray assets = jo.getJSONArray("assets");
						String pharUrl = null;
						for(Object asset : assets){
							JSONObject assetObject = (JSONObject) asset;
							if(assetObject.getString("name").endsWith(".phar")){
								pharUrl = assetObject.getString("name");
							}
						}
						if(pharUrl == null){
							continue;
						}
						Release release = new Release(
								jo.getString("tag_name"),
								jo.getBoolean("prerelease") ? Release.ReleaseType.PRE_RELEASE : Release.ReleaseType.RELEASE,
								date.parse(jo.getString("published_at")).getTime(),
								pharUrl
						);
						synchronized(releases){
							releases.add(release);
						}
					}
				}catch(JSONException | ParseException e){
					e.printStackTrace();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
	}

	private void fetchDevBuilds(){
		try{
			URL url = new URL("http://jenkins.pocketmine.net/job/PocketMine-MP/api/json");

			try{
				String jsonString = IOUtils.toString(url);
				try{
					JSONObject object = new JSONObject(jsonString);
					JSONArray array = object.getJSONArray("builds");

				}catch(JSONException e){
					e.printStackTrace();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
	}

	public static class Release{
		@Getter
		private String name;
		@Getter
		private ReleaseType type;
		@Getter
		private long publishTime;
		@Getter
		private String pharUrl;

		public Release(String name, ReleaseType type, long publishTime, String pharUrl){
			this.name = name;
			this.type = type;
			this.publishTime = publishTime;
			this.pharUrl = pharUrl;
		}

		public enum ReleaseType{
			RELEASE,
			PRE_RELEASE,
			DEVELOPMENT,
			BLEEDING,
			KATANA
		}
	}
}
