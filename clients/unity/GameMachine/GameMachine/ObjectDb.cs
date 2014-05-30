
using GameMachine;

using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
    public class ObjectDb : UntypedActor
    {
        public delegate void EntityReceived();
        
        private EntityReceived entityReceived;

        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            if (entity.objectdbGetResponse.entityFound)
            {

            }
        }
    }
}