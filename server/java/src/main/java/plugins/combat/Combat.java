
package plugins.combat;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.core.PlayerCommands;

public class Combat extends GameMessageActor {

	public static String name = Combat.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
	
	     // Reliable messages handled here
		if (exactlyOnce(gameMessage)) {
			
			setReply(gameMessage);
		}
		
		// Sending a response to the player from an unreliable message
		//PlayerCommands.sendGameMessage(gameMessage, playerId);
		
	}

}
