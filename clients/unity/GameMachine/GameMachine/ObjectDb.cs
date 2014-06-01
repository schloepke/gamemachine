
using GameMachine;

using Entity = GameMachine.Messages.Entity;
using ObjectdbGet = GameMachine.Messages.ObjectdbGet;
using ObjectdbGetResponse = GameMachine.Messages.ObjectdbGetResponse;
using NativeBytes = GameMachine.Messages.NativeBytes;
using Newtonsoft;

namespace GameMachine
{
    public class ObjectDb : UntypedActor
    {

        public delegate void ObjectReceived(object message);
        private ObjectReceived objectReceived;
        private string playerId;

        public void OnObjectReceived(ObjectReceived callback)
        {
            objectReceived = callback;
        }

        public void SetPlayerId(string _playerId)
        {
            playerId = _playerId;
        }

        public void Find(string entityId)
        {
            Entity entity = new Entity();
            entity.id = entityId;
            ObjectdbGet get = new ObjectdbGet();
            get.entityId = entityId;
            get.playerId = playerId;
            entity.objectdbGet = get;
            ActorSystem.Instance.Find("/remote/default").Tell(entity);
        }
        public void Save(object message)
        {
            Entity entity = message as Entity;
            entity.save = true;
            ActorSystem.Instance.Find("/remote/default").Tell(entity);
        }
        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            if (entity.objectdbGetResponse.entityFound)
            {
                objectReceived(entity);
            }
        }
    }
}