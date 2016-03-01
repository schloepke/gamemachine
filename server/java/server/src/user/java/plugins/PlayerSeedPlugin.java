package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.core.PlayerSeed;

public class PlayerSeedPlugin extends Plugin {

    @Override
    public void start() {
        GameMessageRoute.add(PlayerSeed.name, PlayerSeed.name, false);
        ActorUtil.createActor(PlayerSeed.class, PlayerSeed.name);
    }
}