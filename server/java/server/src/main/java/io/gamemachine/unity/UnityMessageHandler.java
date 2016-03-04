package io.gamemachine.unity;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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


public class UnityMessageHandler extends UntypedActor {

    public static Map<String,RegionData> regions = new ConcurrentHashMap<String,RegionData>();
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
            } else if (unityMessage.unityRegionUpdate != null) {
                updateRegionData(unityMessage.unityRegionUpdate);
            }
        }
    }

    public static void addEngineRequest(int id, UnityEngine unityEngine) {
        engineRequests.put(id,unityEngine);
    }

    public static boolean isAlive(String region) {
        if (!regions.containsKey(region)) {
            return false;
        }

        RegionData data = regions.get(region);
        if (System.currentTimeMillis() - data.lastUpdate > 20L) {
            return false;
        } else {
            return true;
        }
    }

    public static void setRegionStatus(String region, RegionData.Status status) {
        regions.get(region).status = status;
    }

    public static Collection<RegionData> getRegions() {
        return regions.values();
    }

    public static RegionData getRegionData(String region) {
        if (regions.containsKey(region)) {
            return regions.get(region);
        } else {
            return null;
        }
    }

    private void updateRegionData(UnityRegionUpdate update) {
        RegionData data;
        if (!regions.containsKey(update.region)) {
            data = new RegionData();
        } else {
            data = regions.get(update.region);
        }

        data.playerId = update.playerId;
        data.region = update.region;
        data.position = Vector3.fromGmVector3(update.position);
        data.size = update.size;
        data.lastUpdate = System.currentTimeMillis();
        regions.put(data.region,data);
    }

    public static Connection getConnection(String region) {
        if (regions.containsKey(region)) {
            return Connection.getConnection(regions.get(region).playerId);
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
