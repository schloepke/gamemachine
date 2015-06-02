package io.gamemachine.routing;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.RpcMessage;

public class RpcTest extends GameMessageActor {

	@Override
	public void awake() {
		scheduleOnce(1000l, "tick");
		
	}

	@Override
	public void onTick(String message) {
		RpcMessage msg = new RpcMessage();
		msg.messageType = RpcMessage.MessageType.TEST;
		RpcMessage result = RpcHandler.call(msg, "unity1");
		scheduleOnce(1000l, "tick");
	}
	
	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}

}
