
package plugins.core;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.Plugin;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.process.ExternalProcess;
import io.gamemachine.process.ProcessManager;

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
		List<? extends Config> values = config.getConfigList("unity_instances");
		for (Config value : values) {
			
			ExternalProcess info = new ExternalProcess(value.getString("start_script"),value.getString("executable"));
			boolean enabled = value.getBoolean("enabled");
			
			if (enabled) {
				ProcessManager.add(info);
			} else {
				logger.debug(info.executable+" not enabled, skipping");
			}
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
