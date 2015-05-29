using UnityEngine;
using System.Collections;

namespace GameMachine {
    namespace Common {
        public interface IGameEntityInput {

            void SetControllerType(string type);
            bool GetBool(GMKeyCode code);
            float GetFloat(GMKeyCode code);
            int GetInt(GMKeyCode code);
            string GetString(GMKeyCode code);
            Vector3 GetVector3(GMKeyCode code);
            Quaternion GetQuaternion(GMKeyCode code);
        }
    }
}
