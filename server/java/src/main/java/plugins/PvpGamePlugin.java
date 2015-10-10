package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.ChatSubscriptions;
import io.gamemachine.core.GridExpiration;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.pvp_game.GuildHandler;
import plugins.pvp_game.HarvestHandler;
import plugins.pvp_game.TimeHandler;

public class PvpGamePlugin extends Plugin {

	@Override
	public void start() {
		
		GameMessageRoute.add(HarvestHandler.name,HarvestHandler.name,false);
		
		
		GameMessageRoute.add(GuildHandler.name,GuildHandler.name,false);
		
		ActorUtil.createActor(HarvestHandler.class, HarvestHandler.name);
		
		
		
		ActorUtil.createActor(TimeHandler.class, TimeHandler.name);
		ActorUtil.createActor(GuildHandler.class, GuildHandler.name);
		ActorUtil.createActor(ChatSubscriptions.class, ChatSubscriptions.name);

	}

}
