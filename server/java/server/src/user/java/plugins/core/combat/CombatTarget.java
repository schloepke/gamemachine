package plugins.core.combat;

import io.gamemachine.messages.GmTarget;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

/**
 * Created by chris on 3/6/2016.
 */
public class CombatTarget {

    public String id;
    public Vector3 position;

    public CombatTarget(String id, Vector3 position) {
        this.id = id;
        this.position = position;
    }
}
