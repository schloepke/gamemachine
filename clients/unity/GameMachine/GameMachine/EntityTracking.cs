using GameMachine;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using Entity = GameMachine.Messages.Entity;
using TrackEntity = GameMachine.Messages.TrackEntity;
using Vector3 = GameMachine.Messages.Vector3;
using Neighbors = GameMachine.Messages.Neighbors;
using GetNeighbors = GameMachine.Messages.GetNeighbors;
using TrackExtra = GameMachine.Messages.TrackExtra;

namespace GameMachine
{
    public class EntityTracking : UntypedActor
    {
        
        public delegate void UpdateReceived(List<TrackingUpdate> updates);
        private UpdateReceived updateReceived;


        public void OnUpdateReceived(UpdateReceived callback)
        {
            updateReceived = callback;
        }


        private GetNeighbors CreateGetNeighbors(string neighborType, Vector3 vector)
        {
            GetNeighbors getNeighbors = new GetNeighbors();
            getNeighbors.neighborType = neighborType;
            getNeighbors.vector3 = vector;
            return getNeighbors;
        }

        public void Update(TrackingUpdate update)
        {
           
            Entity entity = new Entity();
            entity.id = "1";
            entity.entityType = update.entityType;

            entity.vector3 = new Vector3();
            entity.vector3.x = update.x;
            entity.vector3.y = update.y;
            entity.vector3.z = update.z;

            TrackEntity trackEntity = new TrackEntity();
            trackEntity.value = true;
            if (update.trackExtra != null)
            {
                trackEntity.trackExtra = update.trackExtra; 
            }
            entity.trackEntity = trackEntity;

            entity.getNeighbors = CreateGetNeighbors(update.neighborEntityType, entity.vector3);

           
            ActorSystem.Instance.Find("/remote/default").Tell(entity);
        }
        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;
            if (entity.neighbors != null)
            {
                if (entity.neighbors.entity.Count >= 1)
                {
                    List<TrackingUpdate> updates = new List<TrackingUpdate>();
                    foreach (Entity r in entity.neighbors.entity)
                    {
                        TrackingUpdate update = new TrackingUpdate(r.vector3.x, r.vector3.y, r.vector3.z);
                        update.entityId = r.id;
                        if (r.trackExtra != null)
                        {
                            update.trackExtra = r.trackExtra;
                        }

                       
                        updates.Add(update);
                    }

                    updateReceived(updates);
                }
                    
            }
        }

        // Convert an object to a byte array
        private byte[] ObjectToByteArray(object obj)
        {
            if (obj == null)
                return null;
            BinaryFormatter bf = new BinaryFormatter();
            MemoryStream ms = new MemoryStream();
            bf.Serialize(ms, obj);
            return ms.ToArray();
        }
        
        // Convert a byte array to an Object
        private object ByteArrayToObject(byte[] arrBytes)
        {
            MemoryStream memStream = new MemoryStream();
            BinaryFormatter binForm = new BinaryFormatter();
            memStream.Write(arrBytes, 0, arrBytes.Length);
            memStream.Seek(0, SeekOrigin.Begin);
            object obj = (object)binForm.Deserialize(memStream);
            return obj;
        }
    }
}