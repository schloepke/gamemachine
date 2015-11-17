using UnityEngine;
using System.Collections;
using UnityEditor;
using System.Collections.Generic;
using Ionic.Zip;
using System.IO;
using System.Linq;

namespace GameMachine {
    
    public class GameMachineEditor : EditorWindow {

        private Editor serverConfigEditor;
        private ServerConfig serverConfig;

        [MenuItem("Window/GameMachine/Editor")]
        static void CreateWindow() {
            GameMachineEditor window = (GameMachineEditor)EditorWindow.GetWindow(typeof(GameMachineEditor));
            window.titleContent = new GUIContent("Game Machine");
        }

        void OnEnable() {
            serverConfig = Resources.Load<ServerConfig>("ServerConfig");
        }

        void OnGUI() {
            if (serverConfig != null) {
               if (serverConfigEditor == null) {
                    serverConfigEditor = Editor.CreateEditor(serverConfig);
                }
                serverConfigEditor.OnInspectorGUI();
            }
        }
        
    }
}
