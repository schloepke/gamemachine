using UnityEngine;
using System.Collections;
using GameMachine.Common;

namespace GameMachine {
    namespace ServerClient {
        public class ClientMonitor : MonoBehaviour {

            // Use this for initialization
            void Start() {
                InvokeRepeating("CheckClient", 10f, 10.0F);
            }


            void CheckClient() {
                GameObject client = GameObject.Find("ServerClient");
                if (client == null) {
                    GameObject go = GameComponent.Get<AssetLibrary>().Load("ServerClient");
                    go.name = "ServerClient";
                }
            }
        }
    }
}
