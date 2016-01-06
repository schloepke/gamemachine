package plugins;

import io.gamemachine.chat.ChatSubscriptions;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.guild.GuildService;
import io.gamemachine.routing.GameMessageRoute;
import plugins.pvp_game.TimeHandler;

public class PvpGamePlugin extends Plugin {

	@Override
	public void start() {
			
		
		GameMessageRoute.add(GuildService.name,GuildService.name,false);
		
		
		ActorUtil.createActor(TimeHandler.class, TimeHandler.name);
		ActorUtil.createActor(GuildService.class, GuildService.name);
		ActorUtil.createActor(ChatSubscriptions.class, ChatSubscriptions.name);

	}

}
