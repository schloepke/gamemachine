using UnityEngine;
using System.Collections.Generic;
using System.Linq;
using System;

namespace GameMachine {
    public class NpcGroup : MonoBehaviour {

        public enum Formation {
            SingleFile,
            Random
        }


        public NpcDef leaderDef;
        public Formation formation = Formation.SingleFile;
        public List<NpcDef> memberDefs = new List<NpcDef>();
        public WaypointRoute route;
        public Npc leader;
        public float formationRandomRange = 10f;
        public bool showFormationPoint = false;

        private List<Transform> followTargets = new List<Transform>();
        private int npcCount = 0;
        private List<Npc> npcs = new List<Npc>();

        private NpcGroup group;
        void Start() {
           
        }

        public List<Transform> GetFollowTargets() {
            return followTargets;
        }

        public List<Npc> GetNpcList() {
            return npcs;
        }

        public void AddNpc(Npc npc) {
            npcs.Add(npc);
            npcCount++;
        }

        public Vector3 RandomVector3(Vector3 position) {
            return new Vector3(
                    position.x + UnityEngine.Random.Range(-formationRandomRange, formationRandomRange),
                    position.y,
                    position.z + UnityEngine.Random.Range(-formationRandomRange, formationRandomRange)
                    );
        }

        public void SetLeader(Npc leader) {
            this.leader = leader;
        }
        
        public void InitFormation() {
            if (formation == Formation.Random) {
                CreateRandomFormation();
            }
        }

        private void CreateRandomFormation() {
            for (int i=0;i< npcCount;i++) {
                CreateRandomPoint();
            }
        }

        private void CreateRandomPoint() {
            for (int i=0;i<100;i++) {
                Vector3 point = RandomVector3(leader.transform.position);

                bool conflict = followTargets.Any(random => Vector3.Distance(random.transform.position, point) <= 2f);
                if (!conflict) {
                    GameObject go = CreateFormationPoint();
                    go.transform.position = point;
                    go.transform.parent = leader.transform;
                    followTargets.Add(go.transform);
                    break;
                }
            }
        }

        private GameObject CreateFormationPoint() {
            if (showFormationPoint) {
                GameObject go = GameObject.CreatePrimitive(PrimitiveType.Sphere);
                Destroy(go.GetComponent<Collider>());
                return go;
            } else {
                return new GameObject();
            }
        }

        public bool IsLeader(Npc npc) {
            return (leader == npc);
        }

        public bool HasLeader() {
            return (leader != null);
        }
        
    }
}
