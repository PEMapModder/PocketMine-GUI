<?php

/**
 * @name PMGUI-Adaptor
 * @author PEMapModder
 * @version 1.0
 * @api 2.0.0
 * @main com\github\pemapmodder\pocketminegui\plugins\Adaptor
 */

namespace com\github\pemapmodder\pocketminegui\plugins;

use pocketmine\event\Listener;
use pocketmine\event\player\PlayerJoinEvent;
use pocketmine\event\player\PlayerQuitEvent;
use pocketmine\plugin\PluginBase;
use pocketmine\scheduler\PluginTask;
use pocketmine\utils\Binary;

class Adaptor extends PluginBase implements Listener{
	public function onLoad(){
		if(((int) $this->getServer()->getProperty("pmgui.enabled")) !== 1){
			throw new \RuntimeException("Adaptor plugin must be used with PMGUI only");
		}
	}
	public function onEnable(){
		$this->getServer()->getPluginManager()->registerEvents($this, $this);
		$this->getServer()->getScheduler()->scheduleDelayedTask(new CallbackPluginTask($this, function(){
			new InternalMessage(InternalMessage::SERVER_STARTED, [
				"pluginData" => [
					"version" => $this->getDescription()->getVersion(),
				],
				"serverData" => [
					"name" => $this->getServer()->getName(),
					"version" => $this->getServer()->getPocketMineVersion(),
					"mcpeVersion" => $this->getServer()->getVersion(),
				],
			]);
		}), 1);
	}
	public function onJoin(PlayerJoinEvent $event){
		new InternalMessage(InternalMessage::PLAYER_JOIN, ["name" => $event->getPlayer()->getName()]);
	}
	public function onQuit(PlayerQuitEvent $event){
		new InternalMessage(InternalMessage::PLAYER_QUIT, ["name" => $event->getPlayer()->getName()]);
	}
	public function onDisable(){
		new InternalMessage(InternalMessage::PLUGIN_DISABLED);
	}
}

class InternalMessage{
	const SERVER_STARTED = 0;
	const PLAYER_JOIN = 1;
	const PLAYER_QUIT = 2;
	const PLUGIN_DISABLED = 3;

	private $action;
	private $payload;

	public function __construct(int $action, $payload, bool $send = true){
		$this->action = $action;
		$this->payload = $payload;
		if($send){
			$this->send();
		}
	}
	public function send(){
		$payload = json_encode($this->payload);
		echo "\x1b]PMGUI;" . $this->action . ":" . $payload . "\x07";
	}
}
class CallbackPluginTask extends PluginTask{
	private $callable;

	public function __construct(Adaptor $plugin, callable $callable){
		parent::__construct($plugin);
		$this->callable = $callable;
	}
	public function onRun($ticks){
		$c = $this->callable;
		$c();
	}
}
