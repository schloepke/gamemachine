package io.gamemachine.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import akka.actor.UntypedActor;
import io.gamemachine.config.AppConfig.GridConfig;
import io.gamemachine.core.CloudClient;
import io.gamemachine.core.CloudClient.CloudResponse;
import io.gamemachine.core.GameGrid;
import io.gamemachine.messages.GameConfigs;
import scala.concurrent.duration.Duration;

public class GameConfig extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(GameConfig.class);
	
	private static Map<String, Config> gameConfigs = new ConcurrentHashMap<String, Config>();
	
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
			logger.info("Undefined grid "+gridName);
			return null;
		}
	}
		

	private void updateDependencies(String gameId) {
		GameGrid.removeGridsForGame(gameId);
	}
	
	private void checkConfig() throws Exception {
		boolean updateConfig = false;
		CloudClient cloud = CloudClient.getInstance();
		CloudResponse response = cloud.getString("game_configs");
		if (response.status == 200) {
			GameConfigs configs = GameConfigs.parseFromJson(response.stringBody);
			for (io.gamemachine.messages.GameConfig config : configs.getGameConfigList()) {
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
