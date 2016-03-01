package io.gamemachine.unity.unity_engine;

import io.gamemachine.messages.SphereCastResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2/28/2016.
 */
public class SphereCastResult implements UnityEngineResult {
    public List<GameObject> gameObjects = new ArrayList<GameObject>();
}
