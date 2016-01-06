using UnityEngine;
using System.Collections;
using UnityEngine.EventSystems;
using System.Collections.Generic;

namespace GameMachine {
    namespace Common {
        public class InputState {

            public static HashSet<string> activeInputs = new HashSet<string>();

            public static bool dragging = false;
            public static bool chatfocus = false;
            public static bool cameraDisabled = false;
            
            public static bool CameraDisabled() {
                bool isDead = false;
                if (GamePlayer.IsNetworked()) {
                    isDead = GamePlayer.Instance().gameEntity.IsDead();
                }
                
                if (cameraDisabled || dragging || isDead || (chatfocus && Input.GetMouseButton(0))) {
                    return true;
                } else {
                    return false;
                }
            }

            public static bool KeyInputActive() {
                return activeInputs.Count > 0;
            }
            
            public static void SetInput(string key, bool active) {
                if (active) {
                    if (!activeInputs.Contains(key)) {
                        activeInputs.Add(key);
                    }
                } else {
                    if (activeInputs.Contains(key)) {
                        activeInputs.Remove(key);
                    }
                }
            }
            
        }
    }
}
