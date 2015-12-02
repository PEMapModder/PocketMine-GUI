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

import com.github.pemapmodder.pocketminegui.gui.startup.config.ServerOptionsActivity;
import com.github.pemapmodder.pocketminegui.gui.startup.installer.InstallServerActivity;
import com.github.pemapmodder.pocketminegui.lib.card.Card;
import org.apache.commons.lang3.RandomUtils;
import org.yaml.snakeyaml.Yaml;

import javax.swing.JButton;
import java.io.*;
import java.util.*;

public class ServerSetupCard extends Card{
	private final static Map<String, Object> SERVER_PROPERTIES_MAP = new HashMap<>();
	private final static Map<String, String> SERVER_PROPERTIES_DESC_MAP = new HashMap<>();
	private final static Map<String, Object> POCKETMINE_YML_MAP = new HashMap<>();
	private final static Map<String, String> POCKETMINE_YML_DESC_MAP = new HashMap<>();

	static{
		initServerPropertiesMap();
		initServerPropertiesDescMap();
		initPocketMineYmlMap();
		initPocketMineYmlDescMap();
	}

	private static void initServerPropertiesMap(){
		SERVER_PROPERTIES_MAP.put("motd", "Minecraft: PE Server");
		SERVER_PROPERTIES_MAP.put("server-port", 19132);
		SERVER_PROPERTIES_MAP.put("white-list", false);
		SERVER_PROPERTIES_MAP.put("announce-player-achievements", true);
		SERVER_PROPERTIES_MAP.put("spawn-protection", 16);
		SERVER_PROPERTIES_MAP.put("max-players", 20);
		SERVER_PROPERTIES_MAP.put("allow-flight", false);
		SERVER_PROPERTIES_MAP.put("spawn-animals", true);
		SERVER_PROPERTIES_MAP.put("spawn-mobs", true);
		SERVER_PROPERTIES_MAP.put("gamemode", 0);
		SERVER_PROPERTIES_MAP.put("force-gamemode", false);
		SERVER_PROPERTIES_MAP.put("hardcore", false);
		SERVER_PROPERTIES_MAP.put("pvp", true);
		SERVER_PROPERTIES_MAP.put("difficulty", 1);
		SERVER_PROPERTIES_MAP.put("generator-settings", "");
		SERVER_PROPERTIES_MAP.put("level-name", "world");
		SERVER_PROPERTIES_MAP.put("level-seed", "");
		SERVER_PROPERTIES_MAP.put("level-type", "DEFAULT");
		SERVER_PROPERTIES_MAP.put("enable-query", true);
		SERVER_PROPERTIES_MAP.put("enable-rcon", true);
		SERVER_PROPERTIES_MAP.put("rcon.password", new String(Base64.getMimeEncoder().encode(RandomUtils.nextBytes(20))).substring(3, 13));
		SERVER_PROPERTIES_MAP.put("auto-save", true);
	}

	private static void initServerPropertiesDescMap(){
		SERVER_PROPERTIES_DESC_MAP.put("motd", "Server display name on Minecraft server list");
		SERVER_PROPERTIES_DESC_MAP.put("server-port", "UDP port to run server on");
		SERVER_PROPERTIES_DESC_MAP.put("white-list", "Turn on whitelist. You can manage the whitelist later.");
		SERVER_PROPERTIES_DESC_MAP.put("announce-player-achievements", "Whether to broadcast a message when a player has got an achievement");
		SERVER_PROPERTIES_DESC_MAP.put("spawn-protection", "Radius to disallow non-ops to build on");
		SERVER_PROPERTIES_DESC_MAP.put("max-players", "Maximum number of players allowed on the server");
		SERVER_PROPERTIES_DESC_MAP.put("allow-flight", "If set to false, PocketMine will kick players that appear to be flying.");
		SERVER_PROPERTIES_DESC_MAP.put("spawn-animals", "whether to spawn animals");
		SERVER_PROPERTIES_DESC_MAP.put("spawn-mobs", "whether to spawn mobs");
		SERVER_PROPERTIES_DESC_MAP.put("gamemode", "Default gamemode of this server.\n0 - survival\n1 - creative\n" +
				"2 - adventure mode (cannot break blocks)\n3 - spectator mode (no block breaking, noclip, no PvP, flying)");
		SERVER_PROPERTIES_DESC_MAP.put("force-gamemode", "Whether to set the player back to the old gamemode");
		SERVER_PROPERTIES_DESC_MAP.put("hardcore", "Whether to ban player after he died");
		SERVER_PROPERTIES_DESC_MAP.put("pvp", "Whether to enable PvP");
		SERVER_PROPERTIES_DESC_MAP.put("difficulty", "Difficulty of the server.\n0 - Peaceful mode\n1 - Easy\n2 - Normal\n3 - Hard");
		SERVER_PROPERTIES_DESC_MAP.put("generator-settings", "Superflat formula if level-type is FLAT");
		SERVER_PROPERTIES_DESC_MAP.put("level-name", "Name for the default world");
		SERVER_PROPERTIES_DESC_MAP.put("level-seed", "Seed for the default world (only applies if world is newly generated)");
		SERVER_PROPERTIES_DESC_MAP.put("level-type", "Type for the default world (only applies if world is newly generated)\n" +
				"DEFAULT - normal world with terrain\nFLAT - superflat world");
		SERVER_PROPERTIES_DESC_MAP.put("enable-query", "Enable UT3 querying, used by server list websites.");
		SERVER_PROPERTIES_DESC_MAP.put("enable-rcon", "Enable RCON, used to connect to console remotely.");
		SERVER_PROPERTIES_DESC_MAP.put("rcon.password", "The password to connect with RCON");
		SERVER_PROPERTIES_DESC_MAP.put("auto-save", "Enable auto saving");
	}

