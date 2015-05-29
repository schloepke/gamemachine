using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

namespace GameMachine {
    namespace Common {
        public class AssetLibrary : MonoBehaviour {

            [Serializable]
            public class LibraryAsset {
                //[HideInInspector]
                public string name;
                public GameObject prefab;
            }

            public LibraryAsset[] assets;

            private Dictionary<string, GameObject> prefabs = new Dictionary<string, GameObject>();
            
            private void EnsureIndex() {
                if (prefabs.Count == 0) {
                    foreach (LibraryAsset asset in assets) {
                        prefabs[asset.name] = asset.prefab;
                    }
                } 
            }
            public GameObject Load(string name) {
                EnsureIndex();
                return GameObject.Instantiate(prefabs[name]);
            }
        }
    }
}
