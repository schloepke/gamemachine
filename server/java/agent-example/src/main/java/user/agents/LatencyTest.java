package user.agents;

/*
 * For internal testing only and not enabled on production servers.  In production the server will drop these messages, but you will be charged.  You have been warned!
 */
import user.messages.TestMessage;

import com.game_machine.client.agent.CodeblockEnv;
import com.game_machine.client.api.Api;
import com.game_machine.client.api.ApiMessage;
import com.game_machine.client.api.DynamicMessageUtil;
import com.game_machine.client.messages.GameMessage;
import com.game_machine.codeblocks.Codeblock;

public class LatencyTest implements Codeblock {

	private CodeblockEnv env;
	private Api api;

	@Override
	public void run(Object message) throws Exception {
		if (message instanceof GameMessage) {
			
		} else if (message instanceof String) {
			for (int x = 0; x < 10; x++) {
				ApiMessage apiMessage = api.newMessage();
				TestMessage testMessage = new TestMessage();
				testMessage.sentAt = System.currentTimeMillis();
				testMessage.senderId = "LatencyTestReceiver";
				GameMessage gameMessage = DynamicMessageUtil.toGameMessage(testMessage);
				gameMessage.setDestinationId(10);
				apiMessage.setGameMessage(gameMessage);
				apiMessage.timestamp();
				apiMessage.send();
			}
			this.env.tick(10, "tick");
		}

	}

	@Override
	public void awake(Object message) {
		this.env = (CodeblockEnv) message;
		this.api = env.getApi();
		if (this.env.getReloadCount() == 0) {
			this.env.tick(10, "tick");
		}
	}

	@Override
	public void shutdown(Object message) throws Exception {

	}

}
