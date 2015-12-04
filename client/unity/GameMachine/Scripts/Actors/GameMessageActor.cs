using UnityEngine;
using System.Collections;
using System.IO;
using ProtoBuf;
using GameMachine.Core;

using System.Reflection;
using System;
using io.gamemachine.messages;
using System.Collections.Generic;
using System.Linq;

namespace GameMachine {
    public class GameMessageActor : MonoBehaviour {

        private UnityGameMessage unityGameMessage;

        public virtual void OnGameMessage(GameMessage gameMessage) {

        }

        protected void Reply(GameMessage gameMessage) {
            unityGameMessage.gameMessage = gameMessage;
            GameMessageDispatcher.instance.SendUnityGameMessage(unityGameMessage);
            unityGameMessage = null;
        }

        public void OnUnityGameMessage(UnityGameMessage unityGameMessage) {
            this.unityGameMessage = unityGameMessage;
            OnGameMessage(unityGameMessage.gameMessage);
        }

        protected void Register() {
            GameMessageDispatcher.instance.Register(this);
        }

    }
}
