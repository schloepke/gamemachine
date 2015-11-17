using UnityEngine;
using System.Collections;
using System.IO;
using ProtoBuf;
using GameMachine.Core;

using System.Reflection;
using System;
using io.gamemachine.messages;
using System.Collections.Generic;

namespace GameMachine {
    namespace ServerClient {
        public class RpcHandler : MonoBehaviour {

            public static RpcHandler instance;
            private Dictionary<string, MethodInfo> rpcMethods = new Dictionary<string, MethodInfo>();
           
            public void RegisterMethods() {
                RegisterMethod(typeof(RpcHandler), "TestMethod");
            }

            public void RegisterMethod(Type type, string methodName) {
                string id = type.FullName + "." + methodName;
                Debug.Log("Register Rpc " + id);
                rpcMethods[id] = type.GetMethod(methodName, BindingFlags.Static | BindingFlags.Public);
            }

            public static GameMessage TestMethod(GameMessage gameMessage) {
                return gameMessage;
            }

            void Awake() {
                instance = this;
                RegisterMethods();
            }

            void FixedUpdate() {
                RpcMessage message;

                for (int i = 0; i < ClientMessageQueue.rpcQueue.Count; i++) {
                    if (ClientMessageQueue.rpcQueue.TryDequeue(out message)) {
                        Dispatch(message);
                    }
                }
                //Debug.Log("Update time :" + Time.deltaTime);
            }

            public void Dispatch(RpcMessage rpcMessage) {
                if (rpcMethods.ContainsKey(rpcMessage.method)) {
                    MethodInfo methodInfo = rpcMethods[rpcMessage.method];
                    object[] args = new object[] { rpcMessage.gameMessage };
                    GameMessage gameMessage = (GameMessage)methodInfo.Invoke(null, args);

                    rpcMessage.gameMessage = gameMessage;
                    SendRpcMessage(rpcMessage);
                }
               
            }
            public void SendRpcMessage(RpcMessage message) {
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
