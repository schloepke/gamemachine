package pvp_game;

import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;

public class NpcManager extends GameMessageActor {

	public static String name = NpcManager.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	@Override
	public void awake() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameMessage(GameMessage gameMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDisconnect(String playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTick(String message) {
		// TODO Auto-generated method stub
		
	}

}
