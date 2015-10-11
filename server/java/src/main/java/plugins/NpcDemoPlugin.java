package plugins;

import io.gamemachine.core.Plugin;
import plugins.npcdemo.NpcDemo;

public class NpcDemoPlugin extends Plugin {

	@Override
	public void start() {
		NpcDemo.createDemoNpcs();
		
	}

}
