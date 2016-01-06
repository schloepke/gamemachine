

using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using UnityEngine;
using io.gamemachine.messages;

#if UNITY_EDITOR
using UnityEditor;
#endif

namespace GameMachine {
    public class UnityObjectHelper {

        private static Dictionary<UnityObject, UnityEngine.Object> localObjects = new Dictionary<UnityObject, UnityEngine.Object>();

        public static UnityEngine.Object GetLocalObject(UnityObject id) {
            if (localObjects.ContainsKey(id)) {
                return localObjects[id];
            } else {
                return null;
            }
            
        }

        public static void SetLocalObject(UnityObject unityObject, UnityEngine.Object localObject) {
            localObjects[unityObject] = localObject;
        }

        public static Dictionary<string, UnityEngine.Object> assets = new Dictionary<string, UnityEngine.Object>();

        private static String GetFullPathWithoutExtension(String path) {
            return System.IO.Path.GetDirectoryName(path) + "/" + System.IO.Path.GetFileNameWithoutExtension(path);
        }

        public static UnityEngine.Object GetAsset(UnityObject unityObject, Type type) {
            if (!assets.ContainsKey(unityObject.uuid)) {
                if (string.IsNullOrEmpty(unityObject.path)) {
                    return null;
                }
                string resourcePath = Regex.Replace(unityObject.path, @".*Resources\/", "");
                resourcePath = GetFullPathWithoutExtension(resourcePath);
                assets[unityObject.uuid] = Resources.Load(resourcePath, type);
            }
            return assets[unityObject.uuid];
        }

#if UNITY_EDITOR

        public static UnityObject Load(UnityObject unityObject, Type type) {
            if (unityObject == null) {
                return new UnityObject();
            }
            if (!string.IsNullOrEmpty(unityObject.uuid) && GetLocalObject(unityObject) == null) {
                SetLocalObject(unityObject,AssetDatabase.LoadAssetAtPath(unityObject.path, type));
                return unityObject;
            } else {
                return unityObject;
            }
        }

        public static UnityObject ShowInEditor(string label, UnityObject unityObject, Type type) {
            if (unityObject == null) {
                unityObject = new UnityObject();
            }
            unityObject = Load(unityObject, type);
            SetLocalObject(unityObject,EditorGUILayout.ObjectField(label, GetLocalObject(unityObject), type, false));
            unityObject = Save(unityObject);

            return unityObject;
        }

        public static UnityObject Save(UnityObject unityObject) {
            if (GetLocalObject(unityObject) == null) {
                unityObject.path = null;
                unityObject.uuid = null;
            } else {
                unityObject.path = AssetDatabase.GetAssetPath(GetLocalObject(unityObject));
                unityObject.uuid = AssetDatabase.AssetPathToGUID(unityObject.path);
            }
            return unityObject;
        }

#endif
    }
}