	@SuppressWarnings("unchecked")
	private static void initPocketMineYmlMap(){
		Yaml yaml = new Yaml();
		Map<String, Object> nest = (Map<String, Object>) yaml.load(ServerSetupCard.class.getClassLoader().getResourceAsStream("pocketmine.yml"));
		addNestedToPlain("", nest, POCKETMINE_YML_MAP);
	}

	private static void initPocketMineYmlDescMap(){
		POCKETMINE_YML_DESC_MAP.put("settings.language", "Three-letter language code for server-side localization\n" +
				"Check your language code on https://en.wikipedia.org/wiki/List_of_ISO_639-2_codes");
		POCKETMINE_YML_DESC_MAP.put("settings.force-language", "Whether to send all text translated to server locale or " +
				"let the device handle them");
		POCKETMINE_YML_DESC_MAP.put("settings.shutdown-message", "The kick message when the server stops");
		POCKETMINE_YML_DESC_MAP.put("settings.query-plugins", "Allow listing plugins via Query");
		POCKETMINE_YML_DESC_MAP.put("settings.deprecated-verbose", "Show a console message when a plugin uses " +
				"deprecated API methods");
		POCKETMINE_YML_DESC_MAP.put("settings.enable-profiling", "Enable plugin and core profiling by default");
		POCKETMINE_YML_DESC_MAP.put("settings.profile-report-trigger", "Will only add results when tick measurement is " +
				"below or equal to given value (default 20)");
		POCKETMINE_YML_DESC_MAP.put("settings.async-workers", "Number of AsyncTask workers.\n" +
				"Used for plugin asynchronous tasks, world generation, compression and web communication.\n" +
				"Set this approximately to your number of cores.\n" +
				"If set to auto, it'll try to detect the number of cores (or use 2)");
		POCKETMINE_YML_DESC_MAP.put("memory.global-limit", "Global soft memory limit in megabytes. Set to 0 to disable\n" +
				"This will trigger low-memory-triggers and fire an event to free memory when the usage goes over this");
		POCKETMINE_YML_DESC_MAP.put("memory.main-limit", "Main thread soft memory limit in megabytes. Set to 0 to " +
				"disable\nThis will trigger low-memory-triggers and fire an event to free memory when the usage goes over this");
		POCKETMINE_YML_DESC_MAP.put("memory.main-hard-limit", "Main thread hard memory limit in megabytes. Set to 0 to disable\n" +
				"This will stop the server when the limit is surpassed");
		POCKETMINE_YML_DESC_MAP.put("memory.check-rate", "Period in ticks to check memory (default 1 second)");
		POCKETMINE_YML_DESC_MAP.put("memory.continuous-trigger", "Continue firing low-memory triggers and event while on low memory");
		POCKETMINE_YML_DESC_MAP.put("memory.continuous-trigger-rate", "Only if memory.continuous-trigger is enabled. " +
				"Specifies the rate in memory.check-rate steps (default 30 seconds)");
		POCKETMINE_YML_DESC_MAP.put("memory.garbage-collection.period", "Period in ticks to fire the garbage collector " +
				"manually (default 30 minutes), set to 0 to disable\nThis only affect the main thread. Other threads " +
				"should fire their own collections");
		POCKETMINE_YML_DESC_MAP.put("memory.garbage-collection.collect-async-worker", "Fire asynchronous tasks to " +
				"collect garbage from workers");
		POCKETMINE_YML_DESC_MAP.put("memory.garbage-collection.low-memory-trigger", "Trigger on low memory");
		POCKETMINE_YML_DESC_MAP.put("memory.max-chunks.trigger-limit", "Limit of chunks to load per player, overrides chunk-sending.max-chunks");
		POCKETMINE_YML_DESC_MAP.put("memory.max-chunks.trigger-chunk-collect", "Do chunk garbage collection on trigger");
		POCKETMINE_YML_DESC_MAP.put("memory.max-chunks.low-memory-trigger", "Trigger on low memory");
		POCKETMINE_YML_DESC_MAP.put("memory.world-caches.disable-chunk-cache", "Disable caching chunks");
		POCKETMINE_YML_DESC_MAP.put("memory.world-caches.low-memory-trigger", "Clear cache upon low memory");
		POCKETMINE_YML_DESC_MAP.put("network.batch-threshold", "Threshold for batching packets, in bytes. Only these " +
				"packets will be compressed\nSet to 0 to compress everything, -1 to disable.");
		POCKETMINE_YML_DESC_MAP.put("network.compression-level", "Compression level used when sending batched packets. " +
				"Higher = more CPU, less bandwidth usage");
		POCKETMINE_YML_DESC_MAP.put("network.async-compression", "Use AsyncTasks for compression. Adds half/one tick " +
				"delay, less CPU load on main thread");
		POCKETMINE_YML_DESC_MAP.put("network.upnp-forwarding", "Experimental, only for Windows. Tries to use UPnP to " +
				"automatically port forward");
		POCKETMINE_YML_DESC_MAP.put("debug.level", "Set to 2 to show debug messages on console, 1 for a cleaner console");
		POCKETMINE_YML_DESC_MAP.put("debug.commands", "Enables these debug commands:\n> /status\n> /gc\n> /dumpmemory");
		// TODO POCKETMINE_YML_DESC_MAP
	}

