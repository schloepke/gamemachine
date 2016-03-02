package io.gamemachine.unity.unity_engine;

import io.gamemachine.messages.NpcGroupDef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/2/2016.
 */
public class UnityConfigResult  implements UnityEngineResult {
    public List<NpcGroupDef> npcGroupDefs = new ArrayList<NpcGroupDef>();
}
