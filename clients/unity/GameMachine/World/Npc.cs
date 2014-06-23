using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using KAM3RA;

// TODO separate classes for npc and player
namespace GameMachine.World
{
    public class Npc : NpcController
    {
        private Vector3 targetLocation ;
        public string name;
        private Vector3 currentTarget;
        private Vector3 currentDirection;
        private float currentSpeed;

        public float lastUpdate;
        private CharacterController controller;
        private Terrain terrain;
        public bool isPlayer = false;
        private float lastAttack;


        protected override void Start()
        {
            terrain = Terrain.activeTerrain;
            lastUpdate = Time.time;
            base.Start();
            SetNameTag(name);
            controller = GetComponent<CharacterController>();
            //controller.detectCollisions = false;
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
               
                if (isPlayer)
                {
                    MovePlayer();
                } else
                {
                    MoveToTarget(currentTarget);
                }
            }
            if (State == "Idle")
            {
                return;
            }
        }



        public void UpdatePlayer(Vector3 target, Vector3 direction, float speed)
        {
            currentTarget = target;
            currentDirection = direction;
            currentDirection.y = 0;
            currentSpeed = speed;
            lastUpdate = Time.time;
        }

        public void Attack(CombatUpdate combatUpdate)
        {
            lastAttack = Time.time;
            State = "Attack";
        }

        public void Attacked(CombatUpdate combatUpdate)
        {
            SetNameTag(name + " (" + combatUpdate.target_health + ")");
        }

        public void MovePlayer()
        {

            Quaternion qTo = Quaternion.LookRotation(currentDirection);
            
            transform.rotation = Quaternion.Slerp(transform.rotation, qTo, 4f * Time.deltaTime);

            float distance = Vector3.Distance(KAM3RA.User.VectorXZ(transform.position), KAM3RA.User.VectorXZ(currentTarget));
            speed = 4.0f; //ScaleSpeed(distance);
            if (distance >= 0.3f)
            {
                if (speed > 2.0f)
                {
                    if (animation ["Run"] || animation ["run"])
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
                Vector3 targetDir = currentTarget - transform.position;
                controller.SimpleMove(targetDir * 0.8f * speed);
            } else
            {
                State = "Idle";
            }


            if (Time.time - lastAttack < 1.0f)
            {
                State = "Attack";
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
               
                controller.SimpleMove(newDir * 0.8f * speed);
            } else
            {
                State = "Idle";
            }

        }
    }
}