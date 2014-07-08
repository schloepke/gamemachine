using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Entity = GameMachine.Messages.Entity;
using TrackData = GameMachine.Messages.TrackData;

namespace GameMachine.World
{
    
	public class AreaOfInterest : MonoBehaviour
	{
        
		private EntityTracking entityTracking;
		private NpcManager npcManager;
		private KAM3RA.User cameraUser;
		private GameMachine.Messages.Vector3 directionVector = new GameMachine.Messages.Vector3 ();

		void Start ()
		{
            
			entityTracking = ActorSystem.Instance.Find ("EntityTracking") as EntityTracking;
            
			EntityTracking.UpdateReceived callback = OnUpdateReceived;
			entityTracking.OnUpdateReceived (callback);

			GameObject world = GameObject.Find ("World");

			npcManager = world.GetComponent<NpcManager> () as NpcManager;

			GameObject camera = GameObject.Find ("KAM3RA");
			cameraUser = camera.GetComponent<KAM3RA.User> ();

			InvokeRepeating ("UpdateTracking", 0.010f, 0.08F);
		}
        
		void UpdateTracking ()
		{

			Vector3 position = this.gameObject.transform.position;
			Vector3 direction = cameraUser.Direction;

			directionVector.x = direction.x;
			directionVector.z = direction.z;

			TrackingUpdate update = new TrackingUpdate (User.Instance.username, position.x, position.z, position.y);
			update.entityType = "player";
			TrackData trackData = new TrackData ();
			trackData.direction = directionVector;
			update.trackData = trackData;
			entityTracking.Update (update);
		}
        
		void OnUpdateReceived (List<TrackingUpdate> updates)
		{
			npcManager.UpdateFromTracking (updates);

		}
        
	}
}