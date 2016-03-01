package io.gamemachine.unity.unity_engine;

import io.gamemachine.messages.SyncObject;

/**
 * Created by chris on 2/28/2016.
 */
public interface UnityEngineHandler {
    void onEngineResult(UnityEngineResult result);
    void componentUpdated(Object object);
    void componentRemoved(String objectId);
    void componentAdded(Object object);

}
