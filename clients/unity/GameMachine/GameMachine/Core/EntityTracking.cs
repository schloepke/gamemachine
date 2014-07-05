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

namespace GameMachine.Core
{
	public class EntityTracking : UntypedActor
	{
        
		public delegate void UpdateReceived (List<TrackingUpdate> updates);
		private UpdateReceived updateReceived;


		public void OnUpdateReceived (UpdateReceived callback)
		{
			updateReceived = callback;
		}


		private GetNeighbors CreateGetNeighbors (string neighborType, Vector3 vector)
		{
			GetNeighbors getNeighbors = new GetNeighbors ();
			getNeighbors.neighborType = neighborType;
			getNeighbors.vector3 = vector;
			return getNeighbors;
		}

		public void Update (TrackingUpdate update)
		{
           
			Entity entity = new Entity ();
			entity.id = update.entityId;
			entity.entityType = update.entityType;

			entity.vector3 = new Vector3 ();
			entity.vector3.x = update.x;
			entity.vector3.y = update.y;
			entity.vector3.z = update.z;

			TrackEntity trackEntity = new TrackEntity ();
			trackEntity.value = true;
			if (update.trackExtra != null) {
				trackEntity.trackExtra = update.trackExtra; 
			}
			entity.trackEntity = trackEntity;

			entity.getNeighbors = CreateGetNeighbors (update.neighborEntityType, entity.vector3);

			// stay on the fastpath server side (no crossing language boundaries)
			entity.fastpath = true;

			// Always regional
			ActorSystem.Instance.FindRegional ("default").Tell (entity);
		}
		public override void OnReceive (object message)
		{
			Entity entity = message as Entity;
			if (entity.neighbors != null) {
				if (entity.neighbors.entity.Count >= 1) {
					List<TrackingUpdate> updates = new List<TrackingUpdate> ();
					foreach (Entity r in entity.neighbors.entity) {
						TrackingUpdate update = new TrackingUpdate (r.id, r.vector3.x, r.vector3.y, r.vector3.z);
						if (r.trackExtra != null) {
							update.trackExtra = r.trackExtra;
						}

                       
						updates.Add (update);
					}

					if (updateReceived != null) {
						updateReceived (updates);
					}
				}
			}
		}


	}
}