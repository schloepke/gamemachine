package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.demo.NpcDemo;

public class NpcDemoPlugin extends Plugin {

	@Override
	public void start() {
		// TODO Auto-generated method stub
		GameMessageRoute.add(NpcDemo.name,NpcDemo.name,false);
		ActorUtil.createActor(NpcDemo.class,NpcDemo.name);
	}

}
