using UnityEngine;
using System.Collections;
using UnityEngine.EventSystems;

namespace GameMachine {
    namespace Common {
        public class InputState {

            public static bool UIActive() {
                return (EventSystem.current.IsPointerOverGameObject());
            }

            public static bool MovementDisabled() {
                return typing;
            }

            public static bool CameraDisabled() {
                if (chatdragging || (chatfocus && Input.GetMouseButton(0))) {
                    return true;
                } else {
                    return false;
                }
            }

            public static bool KeyInputDisabled() {
                return typing;
            }

            public static bool chatdragging = false;
            public static bool chatfocus = false;
            public static bool typing = false;
        }
    }
}
