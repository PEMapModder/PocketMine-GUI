package com.github.pemapmodder.pocketminegui.gui.startup.installer;

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

public class FetchVersionsThread extends Thread{
	@Getter
	private ChooseVersionCard card;
	@Getter
	private final List<Release> releases = new ArrayList<>();
	@Getter
	private boolean done = false;

	public FetchVersionsThread(ChooseVersionCard card){
		this.card = card;
	}

	@Override
	public void run(){
		fetchGitHubReleases();
		fetchJenkinsBuilds("http://jenkins.pocketmine.net/job/PocketMine-MP/api/json?depth=1", "dev", ReleaseType.DEVELOPMENT);
		fetchJenkinsBuilds("http://jenkins.pocketmine.net/job/PocketMine-MP-Bleeding/api/json?depth=1", "bleeding", ReleaseType.BLEEDING);
		done = true;
	}

	private void fetchGitHubReleases(){
		try{
			URL url = new URL("https://api.github.com/repos/PocketMine/PocketMine-MP/releases");
			String jsonString = IOUtils.toString(url);
			JSONArray releasesArray = new JSONArray(jsonString);
			for(Object object : releasesArray){
				JSONObject jo = (JSONObject) object;
				DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				JSONArray assets = jo.getJSONArray("assets");
				String pharUrl = null;
				for(Object asset : assets){
					JSONObject assetObject = (JSONObject) asset;
					if(assetObject.getString("name").endsWith(".phar")){
						pharUrl = assetObject.getString("browser_download_url");
					}
				}
				if(pharUrl == null){
					continue;
				}
				Release release = new Release(
						jo.getString("tag_name"),
						jo.getBoolean("prerelease") ? ReleaseType.BETA : ReleaseType.STABLE,
						date.parse(jo.getString("published_at")).getTime(),
						pharUrl, null
				);
				synchronized(releases){
					releases.add(release);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(NullPointerException | ClassCastException | JSONException | ParseException e){
			System.err.println("GitHub API returned invalid value!");
			e.printStackTrace();
		}
	}

	private void fetchJenkinsBuilds(String urlPath, String buildPrefix, ReleaseType releaseType){
		try{
			URL url = new URL(urlPath);
			String jsonString = IOUtils.toString(url);
			JSONObject object = new JSONObject(jsonString);
			JSONArray array = object.getJSONArray("builds");
			for(Object arrayObject : array){
				JSONObject build = (JSONObject) arrayObject;
				if(!build.getString("result").equals("SUCCESS")){
					continue;
				}
				String artifactName = null;
				for(Object artifactObject : build.getJSONArray("artifacts")){
					JSONObject artifact = (JSONObject) artifactObject;
					if(artifact.getString("fileName").endsWith(".phar")){
						artifactName = artifact.getString("relativePath");
					}
				}
				if(artifactName == null){
					continue;
				}
				Release release = new Release(
						buildPrefix + "-" + build.getInt("number"),
						releaseType,
						build.getLong("timestamp"),
						build.getString("url") + "artifact/" + artifactName, null
				);
				synchronized(releases){
					releases.add(release);
				}
			}
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException | JSONException | NullPointerException | ClassCastException e){
			System.err.println("Jenkins API returned invalid value!");
			e.printStackTrace();
		}
	}

}
