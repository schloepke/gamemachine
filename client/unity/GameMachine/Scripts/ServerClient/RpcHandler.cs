using UnityEngine;
using System.Collections;
using System.IO;
using ProtoBuf;
using GameMachine.Core;
using RpcMessage = io.gamemachine.messages.RpcMessage;
using ClientMessage = io.gamemachine.messages.ClientMessage;

namespace GameMachine {
    namespace ServerClient {
        public class RpcHandler : MonoBehaviour {

            public static RpcHandler instance;

            void Awake() {
                instance = this;
            }

            void Update() {
                RpcMessage message;

                for (int i = 0; i < ClientMessageQueue.rpcQueue.Count; i++) {
                    if (ClientMessageQueue.rpcQueue.TryDequeue(out message)) {
                        Incoming(message);
                    }
                }
            }

            public void Incoming(RpcMessage message) {
                Debug.Log("Incoming");
                ClientMessage clientMessage = new ClientMessage();
                clientMessage.rpcMessage = message;
                ActorSystem.instance.client.Send(Serialize(clientMessage));
            }

            private byte[] Serialize(ClientMessage message) {
                MemoryStream stream = new MemoryStream();
                Serializer.Serialize(stream, message);
                return stream.ToArray();
            }
        }
    }
}
