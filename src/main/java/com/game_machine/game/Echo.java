package com.game_machine.game;

import akka.actor.UntypedActor;

import com.game_machine.MessageUtil;
import com.game_machine.Pb.ClientMessage;

public class Echo extends UntypedActor {

	public void onReceive(Object message) {
		if (message instanceof EchoCommand) {
			EchoCommand echo = (EchoCommand) message;
			ClientMessage clientMessage = MessageUtil.createClientMessage(null, null, echo.getClientId().getBytes());
			Gateway.sendToClient(echo.getClientId(), clientMessage.toByteArray());
		} else {
			unhandled(message);
		}
	}
}
