package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.routing.GameMessageRoute;
import plugins.misc.CharacterLocationHandler;

public class CharacterLocationPlugin {

	public void start() {
		GameMessageRoute.add(CharacterLocationHandler.name,CharacterLocationHandler.name,false);
		ActorUtil.createActor(CharacterLocationHandler.class,CharacterLocationHandler.name);
	}
}
