package plugins;

import io.gamemachine.chat.ChatSubscriptions;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import plugins.pvp_game.TimeHandler;

public class PvpGamePlugin extends Plugin {

    @Override
    public void start() {

        ActorUtil.createActor(TimeHandler.class, TimeHandler.name);
        ActorUtil.createActor(ChatSubscriptions.class, ChatSubscriptions.name);

    }

}
