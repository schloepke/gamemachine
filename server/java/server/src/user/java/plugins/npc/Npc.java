package plugins.npc;

import akka.actor.ActorRef;
import io.gamemachine.messages.Character;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

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

    public Character character;
    public Action action;
    public Vector3 position;
    public Vector3 target;

}
