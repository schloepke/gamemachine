package io.gamemachine.unity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.gamemachine.messages.UnityEngineResponse;
import io.gamemachine.messages.UnityInstanceUpdate;
import io.gamemachine.net.Connection;
import io.gamemachine.unity.unity_engine.UnityEngine;
import io.gamemachine.unity.unity_engine.UnityInstanceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.messages.UnityMessage;
import scala.concurrent.duration.Duration;


public class UnityMessageHandler extends UntypedActor {

    public static Map<String,UnityInstanceData> instances = new ConcurrentHashMap<String,UnityInstanceData>();
    public static String name = UnityMessageHandler.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(UnityMessageHandler.class);

    private ActorSelection unitySync = ActorUtil.getSelectionByName(UnitySync.name);
    private static Map<Integer,UnityEngine> engineRequests = new ConcurrentHashMap<Integer,UnityEngine>();


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof UnityMessage) {
            UnityMessage unityMessage = (UnityMessage) message;
            if (unityMessage.syncObjects != null) {
                unitySync.tell(unityMessage.syncObjects, null);
            } else if (unityMessage.unityEngineResponse != null) {
                HandleUnityEngineResponse(unityMessage.unityEngineResponse);
            } else if (unityMessage.unityInstanceUpdate != null) {
                updateUnityInstance(unityMessage.unityInstanceUpdate);
            }
        }
    }

    public static void addEngineRequest(int id, UnityEngine unityEngine) {
        engineRequests.put(id,unityEngine);
    }

    public static boolean isAlive(String region) {
        return instances.containsKey(region);
    }

    private void updateUnityInstance(UnityInstanceUpdate update) {
        UnityInstanceData data;
        if (instances.containsKey(update.region)) {
            data = instances.get(update.region);
            data.region = update.region;
            data.lastUpdate = System.currentTimeMillis();
        } else {
            data = new UnityInstanceData();
            data.playerId = update.playerId;
            data.region = update.region;
            data.lastUpdate = System.currentTimeMillis();
            instances.put(data.region,data);
        }
    }

    public static Connection getConnection(String region) {
        if (instances.containsKey(region)) {
            return Connection.getConnection(instances.get(region).playerId);
        }

        return null;
    }

    private void HandleUnityEngineResponse(UnityEngineResponse response) {
        UnityEngine engine;
        if (response.overlapSphereResponse != null) {
            engine = engineRequests.get(response.overlapSphereResponse.messageId);
            engine.overlapSphereResponse(response.overlapSphereResponse);
            engineRequests.remove(response.overlapSphereResponse.messageId);
        } else if (response.sphereCastResponse != null) {
            engine = engineRequests.get(response.sphereCastResponse.messageId);
            engine.sphereCastResponse(response.sphereCastResponse);
            engineRequests.remove(response.sphereCastResponse.messageId);
        } else if (response.raycastResponse != null) {
            engine = engineRequests.get(response.raycastResponse.messageId);
            engine.raycastResponse(response.raycastResponse);
            engineRequests.remove(response.raycastResponse.messageId);
        } else if (response.instantiateResponse != null) {
            engine = engineRequests.get(response.instantiateResponse.messageId);
            engine.instantiateResponse(response.instantiateResponse);
            engineRequests.remove(response.instantiateResponse.messageId);
        } else if (response.destroyResponse != null) {
            engine = engineRequests.get(response.destroyResponse.messageId);
            engine.destroyResponse(response.destroyResponse);
            engineRequests.remove(response.destroyResponse.messageId);
        } else if (response.pathResponse != null) {
            engine = engineRequests.get(response.pathResponse.messageId);
            engine.pathResponse(response.pathResponse);
            engineRequests.remove(response.pathResponse.messageId);
        } else if (response.unityConfigResponse != null) {
            engine = engineRequests.get(response.unityConfigResponse.messageId);
            engine.unityConfigResponse(response.unityConfigResponse);
            engineRequests.remove(response.unityConfigResponse.messageId);
        }
    }

}
