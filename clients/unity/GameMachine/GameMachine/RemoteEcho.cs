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
        private string playerId;
        
        public void OnEchoReceived(EchoReceived callback)
        {
            echoReceived = callback;
        }

        public void Echo()
        {
            Entity entity = new Entity();
            entity.id = "echo";
            EchoTest echoTest = new EchoTest();
            echoTest.message = "echo";
            entity.echoTest = echoTest;
            ActorSystem.Instance.FindRemote("GameMachine/GameSystems/RemoteEcho").Tell(entity);
        }
       
        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            if (entity.echoTest != null)
            {
                if (echoReceived != null)
                {
                    echoReceived();
                }
            }
        }
    }
}