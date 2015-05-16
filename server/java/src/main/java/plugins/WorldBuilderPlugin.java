package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.world_builder.WorldBuilderHandler;


public class WorldBuilderPlugin extends Plugin {

	public void start() {
		GameMessageRoute.add(WorldBuilderHandler.name,WorldBuilderHandler.name,false);
		ActorUtil.createActor(WorldBuilderHandler.class,WorldBuilderHandler.name);
	}
}
