using UnityEngine;
using System.Collections;
using io.gamemachine.messages;
using System.Collections.Generic;
using GameMachine.Core;
using System.IO;
using ProtoBuf;
using GameMachine.Common;

namespace GameMachine {
    public class GameMessageDispatcher : MonoBehaviour {

        public static GameMessageDispatcher instance;
        private Dictionary<string, GameMessageActor> gameMessageActors = new Dictionary<string, GameMessageActor>();

        public void Register(GameMessageActor instance) {
            string actorName = instance.GetType().Name;
            gameMessageActors[actorName] = instance;
            UnityGameMessage unityGameMessage = new UnityGameMessage();
            unityGameMessage.actorName = actorName;
            unityGameMessage.messageType = UnityGameMessage.MessageType.Register;
            SendUnityGameMessage(unityGameMessage);
        }

        public void SendUnityGameMessage(UnityGameMessage unityGameMessage) {
            unityGameMessage.playerId = NetworkSettings.instance.username;
            ClientMessage clientMessage = new ClientMessage();
            clientMessage.unityGameMessage = unityGameMessage;
            ActorSystem.instance.client.Send(Serialize(clientMessage));
        }

        void Awake() {
            instance = this;
        }

        void Update() {
            UnityGameMessage unityGameMessage;

            for (int i = 0; i < ClientMessageQueue.unityGameMessageQueue.Count; i++) {
                if (ClientMessageQueue.unityGameMessageQueue.TryDequeue(out unityGameMessage)) {
                    Dispatch(unityGameMessage);
                }
            }
        }

        private void Dispatch(UnityGameMessage unityGameMessage) {
            if (string.IsNullOrEmpty(unityGameMessage.actorName)) {
                Debug.Log("UnityGameMessage without actorName");
                return;
            }

            if (!gameMessageActors.ContainsKey(unityGameMessage.actorName)) {
                Debug.Log("Actor not found with name " + unityGameMessage.actorName);
                return;
            }

            gameMessageActors[unityGameMessage.actorName].OnUnityGameMessage(unityGameMessage);
        }
        
        private byte[] Serialize(ClientMessage message) {
            MemoryStream stream = new MemoryStream();
            Serializer.Serialize(stream, message);
            return stream.ToArray();
        }

        
    }
}
