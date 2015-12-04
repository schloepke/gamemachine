using GameMachine;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System;
using System.Runtime.Serialization.Formatters.Binary;
using Entity = io.gamemachine.messages.Entity;
using TrackEntity = io.gamemachine.messages.TrackEntity;
using Neighbors = io.gamemachine.messages.Neighbors;
using Neighbor = io.gamemachine.messages.Neighbor;
using TrackData = io.gamemachine.messages.TrackData;


namespace GameMachine.Core
{
	public class EntityTracking : UntypedActor
	{
        
		public Trackable trackable;

		public static float fromDelta (float pos, int delta)
		{
			pos += (float)(delta / 100f);
			return pos;
		}

		public static int ToInt (float f)
		{
			return (int)(Math.Round (f * 100, 2));
		}
		public static void Register (Trackable component)
		{
			EntityTracking entityTracking = ActorSystem.instance.Find ("EntityTracking") as EntityTracking;
			entityTracking.trackable = component;
			ActorSystem.instance.InvokeRepeating (entityTracking, "UpdateTracking");
		}

		public void UpdateTracking ()
		{
			TrackData trackData = trackable.UpdateTracking ();
			Update (trackData);
		}

		public void Update (TrackData trackData)
		{
			if (trackData == null) {
				return;
			}

			trackData.id = User.Instance.username;
			if (trackData.entityType == TrackData.EntityType.None) {
				trackData.entityType = TrackData.EntityType.Player;
			}
			
			Entity entity = new Entity ();
			entity.id = "0";
			entity.trackData = trackData;

			// Always regional
			ActorSystem.instance.FindRegional ("default").Tell (entity);
		}
		public override void OnReceive (object message)
		{
			Entity entity = message as Entity;

			if (entity.trackDataResponse != null) {
				if (trackable != null) {
					trackable.HandleTrackDataResponse (entity.trackDataResponse);
				}
				return;
			}

			if (trackable != null) {
				trackable.TrackDataReceived (entity.neighbors.trackData);
			}

		}
	}
}