using UnityEngine;
using System.Collections;
using System.Collections.Generic;

namespace GameMachine.Pathfinding
{
    public class Manager : MonoBehaviour
    {
        public int navmeshId = 1;
        public string navmeshFile = "navmesh.bin";
        public float crowdStep = 0.01f;

        public static Pathfinder pathfinder;

        void Awake ()
        {
            pathfinder = new Pathfinder (navmeshId, navmeshFile);
        }

        void Start ()
        {
            InvokeRepeating ("UpdateCrowds", 0.02f, 0.02F);
        }
	
        void Update ()
        {

        }

        void UpdateCrowds ()
        {
            pathfinder.TickCrowds (crowdStep);
        }
    }
}
