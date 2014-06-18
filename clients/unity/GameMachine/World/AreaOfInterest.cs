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
        
        private double lastUpdate = 0;
        private double updatesPerSecond = 10;
        private double updateInterval;
        private EntityTracking entityTracking;
        private NpcManager npcManager;
        
        void Start()
        {
            
            updateInterval = 0.60 / updatesPerSecond;
            
            entityTracking = ActorSystem.Instance.Find("EntityTracking") as EntityTracking;
            
            EntityTracking.UpdateReceived callback = OnUpdateReceived;
            entityTracking.OnUpdateReceived(callback);

            GameObject world = GameObject.Find("World");

            npcManager = world.GetComponent<NpcManager>() as NpcManager;
        }
        
        void Update()
        {
            if (Time.time > (lastUpdate + updateInterval))
            {
                lastUpdate = Time.time;
                Vector3 position = this.gameObject.transform.position;
                
                TrackingUpdate update = new TrackingUpdate(User.Instance.username, position.x, position.z, position.y);
                update.entityType = "player";
                //update.neighborEntityType = "npc";

                TrackExtra trackExtra = new TrackExtra();
                trackExtra.speed = 1.0f;
                trackExtra.velocity = 12.0f;
                update.trackExtra = trackExtra;
                
                entityTracking.Update(update);
            }
        }
        
        void OnUpdateReceived(List<TrackingUpdate> updates)
        {
            foreach (TrackingUpdate update in updates)
            {
                npcManager.UpdateFromTracking(update); 
            }
            //Logger.Debug("Update received");
        }
        
    }
}