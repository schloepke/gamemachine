package plugins;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.config.AppConfig;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.zonemanager.ZoneHandler;

public class ZoneHandlerPlugin extends Plugin {

	private static final Logger logger = LoggerFactory.getLogger(ZoneHandlerPlugin.class);
	
	@Override
	public void start() {
		if (!AppConfig.getOrm()) {
			logger.warn("ZoneHandler disabled (requires an sql database and orm=true in config)");
			return;
		}
		
		GameMessageRoute.add(ZoneHandler.name,ZoneHandler.name,false);
		ActorUtil.createActor(ZoneHandler.class,ZoneHandler.name);
	}
}