	@SuppressWarnings("unchecked")
	public ServerSetupCard(InstallServerActivity activity){
		JButton serverPropertiesButton = new JButton("General server properties");
		serverPropertiesButton.addActionListener(e -> new ServerOptionsActivity("Server properties editor", activity,
				new HashMap<>(SERVER_PROPERTIES_MAP), new HashMap<>(SERVER_PROPERTIES_DESC_MAP)){
			@Override
			protected void onResult(Map<String, Object> opts){
				try{
					OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(
							new File(activity.getSelectedHome(), "server.properties")));
					writer.append("#Properties Config file\r\n")
							.append("#Generated by PocketMine-GUI\r\n");
					for(Map.Entry<String, Object> entry : opts.entrySet()){
						writer.append(entry.getKey())
								.append('=')
								.append(entry.getValue().toString());
					}
				}catch(IOException e1){
					e1.printStackTrace();
				}
			}
		}.init());
		JButton pocketmineOpts = new JButton("PocketMine-specific settings");
		pocketmineOpts.addActionListener(e -> new ServerOptionsActivity("PocketMine settings editor", activity,
				new HashMap<>(POCKETMINE_YML_MAP), new HashMap<>(POCKETMINE_YML_DESC_MAP)){
			@Override
			protected void onResult(Map<String, Object> opts){
				Yaml yaml = new Yaml();
				try{
					yaml.dump(convertPlainToNested(opts), new OutputStreamWriter(new FileOutputStream(
							new File(activity.getSelectedHome(), "pocketmine.yml"))));
				}catch(FileNotFoundException e1){
					e1.printStackTrace();
				}
			}
		}.init());
		add(serverPropertiesButton);
		add(pocketmineOpts);
	}

	@SuppressWarnings("unchecked")
	private static void addNestedToPlain(String prefix, Map<String, Object> nest, Map<String, Object> plain){
		for(Map.Entry<String, Object> entry : nest.entrySet()){
			Object value = entry.getValue();
			if(value instanceof Map){
				addNestedToPlain(prefix + entry.getKey() + ".", (Map<String, Object>) value, plain);
			}else{
				plain.put(prefix + entry.getKey(), value);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> convertPlainToNested(Map<String, Object> plain){
		Map<String, Object> out = new HashMap<>();
		for(Map.Entry<String, Object> entry : plain.entrySet()){
			Map<String, Object> current = out;
			Object value = entry.getValue();
			String[] parts = entry.getKey().split("\\.");
			List<String> strings = new ArrayList<>(Arrays.asList(parts));
			while(!strings.isEmpty()){
				String name = strings.remove(0);
				if(!current.containsKey(name)){
					if(strings.isEmpty()){
						current.put(name, value);
						break;
					}
					current.put(name, new HashMap<String, Object>());
				}
				current = (Map<String, Object>) current.get(name);
			}
		}
		return out;
	}

	@Override
	public String getCardName(){
		return "Server configuration";
	}
}
