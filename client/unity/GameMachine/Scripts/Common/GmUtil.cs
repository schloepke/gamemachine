using UnityEngine;
using System;
using TrackData = io.gamemachine.messages.TrackData;
using UserDefinedData = io.gamemachine.messages.UserDefinedData;
using System.IO;

namespace GameMachine {
    namespace Common {
        public sealed class GmUtil {

            public static Vector3 Infinity = new Vector3(float.PositiveInfinity, float.PositiveInfinity, float.PositiveInfinity);

            static readonly GmUtil _instance = new GmUtil();
            public static GmUtil Instance {
                get {
                    return _instance;
                }
            }

            private Settings settings;

            GmUtil() {
                settings = Settings.Instance();
            }

            public static bool IsWindows() {
               return Path.DirectorySeparatorChar == '\\';
            }

            public static float Distance2d(Vector3 start, Vector3 end) {
                Vector3 start2d = new Vector3(start.x,0f,start.z);
                Vector3 end2d = new Vector3(end.x,0f,end.z);
                return Vector3.Distance(start2d, end2d);
            }

            public static string Base64Encode(string plainText) {
                var plainTextBytes = System.Text.Encoding.UTF8.GetBytes(plainText);
                return System.Convert.ToBase64String(plainTextBytes);
            }

            public static string Base64Decode(string base64EncodedData) {
                var base64EncodedBytes = System.Convert.FromBase64String(base64EncodedData);
                return System.Text.Encoding.UTF8.GetString(base64EncodedBytes);
            }

            public float IntToFloat(int i) {
                return i / 100f;
            }

            public int FloatToInt(float i) {
                return (int)(Math.Round(i * 100, 2));
            }

            public UserDefinedData Vector3ToUserDefinedData(Vector3 vec, UserDefinedData userdef) {
                userdef.x = FloatToInt(vec.x);
                userdef.y = FloatToInt(vec.y);
                userdef.z = FloatToInt(vec.z);
                return userdef;
            }

            public UserDefinedData QuaternionToUserDefinedData(Quaternion q, UserDefinedData userdef) {
                userdef.rx = FloatToInt(q.x);
                userdef.ry = FloatToInt(q.y);
                userdef.rz = FloatToInt(q.z);
                userdef.rw = FloatToInt(q.w);
                return userdef;
            }

            public TrackData Vector3ToTrackData(Vector3 vec, TrackData trackData) {
                trackData.x = FloatToInt(vec.x);
                trackData.y = FloatToInt(vec.y);
                trackData.z = FloatToInt(vec.z);
                return trackData;
            }

            public TrackData QuaternionToTrackData(Quaternion q, TrackData trackData) {
                trackData.rx = FloatToInt(q.x);
                trackData.ry = FloatToInt(q.y);
                trackData.rz = FloatToInt(q.z);
                trackData.rw = FloatToInt(q.w);
                return trackData;
            }

            public Quaternion TrackdataToQuaternion(TrackData trackData) {

                float x = (trackData.rx / 100f);
                float y = (trackData.ry / 100f);
                float z = (trackData.rz / 100f);
                float w = (trackData.rw / 100f);

                return new Quaternion(x, y, z,w);
            }

            public Vector3 TrackdataToVector3(TrackData trackData) {

                float x = (trackData.x / 100f);
                float y = (trackData.y / 100f);
                float z = (trackData.z / 100f);

                return new Vector3(x, y, z);
            }

            public Vector3 TrackdataDeltaToVector3(TrackData trackData, Vector3 current) {
                float x = current.x + (float)(trackData.ix / 100f);
                float z = current.z + (float)(trackData.iz / 100f);
                float y = current.y + (float)(trackData.iy / 100f);

                return new Vector3(x, y, z);
            }

            public Vector3 UserDefinedDataToVector3(UserDefinedData userdef) {

                float x = (userdef.x / 100f);
                float z = (userdef.z / 100f);
                float y = (userdef.y / 100f);

                return new Vector3(x, y, z);
            }

            public Quaternion UserDefinedDataToQuaternion(UserDefinedData userdef) {

                float x = (userdef.rx / 100f);
                float y = (userdef.ry / 100f);
                float z = (userdef.rz / 100f);
                float w = (userdef.rw / 100f);
                return new Quaternion(x, y, z, w);
            }
        }
    }
}
