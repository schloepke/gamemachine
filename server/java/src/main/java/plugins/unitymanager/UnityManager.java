
package plugins.unitymanager;

import io.gamemachine.core.ExternalProcess;
import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Plugin;
import io.gamemachine.messages.GameMessage;

import java.util.List;

import akka.event.Logging;
import akka.event.LoggingAdapter;

import com.typesafe.config.Config;

public class UnityManager extends GameMessageActor {

	public static String name = UnityManager.class.getSimpleName();
	LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
	
	@Override
	public void awake() {
		Config config = Plugin.getConfig(UnityManager.class);
		List<String> commands = config.getStringList("commands");
		for (String command : commands) {
			ExternalProcess.start(command);
		}
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
