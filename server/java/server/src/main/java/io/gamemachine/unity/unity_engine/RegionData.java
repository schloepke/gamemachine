package io.gamemachine.unity.unity_engine;

import akka.actor.ActorRef;
import io.gamemachine.grid.Grid;
import io.gamemachine.grid.GridService;

import io.gamemachine.unity.unity_engine.unity_types.Vector3;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chris on 2/29/2016.
 */
public class RegionData {

    public static Map<String,RegionData> regions = new ConcurrentHashMap<String,RegionData>();

    public enum Status {
        None,
        Active,
        Inactive
    }

    public String playerId;
    public String region;
    public String zone;
    public long lastUpdate;
    public Vector3 position;
    public float size;
    public Status status;

    private Map<String, ActorRef> npcActors = new ConcurrentHashMap<>();
    private Map<String, Npc> npcs = new ConcurrentHashMap<>();

    static {

        addRegion("default","zone1",5000);
    }

    private static void addRegion(String region, String zone, int size) {
        RegionData data = new RegionData();
        data.region = region;
        data.zone = zone;
        data.size = size;
        data.status = Status.Inactive;
        regions.put(data.region,data);
    }

    public Collection<Npc> getNpcs() {
        return npcs.values();
    }

    public void addNpc(Npc npc) {
        npcs.put(npc.id,npc);
    }

    public Npc getNpc(String id) {
        return npcs.get(id);
    }

    public void clearNpcs() {
        npcs.clear();
    }

    public Collection<ActorRef> getNpcActors() {
        return npcActors.values();
    }

    public void addNpcActor(String id, ActorRef ref) {
        npcActors.put(id,ref);
    }

    public ActorRef getNpcActor(String id) {
        return npcActors.get(id);
    }

    public void clearNpcActors() {
        npcActors.clear();
    }

    public static Grid getPlayerGrid(String region) {
        RegionData data = regions.get(region);
        return GridService.getInstance().getGrid(data.zone,"default");
    }

    public static Grid getNpcGrid(String region) {
        return GridService.getInstance().getGrid(region,"default");
    }

    public static boolean isAlive(String region) {
        if (!regions.containsKey(region)) {
            return false;
        }

        RegionData data = regions.get(region);
        if (System.currentTimeMillis() - data.lastUpdate > 20000L) {
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

    public static RegionData getRegionByZone(String zone) {
        for (RegionData regionData : regions.values()) {
            if (regionData.zone.equals(zone)) {
                return regionData;
            }
        }
        return null;
    }

    public static RegionData getRegionData(String region) {
        if (RegionData.regions.containsKey(region)) {
            return RegionData.regions.get(region);
        } else {
            return null;
        }
    }


}
