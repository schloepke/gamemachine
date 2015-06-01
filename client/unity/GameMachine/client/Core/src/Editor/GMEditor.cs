using UnityEngine;
using UnityEditor;
using System;
using System.IO;
using System.Collections;

namespace GameMachine {
    namespace Common {
        public class GMEditor : MonoBehaviour {

            private static string path = System.IO.Path.GetFullPath(Application.dataPath) + "/GameMachine";

            [MenuItem("Game Machine/Create Game Entity")]
            static void CreateGameEntity() {
                string text = File.ReadAllText("test.txt");
                text = text.Replace("some text", "new value");
                File.WriteAllText("test.txt", text);
                Debug.Log(System.IO.Path.GetFullPath(Application.dataPath));
            }

            static string TemplatesPath() {
                return System.IO.Path.GetFullPath(Application.dataPath);
            }

        }
    }
}
