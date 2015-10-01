using UnityEngine;
using UnityEditor;
using System.Collections;
using System.Collections.Generic;
using TrackData = io.gamemachine.messages.TrackData;

namespace GameMachine {
    namespace Common {
        [CustomEditor(typeof(NetworkFields))]
        public class GameEntityEditor : Editor {

            private SerializedObject so;
            private NetworkFields myTarget;

            void OnEnable() {

                myTarget = (NetworkFields)target;
                so = new SerializedObject(target);
            }

            public override void OnInspectorGUI() {
                so.Update();

                if (myTarget.running) {
                    RunMode();
                } else {
                    EditorMode();
                }

                so.ApplyModifiedProperties();
            }

            void RunMode() {
                DrawDefaultInspector();
            }

            void EditorMode() {
                EditorGUILayout.LabelField("Built in fields", EditorStyles.boldLabel);
                EditorGUILayout.Separator();
                foreach (string name in System.Enum.GetNames(typeof(NetworkFields.SynchronizedField))) {
                    if (name.StartsWith("Userdef")) {
                        continue;
                    }
                    string propName;
                    SerializedProperty prop;
                    string baseName = System.Char.ToLowerInvariant(name[0]) + name.Substring(1);

                    propName = baseName + "Enabled";
                    prop = so.FindProperty(propName);

                    if (prop != null) {
                        prop.boolValue = EditorGUILayout.Toggle(prop.displayName.Replace("Enabled", ""), prop.boolValue);
                    }


                }

                EditorGUILayout.Separator();
                EditorGUILayout.Separator();
                EditorGUILayout.LabelField("User defined fields", EditorStyles.boldLabel);
                EditorGUILayout.Separator();
                foreach (string name in System.Enum.GetNames(typeof(NetworkFields.SynchronizedField))) {

                    if (!name.StartsWith("Userdef")) {
                        continue;
                    }
                    string propName;
                    SerializedProperty prop;
                    string baseName = System.Char.ToLowerInvariant(name[0]) + name.Substring(1);

                    propName = baseName + "Enabled";
                    prop = so.FindProperty(propName);

                    if (prop != null) {
                        prop.boolValue = EditorGUILayout.Toggle(prop.displayName.Replace("Enabled", ""), prop.boolValue);
                    }

                    propName = baseName + "Type";
                    prop = so.FindProperty(propName);
                    //EditorGUILayout.LabelField(prop.displayName);


                    prop = so.FindProperty(propName);
                    if (prop != null) {
                        prop.enumValueIndex = EditorGUILayout.Popup("Type", prop.enumValueIndex, System.Enum.GetNames(typeof(NetworkFields.UserDefValue)));
                    }

                    string friendlyName = baseName + "Name";
                    prop = so.FindProperty(friendlyName);
                    if (prop != null) {
                        prop.stringValue = EditorGUILayout.TextField("Name", prop.stringValue);
                    }
                    EditorGUILayout.Separator();
                }
            }
        }
    }
}
