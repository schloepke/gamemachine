using UnityEngine;
using System.IO;
using ProtoBuf;
using System;
using GameMachine.Common;

namespace GameMachine {
    public class ScriptableProtobuf<T> : ScriptableObject where T : IExtensible {

        private T data;

        [HideInInspector]
        public byte[] bytes;
        private bool loaded = false;

        public T GetData() {
            if (!loaded) {
                Load();
            }
            return data;
        }
        

        public void Clear() {
            loaded = false;
            bytes = null;
        }

        public void Export() {
            Load();
            MemoryStream stream = new MemoryStream();
            Serializer.Serialize(stream, data);
            string path = Settings.GetServerPath() + "/db/" + this.GetType().Name+".proto";
            if (Directory.Exists(Settings.GetServerPath())) {
                File.Delete(path);
                File.WriteAllBytes(path, stream.ToArray());
                Debug.Log("Saved " + path);
            } else {
                Debug.Log("Path does not exist " + Settings.GetServerPath());
            }
        }

        public void Save(T data) {
            this.data = data;
            MemoryStream stream = new MemoryStream();
            Serializer.Serialize(stream, data);
            bytes = stream.ToArray();
            loaded = false;
        }

        public T Clone(T data) {
            MemoryStream stream = new MemoryStream();
            Serializer.Serialize(stream, data);

            stream = new MemoryStream(stream.ToArray());
            return Serializer.Deserialize<T>(stream);
        }

        public T Clone() {
            MemoryStream stream = new MemoryStream();
            Serializer.Serialize(stream, GetData());

            stream = new MemoryStream(stream.ToArray());
            return Serializer.Deserialize<T>(stream);
        }

        private void Load() {
            if (!loaded) {
                if (bytes == null) {
                    data = Activator.CreateInstance<T>();
                    Debug.Log("Backing file doesn't exist, returning new instance");
                } else {
                    MemoryStream stream = new MemoryStream(bytes);
                    data = Serializer.Deserialize<T>(stream);
                }
            }
        }
    }
}
