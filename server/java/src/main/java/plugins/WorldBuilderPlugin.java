package plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.landrush.BuildObjectHandler;


public class WorldBuilderPlugin extends Plugin {
	private static final Logger logger = LoggerFactory.getLogger(WorldBuilderPlugin.class);
	
	public void start() {
		if (!AppConfig.getOrm()) {
			logger.warn("LandRush requires an sql database and orm=true in config");
			return;
		}
		
		GameMessageRoute.add(BuildObjectHandler.name,BuildObjectHandler.name,false);
		ActorUtil.createActor(BuildObjectHandler.class,BuildObjectHandler.name);
	}
}
