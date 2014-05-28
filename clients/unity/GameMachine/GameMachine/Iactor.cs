using GameMachine;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
    public interface IActor
    {
        void OnReceive(object message);
        void SetActorSystem(ActorSystem _actorSystem);
        void Tell(object message);
    }
}
