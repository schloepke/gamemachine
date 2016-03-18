package plugins.npc;

import io.gamemachine.unity.unity_engine.unity_types.Vector3;
import plugins.core.combat.CombatTarget;

/**
 * Created by chris on 3/6/2016.
 */
public interface InpcCombatAi {
    CombatTarget update(Vector3 position);
}
