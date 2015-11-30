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

import com.github.pemapmodder.pocketminegui.gui.startup.config.ServerOptionsActivity;
import com.github.pemapmodder.pocketminegui.lib.card.Card;
import org.apache.commons.lang3.RandomUtils;
import org.yaml.snakeyaml.Yaml;

import javax.swing.JButton;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ServerSetupCard extends Card{
	@SuppressWarnings("unchecked")
	public ServerSetupCard(InstallServerActivity activity){
		JButton serverPropertiesButton = new JButton("General server properties");
		serverPropertiesButton.addActionListener(e -> {
			Map<String, Object> map = new HashMap<>();
			map.put("motd", "Minecraft: PE Server");
			map.put("server-port", 19132);
			map.put("white-list", false);
			map.put("announce-player-achievements", true);
			map.put("spawn-protection", 16);
			map.put("max-players", 20);
			map.put("allow-flight", false);
			map.put("spawn-animals", true);
			map.put("spawn-mobs", true);
			map.put("gamemode", 0);
			map.put("force-gamemode", false);
			map.put("hardcore", false);
			map.put("pvp", true);
			map.put("difficulty", 1);
			map.put("generator-settings", "");
			map.put("level-name", "world");
			map.put("level-seed", "");
			map.put("level-type", "DEFAULT");
			map.put("enable-query", true);
			map.put("enable-rcon", true);
			map.put("rcon.password", new String(Base64.getMimeEncoder().encode(RandomUtils.nextBytes(20))).substring(3, 13));
			map.put("auto-save", true);
			new ServerOptionsActivity("Server properties editor", activity, map).init();
		});
		JButton pocketmineOpts = new JButton("PocketMine-specific settings");
		pocketmineOpts.addActionListener(e -> {
			Yaml yaml = new Yaml();
			Map<String, Object> map = (Map<String, Object>) yaml.load(getClass().getClassLoader().getResourceAsStream("pocketmine.yml"));
			new ServerOptionsActivity("PocketMine settings editor", activity, map).init();
		});
		add(serverPropertiesButton);
		add(pocketmineOpts);
	}

	@Override
	public String getCardName(){
		return "Server configuration";
	}
}
