using UnityEditor;
using GameMachine.Common;
using System;
using System.Collections.Generic;
using UnityEngine;
using io.gamemachine.messages;

namespace GameMachine {
    [CustomEditor(typeof(NetworkFieldsDb))]
    public class NetworkFieldsEditor : Editor {

        private SerializedProperty prop;
        private SerializedObject so;
        private NetworkFieldsDb db;
        private NetworkFields def;
        private int userCodeIndex;

        void OnEnable() {

            so = new SerializedObject(target);
            db = (NetworkFieldsDb)target;
            def = db.GetData();
            EnsureFields();
        }

        private void EnsureFields() {
            foreach (NetworkFields.GmField customField in Enum.GetValues(typeof(NetworkFields.GmField))) {
                if (!def.fieldMap.ContainsKey(customField)) {
                    def.fieldMap[customField] = new NetworkFields.FieldSet();
                    def.fieldMap[customField].fieldType = NetworkFields.FieldType.None;
                    def.fieldMap[customField].gmField = customField;
                }
            }
            db.Save(def);
        }

        public override void OnInspectorGUI() {
            so.Update();

            //db.Clear();
            def = db.GetData();
            EditorMode();
            so.ApplyModifiedProperties();

            db.Save(def);
            EditorUtility.SetDirty(db);
        }

        void RunMode() {
            DrawDefaultInspector();
        }

        void EditorMode() {
            EditorGUILayout.Separator();
            EditorGUILayout.LabelField("Fixed fields", EditorStyles.boldLabel);
            foreach (NetworkFields.GmField gmField in def.FixedFields()) {
                NetworkFields.FieldSet fieldSet = def.fieldMap[gmField];
                fieldSet.fixedField = EditorGUILayout.Toggle(gmField.ToString(), fieldSet.fixedField);
                fieldSet.keyCode = def.FieldToKey(gmField);
                if (fieldSet.fixedField) {
                    fieldSet.active = true;
                } else {
                    fieldSet.active = false;
                }
            }

            EditorGUILayout.Separator();
            EditorGUILayout.LabelField("Optional fields", EditorStyles.boldLabel);
            EditorGUILayout.Separator();
            string[] keycodes = def.KeycodeNames().ToArray();

            foreach (NetworkFields.GmField gmField in def.UserFields()) {
                NetworkFields.FieldSet fieldSet = def.fieldMap[gmField];
                fieldSet.fixedField = false;

                fieldSet.fieldType = (NetworkFields.FieldType)EditorGUILayout.Popup("Value type", (int)fieldSet.fieldType, Enum.GetNames(typeof(NetworkFields.FieldType)));

                if (fieldSet.fieldType == NetworkFields.FieldType.None) {
                    fieldSet.keyCode = GMKeyCode.None;
                    fieldSet.broadcast = false;
                    fieldSet.active = false;
                    fieldSet.userCode = 0;
                } else {
                    fieldSet.active = true;
                    fieldSet.userCode = EditorGUILayout.Popup("Key", fieldSet.userCode, keycodes);
                   
                    fieldSet.keyCode = (GMKeyCode)Enum.Parse(typeof(GMKeyCode), keycodes[fieldSet.userCode]);
                    if (fieldSet.fieldType == NetworkFields.FieldType.BroadcastBool) {
                        fieldSet.broadcast = true;
                    } else if (fieldSet.fieldType == NetworkFields.FieldType.BroadcastFloat) {
                        fieldSet.broadcast = true;
                    } else if (fieldSet.fieldType == NetworkFields.FieldType.BroadcastInt) {
                        fieldSet.broadcast = true;
                    } else {
                        fieldSet.broadcast = false;
                    }
                }

                EditorGUILayout.Separator();
            }

            EditorGUILayout.Separator();
            if (GUILayout.Button("Debug", GUILayout.Width(300))) {
                foreach (NetworkFields.FieldSet set in def.fieldMap.Values) {
                    Debug.Log("KeyCode " + set.keyCode.ToString() + " -> " + set.gmField.ToString());
                }
                Debug.Log(db.ToString());
            }
        }

    }
}
