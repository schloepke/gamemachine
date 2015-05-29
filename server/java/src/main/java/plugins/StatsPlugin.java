package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.core.StatsHandler;

public class StatsPlugin extends Plugin {

	@Override
	public void start() {
		// TODO Auto-generated method stub
		GameMessageRoute.add(StatsHandler.name,StatsHandler.name,false);
		ActorUtil.createActor(StatsHandler.class,StatsHandler.name);
	}
}
