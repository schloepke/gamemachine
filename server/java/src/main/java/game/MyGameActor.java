package game;

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

  private void onPlayerDisconnect(String playerId) {

  }

}