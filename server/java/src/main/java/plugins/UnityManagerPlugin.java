package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.unitymanager.UnityManager;

public class UnityManagerPlugin extends Plugin {

	@Override
	public void start() {
		// TODO Auto-generated method stub
		GameMessageRoute.add(UnityManager.name,UnityManager.name,false);
		ActorUtil.createActor(UnityManager.class,UnityManager.name);
	}
}