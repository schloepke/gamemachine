package plugins;

import io.gamemachine.core.GameEntityManagerService;
import io.gamemachine.core.Plugin;
import plugins.demo.NpcDemo;
import plugins.demo.SimpleGameEntityManager;

public class GameEntityManagerPlugin extends Plugin {

	@Override
	public void start() {
		SimpleGameEntityManager characterManager = new SimpleGameEntityManager();
		GameEntityManagerService.instance().setGameEntityManager(characterManager);
		NpcDemo.createDemoNpcs();
	}

}
