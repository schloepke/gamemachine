package io.gamemachine.unity.unity_engine;

import io.gamemachine.unity.unity_engine.engine_results.UnityEngineResult;

/**
 * Created by chris on 2/28/2016.
 */
public interface UnityEngineHandler {
    void onEngineResult(UnityEngineResult result);
    void componentUpdated(Object object);
    void componentRemoved(String objectId);
    void componentAdded(Object object);

}
