package io.gamemachine.unity.unity_engine.engine_results;

import io.gamemachine.unity.unity_engine.unity_types.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2/28/2016.
 */
public class RaycastResult  implements UnityEngineResult  {
    public boolean hit;
    public List<GameObject> gameObjects = new ArrayList<GameObject>();
}
