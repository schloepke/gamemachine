using UnityEngine;
using System.IO;
using ProtoBuf;
using System;
using GameMachine.Common;
using Newtonsoft.Json;

namespace GameMachine {
    public class ScriptableJson<T> : ScriptableObject {

        private T data;

        [HideInInspector]
        public string json;
        private bool loaded = false;

        public T GetData() {
            if (!loaded) {
                Load();
            }
            return data;
        }


        public void Clear() {
            loaded = false;
            json = null;
        }

        public void Export() {
            Load();
            string path = Settings.GetServerPath() + "/db/" + this.GetType().Name + ".json";
            if (Directory.Exists(Settings.GetServerPath())) {
                File.Delete(path);
                File.WriteAllText(path, json);
                Debug.Log("Saved " + path);
            } else {
                Debug.Log("Path does not exist " + Settings.GetServerPath());
            }
        }

        public void Save(T data) {
            this.data = data;
            json = JsonConvert.SerializeObject(data, Formatting.Indented);
            loaded = false;
        }

        public T Clone() {
            string str = JsonConvert.SerializeObject(data, Formatting.Indented);
            return JsonConvert.DeserializeObject<T>(str);
        }

        public override string ToString() {
            return json;
        }

        private void Load() {
            if (!loaded) {
                if (string.IsNullOrEmpty(json)) {
                    data = Activator.CreateInstance<T>();
                    Debug.Log("Backing file doesn't exist, returning new instance");
                } else {
                    data = JsonConvert.DeserializeObject<T>(json);
                }
            }
        }
    }
}
