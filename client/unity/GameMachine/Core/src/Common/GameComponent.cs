using UnityEngine;
using System.Collections;

namespace GameMachine {
    namespace Common {
        public class GameComponent {

            public static T Get<T>() {
                GameObject container = GameObject.Find(typeof(T).Name);
                return container.GetComponent<T>();
            }

            public static Component Get<T>(string name) {
                GameObject go = GameObject.Find(name);
                if (go == null) {
                    throw new System.NullReferenceException("Unable to load GameObject "+name);
                }
                return go.GetComponent(typeof(T));
            }

        }
    }
}
