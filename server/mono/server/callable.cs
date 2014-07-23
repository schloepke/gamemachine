using GameMachine;
using GameMessage = GameMachine.Messages.GameMessage;
namespace GameMachine
{
    public interface Callable
    {
        GameMessage call(GameMessage message);
    }
}