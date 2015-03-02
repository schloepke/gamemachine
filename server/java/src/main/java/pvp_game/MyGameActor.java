package pvp_game;

import io.gamemachine.core.Commands;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyGameActor extends GameMessageActor {
  public static String name = "my_game_actor";
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  
  public void awake() {
    Commands.clientManagerRegister(name);
  }

  public void onGameMessage(GameMessage gameMessage) {

  }

  public void onPlayerDisconnect(String playerId) {

  }

@Override
public void onTick(String message) {
	// TODO Auto-generated method stub
	
}

}