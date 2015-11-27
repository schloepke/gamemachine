package plugins;

import io.gamemachine.chat.ChatSubscriptions;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.pvp_game.GuildHandler;
import plugins.pvp_game.TimeHandler;

public class PvpGamePlugin extends Plugin {

	@Override
	public void start() {
			
		
		GameMessageRoute.add(GuildHandler.name,GuildHandler.name,false);
		
		
		ActorUtil.createActor(TimeHandler.class, TimeHandler.name);
		ActorUtil.createActor(GuildHandler.class, GuildHandler.name);
		ActorUtil.createActor(ChatSubscriptions.class, ChatSubscriptions.name);

	}

}
