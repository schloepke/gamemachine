package io.gamemachine.unity.unity_engine.unity_types;

import io.gamemachine.messages.GmTransform;

/**
 * Created by chris on 2/28/2016.
 */
public class Transform {
    public Vector3 position;
    public Quaternion rotation;
    public String name;

    public static Transform fromGmTransform(GmTransform gmt) {
        Transform transform = new Transform();
        transform.position = Vector3.fromGmVector3(gmt.position);
        transform.rotation = Quaternion.fromGmQuaternion(gmt.rotation);
        transform.name = gmt.name;
        return transform;
    }
}
