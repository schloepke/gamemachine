using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using TrackExtra = GameMachine.Messages.TrackExtra;

namespace GameMachine.World
{
    
    public class AreaOfInterest : MonoBehaviour
    {
        
        private EntityTracking entityTracking;
        private NpcManager npcManager;
        
        void Start()
        {
            
            entityTracking = ActorSystem.Instance.Find("EntityTracking") as EntityTracking;
            
            EntityTracking.UpdateReceived callback = OnUpdateReceived;
            entityTracking.OnUpdateReceived(callback);

            GameObject world = GameObject.Find("World");

            npcManager = world.GetComponent<NpcManager>() as NpcManager;

            InvokeRepeating("UpdateTracking", 0.010f, 0.06F);
        }
        
        void UpdateTracking()
        {

            Vector3 position = this.gameObject.transform.position;
                
            TrackingUpdate update = new TrackingUpdate(User.Instance.username, position.x, position.z, position.y);
            update.entityType = "player";

            // Get everyone
            //update.neighborEntityType = "npc";

            // Don't need yet
            //TrackExtra trackExtra = new TrackExtra();
            //trackExtra.speed = 1.0f;
            //trackExtra.velocity = 12.0f;
            //update.trackExtra = trackExtra;
                
            entityTracking.Update(update);
        }
        
        void OnUpdateReceived(List<TrackingUpdate> updates)
        {
            npcManager.UpdateFromTracking(updates);

        }
        
    }
}