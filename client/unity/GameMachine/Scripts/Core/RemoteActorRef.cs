using ProtoBuf;
using Entity = io.gamemachine.messages.Entity;
using GameMachine;
using System.Text.RegularExpressions;
using System;

namespace GameMachine.Core {

    public class RemoteActorRef : UntypedActor {
        private string destination;
        private bool regional = false;

        public RemoteActorRef(string _destination) {
            destination = _destination;
        }

        public void SetRegional(bool regional) {
            this.regional = regional;
        }

        public bool isRegional() {
            return regional;
        }

        public override void OnReceive(object message) {
            Entity entity = message as Entity;
            if (!String.IsNullOrEmpty(destination) && destination != "default") {
                entity.destination = destination;
            }

            actorSystem.TellRemote(entity);

        }
    }
}