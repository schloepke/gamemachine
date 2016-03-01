package io.gamemachine.unity.unity_engine;

import io.gamemachine.messages.GmQuaternion;

/**
 * Created by chris on 2/28/2016.
 */
public class Quaternion {
    public float x = 0;
    public float y = 0;
    public float z = 0;
    public float w = 0;

    public static Quaternion identity = new Quaternion();

    public static Quaternion fromGmQuaternion(GmQuaternion gmq) {
        Quaternion q = new Quaternion();
        q.x = gmq.x;
        q.y = gmq.y;
        q.z = gmq.z;
        q.w = gmq.w;
        return q;
    }

    public GmQuaternion toGmQuaternion() {
        GmQuaternion q = new GmQuaternion();
        q.x = x;
        q.y = y;
        q.z = z;
        q.w = w;
        return q;
    }
}
