package plugins;

import io.gamemachine.core.GameEntityManagerService;
import io.gamemachine.core.Plugin;
import plugins.combat.SimpleGameEntityManager;

public class EntityManagerPlugin extends Plugin {

    @Override
    public void start() {
        SimpleGameEntityManager characterManager = new SimpleGameEntityManager();
        GameEntityManagerService.instance().setGameEntityManager(characterManager);

    }

}
