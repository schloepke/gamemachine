using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using KAM3RA;

namespace GameMachine.World
{
    public class OtherPlayer : NpcController
    {
        private Vector3 targetLocation ;
        public string name;
        private Vector3 currentTarget;
        public float lastUpdate;
        private CharacterController controller;
        private Terrain terrain;
        
        protected override void Start()
        {
            terrain = Terrain.activeTerrain;
            lastUpdate = Time.time;
            base.Start();
            SetNameTag(name);
            controller = GetComponent<CharacterController>();
            this.type = Type.Ground;
        }
        
        public Vector3 SpawnLocation(Vector3 vector)
        {
            terrain = Terrain.activeTerrain;
            float height = terrain.SampleHeight(vector);
            Vector3 spawnPoint = new Vector3(vector.x, (height + 1.05f), vector.z);
            return spawnPoint;
        }
        
        protected override void Update()
        {
            
            if (player)
            {
                return;
            }
            
            if (currentTarget != null)
            {
                
                MoveToTarget(currentTarget);
                
            }
            if (State == "Idle")
            {
                return;
            }
        }
        
        public void UpdateTarget(Vector3 target)
        {
            currentTarget = target;
            lastUpdate = Time.time;
        }
        
        public float ScaleSpeed(float distance)
        {
            float scaledSpeed = 1.2f;
            if (distance > 10.0f)
            {
                scaledSpeed = 4.0f;
            } else if (distance > 5.0f)
            {
                scaledSpeed = 3.0f;
            } else if (distance > 2.5f)
            {
                scaledSpeed = 1.8f;
            } else if (distance > 2.0f)
            {
                scaledSpeed = 1.5f;
            }
            return scaledSpeed;
        }
        
        
        public void MoveToTarget(Vector3 target)
        {
            
            float distance = Vector3.Distance(KAM3RA.User.VectorXZ(transform.position), KAM3RA.User.VectorXZ(target));
            speed = ScaleSpeed(distance);
            
            
            if (distance >= 0.5f)
            {
                if (speed > 2.0f)
                {
                    if (animation ["Run"])
                    {
                        State = "RunForward";
                    } else
                    {
                        State = "WalkForward";
                    }
                    
                } else
                {
                    State = "WalkForward";
                }
                
                
                float step = 5f * Time.deltaTime;
                Vector3 targetDir = target - transform.position;
                Vector3 newDir = Vector3.RotateTowards(transform.forward, targetDir, step, 0.0F);
                KAM3RA.User.LookAt2D(transform, target);
                //newDir.y = 0;
                //transform.rotation = Quaternion.LookRotation(newDir);
                
                controller.SimpleMove(newDir * 0.8f * speed);
            } else
            {
                State = "Idle";
            }
            
        }
    }
}