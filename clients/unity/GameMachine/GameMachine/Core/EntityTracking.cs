using GameMachine;
using System.Collections.Generic;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;
using Entity = GameMachine.Messages.Entity;
using TrackEntity = GameMachine.Messages.TrackEntity;
using Vector3 = GameMachine.Messages.Vector3;
using Neighbors = GameMachine.Messages.Neighbors;
using Neighbor = GameMachine.Messages.Neighbor;
using GetNeighbors = GameMachine.Messages.GetNeighbors;
using TrackData = GameMachine.Messages.TrackData;

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
			if (update.trackData != null) {
				trackEntity.trackData = update.trackData; 
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

			List<TrackingUpdate> updates = new List<TrackingUpdate> ();
			int i = 0;
			foreach (TrackData trackData in entity.neighbors.trackData) {
				TrackingUpdate update = new TrackingUpdate (trackData.id, trackData.x, trackData.y, trackData.z);
				update.trackData = trackData;
				updates.Add (update);
				i++;
			}

			if (updateReceived != null) {
				updateReceived (updates);
			
			}
		}
	}
}