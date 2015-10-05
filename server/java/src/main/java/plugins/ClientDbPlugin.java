package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.clientDbLoader.ClientDbLoader;

public class ClientDbPlugin extends Plugin {

	@Override
	public void start() {
		GameMessageRoute.add(ClientDbLoader.name, ClientDbLoader.name, false);
		ActorUtil.createActor(ClientDbLoader.class, ClientDbLoader.name);
	}

}