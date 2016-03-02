package io.gamemachine.unity.unity_old;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.UnityInstanceData;

public class TestInstance extends GameMessageActor {

    private static final Logger log = LoggerFactory.getLogger(TestInstance.class);
    private String name;
    private UnityInstanceData data;

    public TestInstance(String name) {
        this.name = name;
        data = new UnityInstanceData();
        data.role = "test";
        UnityInstanceManager.requestInstance(name, data);
        log.warn("test instance requested " + name);
    }

    @Override
    public void awake() {
        scheduleOnce(100l, "tick");
    }

    @Override
    public void onTick(String message) {
        UnityInstance instance = UnityInstanceManager.findOrCreateByName(name, data);
        if (instance != null && instance.running) {
            String actorName = "ExampleActor";

            for (int i = 0; i < 10; i++) {
                long startTime = System.nanoTime();

                GameMessage gameMessage = new GameMessage();
                GameMessage response = instance.ask(gameMessage, actorName, 20, 2);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime) / 1000000;

                if (response == null) {
                    log.warn("No response " + name + " " + duration);
                } else {
                    UnityInstanceTest.messageCount.getAndIncrement();
                }
            }
        }

        scheduleOnce(10l, "tick");
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {
        // TODO Auto-generated method stub

    }

}
