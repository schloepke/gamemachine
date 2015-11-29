package io.gamemachine.process;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;

public class AkkaProcessRunner extends GameMessageActor {
	
	public static String name = AkkaProcessRunner.class.getSimpleName();
	
	private static ProcessManager manager;

	private long checkInterval = 10000l;
	

	@Override
	public void awake() {
		if (manager == null) {
			manager = new ProcessManager(false);
			manager.startAll();
		}
		
		scheduleOnce(checkInterval,"check");
	}

	public void onTick(String message) {
		manager.checkStatus();
		scheduleOnce(checkInterval,"check");
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}
	

}
