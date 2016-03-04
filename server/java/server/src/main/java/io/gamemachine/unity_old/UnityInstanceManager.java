
package io.gamemachine.unity_old;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.gamemachine.core.GameMessageActor;
import io.gamemachine.core.PlayerService;
import io.gamemachine.grid.GridService;
import io.gamemachine.messages.GameMessage;
import io.gamemachine.messages.Player;
import io.gamemachine.messages.UnityInstanceStatus;
import io.gamemachine.messages.UnityInstanceData;
import io.gamemachine.regions.ZoneService;

public class UnityInstanceManager extends GameMessageActor {

    public static long expireAt = 5000L;

    public static String name = UnityInstanceManager.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(UnityInstanceManager.class);

    private static Map<String, UnityInstance> instances = new ConcurrentHashMap<String, UnityInstance>();
    private static Map<Integer, String> zoneToName = new ConcurrentHashMap<Integer, String>();

    public static void releaseInstance(UnityInstance instance) {
        instance.status.requestedState = UnityInstanceStatus.State.None;
        instance.ask(instance.status);

        PlayerService.getInstance().releastUnityAgent(instance.status.playerId);
        GridService.getInstance().removeForZone(instance.status.zone.name);
        ZoneService.releaseZone(instance.status.name);
        instance.running = false;
        instance.assigned = false;
        zoneToName.remove(instance.status.zone.number);
        instances.remove(instance.status.name);
        logger.warn("Unity instance " + instance.status.name + " released");
    }

    public static UnityInstance findByZone(int zone) {
        if (zoneToName.containsKey(zone)) {
            String instanceName = zoneToName.get(zone);
            return findByName(instanceName);
        } else {
            return null;
        }
    }

    public static UnityInstance findByName(String instanceName) {
        if (instances.containsKey(instanceName)) {
            return instances.get(instanceName);
        } else {
            return null;
        }
    }

    public static UnityInstance findOrCreateByName(String instanceName, UnityInstanceData data) {
        if (instances.containsKey(instanceName)) {
            return instances.get(instanceName);
        } else {
            return requestInstance(instanceName, data);
        }
    }

    public static UnityInstance requestInstance(String instanceName, UnityInstanceData data) {
        if (instances.containsKey(instanceName)) {
            return null;
        }

        UnityInstanceStatus instanceStatus = new UnityInstanceStatus();
        UnityInstance instance = new UnityInstance(instanceStatus);
        instance.assigned = false;
        instance.running = false;
        instanceStatus.name = instanceName;
        instanceStatus.data = data;
        instances.put(instanceName, instance);
        return instance;
    }

    private boolean assignInstance(UnityInstance instance) {
        Player player = PlayerService.getInstance().assignUnityAgent();
        if (player == null) {
            logger.debug("Unable to assign player for unity instance " + instance.status.name);
            return false;
        }

        UnityInstanceStatus status = instance.status;

        status.playerId = player.id;
        status.requestedState = UnityInstanceStatus.State.Running;
        status.state = UnityInstanceStatus.State.None;
        status.zone = ZoneService.getZone(status.name);
        PlayerService.getInstance().setZone(status.playerId, status.zone);
        GridService.getInstance().createForZone(status.zone.name);

        zoneToName.put(status.zone.number, status.name);
        instance.assigned = true;
        return true;
    }

    private static boolean updateInstance(UnityInstance instance) {
        UnityInstanceStatus status = instance.status;
        UnityInstanceStatus response = instance.ask(status);

        if (response != null) {
            if (!instance.running && response.state == status.requestedState) {
                instance.running = true;
                logger.warn("Unity instance " + instance.status.name + " running");
                return true;
            }
            instance.lastContact = System.currentTimeMillis();
        } else {
            if (instance.running) {
                if (System.currentTimeMillis() - instance.lastContact > expireAt) {
                    logger.warn("Unity instance " + instance.status.name + " expired");
                    instance.release();
                }
            }

        }
        return false;
    }

    @Override
    public void awake() {
        scheduleOnce(500l, "tick");
    }

    @Override
    public void onTick(String message) {

        for (UnityInstance instance : instances.values()) {
            if (!instance.assigned) {
                assignInstance(instance);
            }

            if (instance.assigned) {
                updateInstance(instance);
            }
        }

        scheduleOnce(500l, "tick");
    }

    @Override
    public void onGameMessage(GameMessage gameMessage) {
        // Reliable messages handled here
        if (exactlyOnce(gameMessage)) {
        }
        // Sending a response to the player from an unreliable message
        //PlayerCommands.sendGameMessage(gameMessage, playerId);
    }

}
