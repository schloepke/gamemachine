using GameMachine;
using Entity = io.gamemachine.messages.Entity;

namespace GameMachine.Core
{
	public interface IActor
	{
		void OnReceive (object message);
		void SetActorSystem (ActorSystem _actorSystem);
		void Tell (object message);
	}
}
