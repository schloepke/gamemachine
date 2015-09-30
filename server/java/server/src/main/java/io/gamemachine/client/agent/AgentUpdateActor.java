package io.gamemachine.client.agent;

import io.gamemachine.client.SimpleUdpClient;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import akka.actor.UntypedActor;

public class AgentUpdateActor extends UntypedActor {

	private static int lastCount = 0;
	private AgentUpdater updater;
	
	@Override
	public void onReceive(Object message) throws Exception {
		int current = SimpleUdpClient.messageCount.incrementAndGet();
		int count = (current - lastCount) / 5;
		lastCount = current;
		System.out.println(count);
		if (message instanceof String) {
			String imsg = (String) message;
			if (imsg.equals("update_codeblocks")) {
				updater.updateCodeblocks();
				tick(5000, "update_codeblocks");
			} else if (imsg.startsWith("disconnected")) {
				String[] disconnected = imsg.split(":");
				updater.playerDisconnected(disconnected[1]);
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
