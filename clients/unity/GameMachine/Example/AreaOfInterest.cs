using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Entity = GameMachine.Messages.Entity;
using TrackExtra = GameMachine.Messages.TrackExtra;

// This is an example of how to track objects in the game that are near you.
// The basic flow is that you send a message to the server at regular intervals with your position, and a request
// to return a message to you containing all objects within range.  This is sent as a single message.

// The grid tracking allows you to filter results based on the type of objects you are interested in.
// There are two types you can use.  'player' and 'npc'. These choices were completely arbitrary and will
// soon be changed so you can use any string you want to filter with.

// The default tracking radius is 25 points.  That and the size of the grid is configurable on the server side.  
// Note that on the server you can also instantiate multiple grids if you wish, and use them however you want.

// IMPORTANT -  If you just want to send locations between players and you do not care about if they are near you or not,
// this is still the best tool for the job.  Just increase the grid and cell size so that your cell size is roughly the same area
// as your entire world.  World grid size being much larger then your world area is fine and will do no harm.

// In the server config you can change the following values:
// world_grid_size - default 2000
// world_grid_cell_size - default 25.  This is your tracking radius

// The width of the grid in points is world_grid_size / world_grid_cell_size. 
// You can adjust these numbers any way you like just remember the following rules:
//   - world_grid_cell_size must divide evenly into world_grid_size.
//   - world_grid_size must be >= world_grid_cell_size * 3
//   - world_grid_cell_size is always your search radius.
namespace GameMachine.Example
{

	public class AreaOfInterest : MonoBehaviour
	{

		private EntityTracking entityTracking;

		void Start ()
		{
	
			entityTracking = ActorSystem.Instance.Find ("EntityTracking") as EntityTracking;

			EntityTracking.UpdateReceived callback = OnUpdateReceived;
			entityTracking.OnUpdateReceived (callback);
			InvokeRepeating ("UpdateTracking", 0.010f, 0.06F);
		}
	
		void UpdateTracking ()
		{
			Vector3 position = this.gameObject.transform.position;

			// Create object with our coordinates
			TrackingUpdate update = new TrackingUpdate (User.Instance.username, position.x, position.z, position.y);

			// Optional.  Tell the server to set our entity type to this value.  Searches
			// can filter on this.
			update.entityType = "player";

			// Optional, tell the server to filter on this type of entity in the search, and only
			// return entities that match this type.  A null value means return anything within radius.
			//update.neighborEntityType = "npc";


			// TrackExtra is a message you can customize any way you want and allows you to extend the fields that
			// the tracking system stores. It will be saved on the server and returned in tracking updates to other clients.
			// TrackExtra is located in config/game_messages.proto on the server, or you can edit it via the web ui.
			TrackExtra trackExtra = new TrackExtra ();
			trackExtra.speed = 1.0f;
			trackExtra.velocity = 12.0f;
			update.trackExtra = trackExtra;
               
			entityTracking.Update (update);
		}

		void OnUpdateReceived (List<TrackingUpdate> updates)
		{
			foreach (TrackingUpdate update in updates) {

			}
		}

	}
}
