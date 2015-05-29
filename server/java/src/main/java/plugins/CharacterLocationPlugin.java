package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.core.CharacterLocationHandler;

public class CharacterLocationPlugin extends Plugin {

	public void start() {
		GameMessageRoute.add(CharacterLocationHandler.name,CharacterLocationHandler.name,false);
		ActorUtil.createActor(CharacterLocationHandler.class,CharacterLocationHandler.name);
	}
}
