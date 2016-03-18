package plugins;

import io.gamemachine.core.ActorUtil;
import io.gamemachine.core.Plugin;
import io.gamemachine.routing.GameMessageRoute;
import plugins.npc.NpcManager;

public class NpcPlugin extends Plugin {

    @Override
    public void start() {
        GameMessageRoute.add(NpcManager.name, NpcManager.name, false);
        ActorUtil.createActor(NpcManager.class, NpcManager.name);
    }

}
