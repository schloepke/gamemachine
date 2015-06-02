package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.zonemanager.ZoneHandler;

public class ZoneHandlerPlugin extends Plugin {

	@Override
	public void start() {
		// TODO Auto-generated method stub
		GameMessageRoute.add(ZoneHandler.name,ZoneHandler.name,false);
		ActorUtil.createActor(ZoneHandler.class,ZoneHandler.name);
	}
}