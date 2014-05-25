using GameMachine;
using Entity = com.game_machine.entity_system.generated.Entity;
namespace GameMachine
{
	public interface IActor {
		void OnReceive (object message);
		void PostInit (ProxyClient proxyClient);
		void Tell(string destination, Entity entity);
	}
}