using GameMachine;
using Entity = io.gamemachine.messages.Entity;
using EchoTest = io.gamemachine.messages.EchoTest;
using ObjectdbGet = io.gamemachine.messages.ObjectdbGet;
using ObjectdbGetResponse = io.gamemachine.messages.ObjectdbGetResponse;
using NativeBytes = io.gamemachine.messages.NativeBytes;
using Newtonsoft;

namespace GameMachine.Core
{
    public class RemoteEcho : UntypedActor
    {
        
        public delegate void EchoReceived ();
        private EchoReceived echoReceived;
        public delegate void RegionEchoReceived ();
        private RegionEchoReceived regionEchoReceived;

        private string playerId;
        
        public void OnEchoReceived (EchoReceived callback)
        {
            echoReceived = callback;
        }

        public void OnRegionEchoReceived (RegionEchoReceived callback)
        {
            regionEchoReceived = callback;
        }

        public Entity EchoMessage (string id)
        {
            Entity entity = new Entity ();
            entity.id = id;
            EchoTest echoTest = new EchoTest ();
            echoTest.message = id;
            entity.echoTest = echoTest;
            return entity;
        }

        public void Echo ()
        {
            ActorSystem.Instance.FindRemote ("GameMachine/GameSystems/RemoteEcho").Tell (EchoMessage ("cluster"));
        }
       
        public void RegionEcho ()
        {
            // We dont' use FindRegional here because it falls back to FindRemote, which in this specific case
            // we don't want
            ActorSystem.Instance.Find ("GameMachine/GameSystems/RemoteEcho", true, true).Tell (EchoMessage ("region"));
        }

        public override void OnReceive (object message)
        {
            Entity entity = message as Entity;
            if (entity.echoTest != null) {
                if (entity.id == "cluster") {
                    if (echoReceived != null) {
                        echoReceived ();
                    }
                } else if (entity.id == "region") {
                    if (regionEchoReceived != null) {
                        regionEchoReceived ();
                    }
                }

            }
        }
    }
}