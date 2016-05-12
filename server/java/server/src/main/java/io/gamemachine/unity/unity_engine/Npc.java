package io.gamemachine.unity.unity_engine;

import com.google.common.base.Strings;
import io.gamemachine.messages.Factions;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;
import plugins.npc.path.WaypointRoute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chris on 3/3/2016.
 */
public class Npc {
    public enum Action {
        Waypoint,
        Attack,
        Guard,
        Leash
    }

    public NpcGroup group;
    public String id;
    public String leader;
    public Action action;
    public Vector3 position;
    public Vector3 target;
    public WaypointRoute route;
    public Factions.Faction faction;
    public String ownerId;

    public boolean isLeader() {
        return id.equals(leader);
    }

    public boolean hasLeader() {
        return !Strings.isNullOrEmpty(leader) && !id.equals(leader);
    }

    public boolean hasRoute() {
        return route != null;
    }

    public String getActorName() {
        return id.replace(" ", "_");
    }
}
