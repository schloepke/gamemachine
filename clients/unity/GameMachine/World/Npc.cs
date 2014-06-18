using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using KAM3RA;

namespace GameMachine.World
{
    public class Npc : Actor
    {
        private Vector3 targetLocation ;
        public string name;
        private Vector3 currentTarget;
        private float lastUpdate = Time.time + 0.120f;

        protected override void Start()
        {
            base.Start();
            SetNameTag(name);

            // we're giving all NPC's hover ability
            this.type = Type.Hover;
        }
        protected override void Update()
        {

            base.Update();

            if (player)
            {
                return;
            }

            float elapsed = Time.time - lastUpdate;
            if (elapsed > 0.220f)
            {
                NpcManager.DestroyNcp(name);
                Logger.Debug(elapsed.ToString());
            }
            if (currentTarget != null)
            {
                float distance = Vector3.Distance(KAM3RA.User.VectorXZ(transform.position), KAM3RA.User.VectorXZ(currentTarget));
                if (distance < 1.5f)
                {
                    State = "Idle";
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

        public void UpdateTarget(Vector3 target)
        {
            currentTarget = target;
            //Logger.Debug((Time.time - lastUpdate).ToString());
            lastUpdate = Time.time;

        }

        public void MoveToTarget(Vector3 target)
        {
            if (Colliding)
            {
                State = Speed > (2f * transform.localScale.y) ? "RunForward" : "WalkForward";
               
                Vector3 position = transform.position;
                Vector3 destination = target;
                float distance = Vector3.Distance(KAM3RA.User.VectorXZ(position), KAM3RA.User.VectorXZ(destination));
                if (distance < Radius)
                {
                }
                if (!TooFast)
                {
                    velocity = (destination - position).normalized * ScaledSpeed;
                }
                KAM3RA.User.LookAt2D(transform, destination);
            }
        }
    }
}