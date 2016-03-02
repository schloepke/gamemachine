package io.gamemachine.unity.unity_engine;

import io.gamemachine.messages.PathResponse;
import io.gamemachine.unity.unity_engine.unity_types.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 3/1/2016.
 */
public class PathResult implements UnityEngineResult  {
    public List<Vector3> path = new ArrayList<Vector3>();
    public PathResponse.Status status;
}
