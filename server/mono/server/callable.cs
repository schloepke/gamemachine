using GameMachine;
using Entity = GameMachine.Messages.Entity;
namespace GameMachine
{
    public interface Callable
    {
        Entity call(Entity entity);
    }
}