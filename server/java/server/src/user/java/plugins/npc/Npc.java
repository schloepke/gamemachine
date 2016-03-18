package plugins.npc;

import akka.actor.ActorRef;
import com.google.common.base.Strings;
import io.gamemachine.messages.Character;
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

    public static Map<String,Npc> npcs = new ConcurrentHashMap<>();

    public NpcGroup group;
    public String id;
    public String leader;
    public Action action;
    public Vector3 position;
    public Vector3 target;
    public WaypointRoute route;
    Factions.Faction faction;

    public boolean isLeader() {
        return id.equals(leader);
    }

    public boolean hasLeader() {
        return !Strings.isNullOrEmpty(leader) && !id.equals(leader);
    }

    public Npc getLeader() {
        return npcs.get(leader);
    }

    public boolean hasRoute() {
        return route != null;
    }

    public static void setPosition(String id, Vector3 position) {
        npcs.get(id).position = position;
    }

    public static Vector3 getPosition(String id) {
        return npcs.get(id).position;
    }

    public static void setTarget(String id, Vector3 position) {
        npcs.get(id).target = position;
    }

    public String getActorName() {
        return id.replace(" ", "_");
    }
}
