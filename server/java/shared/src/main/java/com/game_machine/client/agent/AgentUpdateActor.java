package com.game_machine.client.agent;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;

public class AgentUpdateActor extends UntypedActor {

	private AgentUpdater updater;
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof String) {
			String imsg = (String) message;
			if (imsg.equals("update_codeblocks")) {
				updater.updateCodeblocks();
				tick(5000, "update_codeblocks");
			}
		} else {
			unhandled(message);
		}
	}

	@Override
	public void preStart() {
		updater = new AgentUpdater();
		tick(5000, "update_codeblocks");
	}

	public void tick(int delay, String message) {
		getContext()
				.system()
				.scheduler()
				.scheduleOnce(Duration.create(delay, TimeUnit.MILLISECONDS), getSelf(), message,
						getContext().dispatcher(), null);
	}
}
