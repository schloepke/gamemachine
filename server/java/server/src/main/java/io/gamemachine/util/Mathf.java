package io.gamemachine.util;

import io.gamemachine.unity.unity_engine.unity_types.Vector3;

import java.util.Random;

/**
 * Created by chris on 3/3/2016.
 */
public class Mathf {

    private static Random rand = new Random();

    public static int toInt(double num) {
        return (int) Math.round(num * 100l);
    }

    public static double ToDouble(int i) {
        return i / 100D;
    }

    public static double randomRange(double min, double max) {
        return rand.nextInt((int) ((max - min) + 1)) + min;
    }

    public static Vector3 randomInRange(Vector3 min, Vector3 max) {
        Vector3 r = new Vector3();
        r.x = randomRange(min.x,max.x);
        if (min.y == 0d && max.y == 0d) {
            r.y = 0d;
        } else {
            r.y = randomRange(min.y,max.y);
        }
        r.z = randomRange(min.z,max.z);
        return r;
    }

    public static double lerp (double from, double to, double value) {
        if (value < 0.0f)
            return from;
        else if (value > 1.0f)
            return to;
        return (to - from) * value + from;
    }

    public static double lerpUnclamped (double from, double to, double value) {
        return (1.0d - value)*from + value*to;
    }

    public static double inverseLerp (double from, double to, double value) {
        if (from < to) {
            if (value < from)
                return 0.0f;
            else if (value > to)
                return 1.0f;
        }
        else {
            if (value < to)
                return 1.0f;
            else if (value > from)
                return 0.0f;
        }
        return (value - from) / (to - from);
    }

    public static double inverseLerpUnclamped (double from, double to, double value) {
        return (value - from) / (to - from);
    }

    public static double smoothStep (double from, double to, double value) {
        if (value < 0.0f)
            return from;
        else if (value > 1.0f)
            return to;
        value = value*value*(3.0f - 2.0f*value);
        return (1.0d - value)*from + value*to;
    }

    public static double smoothStepUnclamped (double from, double to, double value) {
        value = value*value*(3.0f - 2.0f*value);
        return (1.0d - value)*from + value*to;
    }


    public static double superLerp (double from, double to, double from2, double to2, double value) {
        if (from2 < to2) {
            if (value < from2)
                value = from2;
            else if (value > to2)
                value = to2;
        }
        else {
            if (value < to2)
                value = to2;
            else if (value > from2)
                value = from2;
        }
        return (to - from) * ((value - from2) / (to2 - from2)) + from;
    }

    public static Vector3 vector3Lerp (Vector3 v1, Vector3 v2, double value) {
        if (value > 1.0f)
            return v2;
        else if (value < 0.0f)
            return v1;
        return new Vector3 (v1.x + (v2.x - v1.x)*value,
                v1.y + (v2.y - v1.y)*value,
                v1.z + (v2.z - v1.z)*value );
    }
}
