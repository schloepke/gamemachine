using GameMachine;
using Entity = GameMachine.Messages.Entity;
namespace GameMachine
{
    public interface IActor
    {
        void OnReceive(object message);
        void PostInit(ProxyClient proxyClient);
        void Tell(string destination, Entity entity);
    }
}