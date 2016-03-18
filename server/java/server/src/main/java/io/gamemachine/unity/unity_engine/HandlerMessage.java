package io.gamemachine.unity.unity_engine;

/**
 * Created by chris on 3/17/2016.
 */
public class HandlerMessage {
    public enum Type {
        ComponentUpdate,
        ComponentRemove,
        ComponentAdd,
        EngineResult
    }

    public Type type;
    public Object message;

    public HandlerMessage(Type type) {
        this.type = type;
    }
}
