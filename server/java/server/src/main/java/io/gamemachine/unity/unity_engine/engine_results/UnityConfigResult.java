package io.gamemachine.unity.unity_engine.engine_results;


import io.gamemachine.messages.GmSpawn;
import io.gamemachine.messages.GmSpawnGroup;
import io.gamemachine.unity.unity_engine.engine_results.UnityEngineResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/2/2016.
 */
public class UnityConfigResult  implements UnityEngineResult {
    public List<GmSpawnGroup> spawnGroups = new ArrayList<>();
    public List<GmSpawn> spawns = new ArrayList<>();
}
