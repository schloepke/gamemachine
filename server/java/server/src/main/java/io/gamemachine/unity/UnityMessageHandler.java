package io.gamemachine.unity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.gamemachine.core.SystemActors;
import io.gamemachine.messages.UnityEngineResponse;
import io.gamemachine.messages.UnityRegionUpdate;
import io.gamemachine.net.Connection;
import io.gamemachine.unity.unity_engine.UnityEngine;
import io.gamemachine.unity.unity_engine.RegionData;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import io.gamemachine.core.ActorUtil;
import io.gamemachine.messages.UnityMessage;
import plugins.npc.NpcManager;


public class UnityMessageHandler extends UntypedActor {


    public static String name = UnityMessageHandler.class.getSimpleName();
    private static final Logger logger = LoggerFactory.getLogger(UnityMessageHandler.class);

    private static Map<Integer,UnityEngine> engineRequests = new ConcurrentHashMap<Integer,UnityEngine>();


    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof UnityMessage) {
            UnityMessage unityMessage = (UnityMessage) message;
            if (unityMessage.syncObjects != null) {
                SystemActors.unitySyncActor.tell(unityMessage.syncObjects, null);
            } else if (unityMessage.unityEngineResponse != null) {
                HandleUnityEngineResponse(unityMessage.unityEngineResponse);
            } else if (unityMessage.unityRegionUpdate != null) {

                updateRegionData(unityMessage.unityRegionUpdate);
            } else if (unityMessage.groupSpawnRequest != null) {
                NpcManager.spawn(unityMessage.groupSpawnRequest);
            }
        }
    }

    public static void addEngineRequest(int id, UnityEngine unityEngine) {
        engineRequests.put(id,unityEngine);
    }


    private void updateRegionData(UnityRegionUpdate update) {
        RegionData data;
        if (!RegionData.regions.containsKey(update.region)) {
            data = new RegionData();
            data.region = update.region;
            data.status = RegionData.Status.Inactive;
        } else {
            data = RegionData.regions.get(update.region);
        }

        data.playerId = update.playerId;
        data.position = Vector3.fromGmVector3(update.position);
        data.size = update.size;
        data.lastUpdate = System.currentTimeMillis();

        RegionData.regions.put(data.region,data);
    }


    public static Connection getConnection(String region) {
        if (RegionData.regions.containsKey(region)) {
            return Connection.getConnection(RegionData.regions.get(region).playerId);
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
        }
    }

}
