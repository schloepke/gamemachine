package io.gamemachine.unity.unity_engine.unity_types;

import io.gamemachine.messages.GmGameObject;

/**
 * Created by chris on 2/28/2016.
 */
public class GameObject {
    public Transform transform;
    public String name;

    public static GameObject fromGmGameObject(GmGameObject gmGo) {
        GameObject go = new GameObject();
        Transform transform = Transform.fromGmTransform(gmGo.transform);
        go.name = gmGo.name;
        return go;
    }
}
