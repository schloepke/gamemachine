using GameMachine;
using Entity = GameMachine.Messages.Entity;
using TrackEntity = GameMachine.Messages.TrackEntity;
using Vector3 = GameMachine.Messages.Vector3;
using Neighbors = GameMachine.Messages.Neighbors;
using GetNeighbors = GameMachine.Messages.GetNeighbors;

namespace GameMachine
{
    public class EntityTracking : UntypedActor
    {
        
        public delegate void PlayersReceived(object message);
        private PlayersReceived playersReceived;
        public delegate void NpcsReceived(object message);
        private NpcsReceived npcsReceived;

        public void OnPlayersReceived(PlayersReceived callback)
        {
            playersReceived = callback;
        }

        public void OnNpcsReceived(NpcsReceived callback)
        {
            npcsReceived = callback;
        }

        private GetNeighbors CreateGetNeighbors(string neighborType, Vector3 vector)
        {
            GetNeighbors getNeighbors = new GetNeighbors();
            getNeighbors.neighborType = neighborType;
            getNeighbors.vector3 = vector;
            return getNeighbors;
        }

        public void Update(float x, float y, float z, string neighborType)
        {
            Entity entity = new Entity();
            entity.id = "1";
            entity.entityType = "player";

            entity.vector3 = new Vector3();
            entity.vector3.x = x;
            entity.vector3.y = y;
            entity.vector3.z = z;

            TrackEntity trackEntity = new TrackEntity();
            trackEntity.value = true;
            entity.trackEntity = trackEntity;

            entity.getNeighbors = CreateGetNeighbors(neighborType, entity.vector3);
            ActorSystem.Instance.Find("/remote/default").Tell(entity);
        }
        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            if (entity.neighbors != null)
            {
                if (entity.neighbors.npc.Count >= 1)
                {
                    npcsReceived(entity.neighbors.npc);
                }
                if (entity.neighbors.player.Count >= 1)
                {
                    playersReceived(entity.neighbors.player);
                }
                    
            }
        }
    }
}