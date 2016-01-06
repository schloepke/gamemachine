using UnityEngine;
using GameMachine.Common;
using io.gamemachine.messages;

namespace GameMachine {
    public partial class BuildObjectHelper {
        
        public static void SetRotation(BuildObject buildObject, Quaternion rotation) {
            buildObject.rw = GmUtil.FloatToInt(rotation.w);
            buildObject.rx = GmUtil.FloatToInt(rotation.x);
            buildObject.ry = GmUtil.FloatToInt(rotation.y);
            buildObject.rz = GmUtil.FloatToInt(rotation.z);
        }

        public static Quaternion GetRotation(BuildObject buildObject) {
            Quaternion r = new Quaternion();
            r.w = GmUtil.IntToFloat(buildObject.rw);
            r.x = GmUtil.IntToFloat(buildObject.rx);
            r.y = GmUtil.IntToFloat(buildObject.ry);
            r.z = GmUtil.IntToFloat(buildObject.rz);
            return r;
        }

    }
}
