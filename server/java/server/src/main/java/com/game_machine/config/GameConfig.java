package com.game_machine.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import GameMachine.Messages.GameConfigs;
import akka.actor.UntypedActor;

import com.game_machine.config.AppConfig.GridConfig;
import com.game_machine.core.CloudClient;
import com.game_machine.core.GameGrid;
import com.game_machine.core.CloudClient.CloudResponse;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

public class GameConfig extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(GameConfig.class);
	
	private static Map<String, Config> gameConfigs = new ConcurrentHashMap<String, Config>();
	private static Map<String, Integer> messageLimits = new ConcurrentHashMap<String, Integer>();
	private static Map<String, Integer> connectionLimits = new ConcurrentHashMap<String, Integer>();
	
	private Map<String,Integer> versions = new HashMap<String,Integer>();
	
	private static Config getConfig(String gameId) {
		Config config = null;
		if (gameConfigs.containsKey(gameId)) {
			config = gameConfigs.get(gameId);
		} else {
			config = AppConfig.getGameConfig();
		}
		return config;
	}
	
	public static GridConfig getGridConfig(String gameId, String gridName) {
		Config config = getConfig(gameId);
		
		try {
			return new GridConfig(gridName, config.getString("grids." + gridName));
		} catch (ConfigException.Missing e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer getMessageLimit(String gameId) {
		if (!messageLimits.containsKey(gameId)) {
			Config config = getConfig(gameId);
			messageLimits.put(gameId, config.getInt("message_limit"));
		}
		return messageLimits.get(gameId);
	}
	
	public static Integer getConnectionLimit(String gameId) {
		if (!connectionLimits.containsKey(gameId)) {
			Config config = getConfig(gameId);
			connectionLimits.put(gameId, config.getInt("connection_limit"));
		}
		return connectionLimits.get(gameId);
	}

	private void updateDependencies(String gameId) {
		GameGrid.removeGridsForGame(gameId);
		GameLimits.resetMessageLimit(gameId);
	}
	
	private void checkConfig() throws Exception {
		boolean updateConfig = false;
		CloudClient cloud = CloudClient.getInstance();
		CloudResponse response = cloud.getString("game_configs");
		if (response.status == 200) {
			GameConfigs configs = GameConfigs.parseFromJson(response.stringBody);
			for (GameMachine.Messages.GameConfig config : configs.getGameConfigList()) {
				if (versions.containsKey(config.getGameId())) {
					if (!versions.get(config.getGameId()).equals(config.getVersion())) {
						updateConfig = true;
					}
				} else {
					updateConfig = true;
				}
				
				if (updateConfig) {
					logger.debug("Updating game config "+config.getGameId()+" version "+config.getVersion());
					Config conf = ConfigFactory.parseString(config.getConfig()).getConfig("game");
					gameConfigs.put(config.getGameId(), conf);
					versions.put(config.getGameId(), config.getVersion());
					if (messageLimits.containsKey(config.getGameId())) {
						messageLimits.remove(config.getGameId());
					}
					updateDependencies(config.getGameId());
					updateConfig = false;
				}
			}
		}
	}
	
	@Override
	public void preStart() {
		tick(10000,"check_config");
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String msg = (String)message;
			if (msg.equals("check_config")) {
				checkConfig();
				tick(10000,"check_config");
			}
		}
		
	}
	
	public void tick(int delay, Object message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}
}
