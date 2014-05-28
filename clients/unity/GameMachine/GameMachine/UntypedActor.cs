using GameMachine;
using System.IO;
using  ProtoBuf;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
    public abstract class UntypedActor : IActor
    {
        public ActorSystem actorSystem;

        public UntypedActor()
        {
        }
	
        public void SetActorSystem(ActorSystem _actorSystem)
        {
            actorSystem = _actorSystem;
        }
		
        public abstract void OnReceive(object message);

        public void Tell(object message)
        {
            OnReceive(message);
        }

        public void Unhandled(object message)
        {

        }
    }
}
