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

			ActorSystem.Instance.InvokeRepeating (this, "UpdateTracking");
		}
        
		void UpdateTracking ()
		{

			Vector3 position = this.gameObject.transform.position;
			Vector3 direction = cameraUser.Direction;

			directionVector.x = direction.x;
			directionVector.z = direction.z;
			TrackData trackData = new TrackData ();
			trackData.x = position.x;
			trackData.y = position.z;
			trackData.z = position.y;
			trackData.id = User.Instance.username;
			trackData.entityType = "player";
			trackData.direction = directionVector;
			entityTracking.Update (trackData);
		}
        
		void OnUpdateReceived (List<TrackingUpdate> updates)
		{
			npcManager.UpdateFromTracking (updates);

		}
        
	}
}