using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using Entity = io.gamemachine.messages.Entity;
using TrackData = io.gamemachine.messages.TrackData;

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
            ActorSystem.Instance.InvokeRepeating (this, "UpdateTracking");
        }
	
        void UpdateTracking ()
        {
            Vector3 position = this.gameObject.transform.position;

            TrackData trackData = new TrackData ();
            trackData.x = EntityTracking.ToInt (position.x);
            trackData.y = EntityTracking.ToInt (position.z);
            trackData.z = EntityTracking.ToInt (position.y);
            trackData.id = User.Instance.username;
            trackData.entityType = TrackData.EntityType.PLAYER;
            entityTracking.Update (trackData);
            
        }

        void OnUpdateReceived (List<TrackingUpdate> updates)
        {
            foreach (TrackingUpdate update in updates) {
            }
        }

    }
}
