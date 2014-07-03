using GameMachine;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine.Core
{
	public interface IActor
	{
		void OnReceive (object message);
		void SetActorSystem (ActorSystem _actorSystem);
		void Tell (object message);
	}
}
