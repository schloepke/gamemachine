using UnityEngine;
using System.Collections;

namespace GameMachine {
    namespace Common {
        public class DoNotDestroy : MonoBehaviour {

            void Awake() {
                DontDestroyOnLoad(this.gameObject);
            }

        }
    }
}
