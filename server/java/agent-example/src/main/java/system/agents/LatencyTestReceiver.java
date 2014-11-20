package system.agents;

/*
 * For internal testing only
 */

import user.messages.TestMessage;

import io.gamemachine.client.api.DynamicMessageUtil;
import io.gamemachine.client.messages.GameMessage;
import io.gamemachine.codeblocks.Codeblock;

public class LatencyTestReceiver implements Codeblock {

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof GameMessage) {
			GameMessage gameMessage = (GameMessage) message;
			TestMessage testMessage = DynamicMessageUtil.fromGameMessage(gameMessage);
			long latency = System.currentTimeMillis() - testMessage.sentAt;
			if (latency >= 3) {
				System.out.println("latency " + latency);
			}
		}
	}

	@Override
	public void awake(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown(Object message) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
