using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine.Pathfinding
{
    public class Agent : MonoBehaviour
    {
        public bool useCrowd = false;
        public string crowdName;
        public float agentAccel = 8.0f;
        public float agentSpeed = 4.0f;
        public float agentRadius = 0.6f;
        public float agentHeight = 2.0f;
        public int agentOptFlag = 5;
        public float agentSeparationWeight = 10.0f;

        private Manager manager;
        private Pathfinder pathfinder;
        private Crowd crowd;
        private Vector3 currentTarget = Vector3.zero;
        private CharacterController controller;
        private Terrain terrain;
        private ResultPath result;
        private int agentIdx = -1;
        private Stack<Vector3> stack = new Stack<Vector3> ();

        void Start ()
        {
            pathfinder = Manager.pathfinder;
            crowd = pathfinder.getCrowd (crowdName);

            terrain = Terrain.activeTerrain;
            controller = GetComponent<CharacterController> ();
            Vector3 p = new Vector3 (transform.position.x, terrain.SampleHeight (transform.position), transform.position.z);
            agentIdx = crowd.AddAgent (p, agentAccel, agentSpeed, agentRadius, agentHeight, agentOptFlag, agentSeparationWeight);
        }
	
        void Update ()
        {


            if (useCrowd) {
                CrowdUpdate ();
            } else {
                SoloUpdate ();
            }
        }

        void CrowdUpdate ()
        {
            if (agentIdx != -1) {
                currentTarget = crowd.GetAgentPosition (agentIdx);
                currentTarget.y = currentTarget.y + 2.0f;
            }
            if (currentTarget != Vector3.zero) {
                
                MoveToTarget (currentTarget);
            }
        }

        void SoloUpdate ()
        {
            if (currentTarget == Vector3.zero) {
                if (stack.Count >= 1) {
                    currentTarget = stack.Pop ();
                    currentTarget.y = 2.2f;
                    Debug.Log ("Target set " + currentTarget);
                    Debug.DrawLine (transform.position, currentTarget, Color.red, 100000);
                }
            }
            
            if (currentTarget != Vector3.zero) {
                
                MoveToTarget (currentTarget);
            }
        }

        public void SetTarget (Vector3 target)
        {
            ResultPath result;
            result = pathfinder.FindPath (transform.position, target, true);
            if (result.path != null) {
                currentTarget = Vector3.zero;
                List<Vector3> vecs = new List<Vector3> (result.path);
                vecs.Reverse ();
                stack = new Stack<Vector3> (vecs);
            }
            
        }

        public static void LookAt2D (Transform transform, Vector3 point)
        { 
            Vector3 e = transform.eulerAngles; 
            transform.LookAt (point); 
            e.y = transform.eulerAngles.y; 
            transform.eulerAngles = e;
        }

        public void MoveToTarget (Vector3 target)
        {
            float speed = 5.0f;
            float distance = Vector3.Distance (transform.position, target);
            if (distance < 0.5f) {
                if (useCrowd) {
                    currentTarget = Vector3.zero;
                }
                return;
            }
            if (distance > 5.0f) {
                transform.position = currentTarget;
            }
            if (distance > 1.0f) {
                speed = 8.0f;
            }

            float step = 5f * Time.deltaTime;
            Vector3 targetDir = target - transform.position;
            Vector3 newDir = Vector3.RotateTowards (transform.forward, targetDir, step, 0.0F);
            LookAt2D (transform, target);
            controller.SimpleMove (newDir * 0.8f * speed);
        }

        void OnDestroy ()
        {
            if (agentIdx != -1) {
                crowd.RemoveAgent (agentIdx);
                Debug.Log ("removeAgent " + agentIdx);
            }
        }
    }
}
