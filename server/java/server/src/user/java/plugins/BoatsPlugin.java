package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.core.BoatManager;

public class BoatsPlugin extends Plugin {

    @Override
    public void start() {
        // TODO Auto-generated method stub
        GameMessageRoute.add(BoatManager.name, BoatManager.name, false);
        ActorUtil.createActor(BoatManager.class, BoatManager.name);
    }
}
