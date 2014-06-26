using GameMachine;
using Entity = GameMachine.Messages.Entity;
using EchoTest = GameMachine.Messages.EchoTest;
using ObjectdbGet = GameMachine.Messages.ObjectdbGet;
using ObjectdbGetResponse = GameMachine.Messages.ObjectdbGetResponse;
using NativeBytes = GameMachine.Messages.NativeBytes;
using Newtonsoft;

namespace GameMachine
{
    public class RemoteEcho : UntypedActor
    {
        
        public delegate void EchoReceived();
        private EchoReceived echoReceived;
        public delegate void RegionEchoReceived();
        private RegionEchoReceived regionEchoReceived;

        private string playerId;
        
        public void OnEchoReceived(EchoReceived callback)
        {
            echoReceived = callback;
        }

        public void OnRegionEchoReceived(RegionEchoReceived callback)
        {
            regionEchoReceived = callback;
        }

        public Entity EchoMessage(string id)
        {
            Entity entity = new Entity();
            entity.id = id;
            EchoTest echoTest = new EchoTest();
            echoTest.message = id;
            entity.echoTest = echoTest;
            return entity;
        }

        public void Echo()
        {

            ActorSystem.Instance.FindRemote("GameMachine/GameSystems/RemoteEcho").Tell(EchoMessage("cluster"));
        }
       
        public void RegionEcho()
        {
            // We dont' use FindRegional here because it falls back to FindRemote, which in this specific case
            // we don't want
            ActorSystem.Instance.Find("GameMachine/GameSystems/RemoteEcho", true, true).Tell(EchoMessage("region"));
        }

        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            if (entity.echoTest != null)
            {
                if (entity.id == "cluster")
                {
                    if (echoReceived != null)
                    {
                        echoReceived();
                    }
                } else if (entity.id == "region")
                {
                    if (regionEchoReceived != null)
                    {
                        regionEchoReceived();
                    }
                }

            }
        }
    }
}