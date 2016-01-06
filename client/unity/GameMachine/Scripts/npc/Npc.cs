using UnityEngine;
using GameMachine.Common;
using System.Collections.Generic;
using System.Linq;
using System;

namespace GameMachine {
    public class Npc : MonoBehaviour {

        public enum Action {
            Waypoint,
            Attack,
            Guard
        }

        public Action action = Action.Waypoint;
        public IGameEntity gameEntity;
        public NpcEntityController controller;
        public int order;
        public Transform target;
        public WaypointRoute route;
        public NpcGroup group;

        private NpcDef npcDef;
        private Queue<Vector3> navmeshPath = new Queue<Vector3>();
        private int currentWaypointIndex = -1;
        private CombatAi combatAi;
        private float targetDistance;
        private bool stopped = false;

        void Start() {
            gameObject.layer = LayerMask.NameToLayer(Settings.Instance().gameEntityTag);

            controller = gameObject.GetComponent<NpcEntityController>();
            controller.agent = controller.gameObject.AddComponent<NavMeshAgent>();
            controller.agent.updateRotation = false;
            controller.agent.angularSpeed = 600f;
            controller.useNavmesh = true;

            Type type = Type.GetType("LandRush.DefaultCombatAi");
            if (type != null) {
                combatAi = gameObject.AddComponent(type) as CombatAi;
            }

            InvokeRepeating("RunAi", 0.01f, NpcManager.instance.npcUpdateInterval);
        }

        void Update() {
            if (target == null) {
                controller.SetAgentTarget(transform.position);
            } else {
                if (action == Action.Attack) {
                    float distanceToTarget = Vector3.Distance(transform.position, new Vector3(target.position.x, transform.position.y, target.position.z));
                    if (distanceToTarget < 3f || (distanceToTarget <= targetDistance && combatAi.TargetInRange())) {
                        if (!stopped) {
                            controller.agent.Stop();
                            stopped = true;
                        }

                    } else {
                        if (stopped) {
                            controller.agent.Resume();
                            stopped = false;
                        }

                    }
                } else {
                    if (stopped) {
                        controller.agent.Resume();
                        stopped = false;
                    }
                }
                controller.SetAgentTarget(target.position);
            }
        }

        public void SetNpcDef(NpcDef npcDef) {
            this.npcDef = npcDef;
        }

        public NpcDef GetNpcDef() {
            return npcDef;
        }

        public void SetAttackTarget(Transform attackTarget, float targetDistance) {
            target = attackTarget;
            action = Action.Attack;
            controller.SetPlayerSpeed(1f);
            this.targetDistance = targetDistance;
            controller.SetTargetTransform(target);
        }

        public void RemoveAttackTarget() {
            if (action != Action.Attack) {
                return;
            }

            target = null;
            action = Action.Waypoint;
            controller.SetTargetTransform(target);
            controller.SetPlayerSpeed(0f);
        }
        
        public void DeathStatusChange(int status) {
            if (status == 0) {
                Debug.Log("Revived");
                
            } else {
                target = null;
            }
        }

        private void RunAi() {
            if (gameEntity.IsDead()) {
                return;
            }
            CalculateTarget();
        }

        private void CalculateTarget() {
            if (action == Action.Attack) {
                controller.updateAgentDestination = true;
                return;
            }

           
            controller.SetPlayerSpeed(0f);

            if (group != null) {
                if (group.IsLeader(this)) {
                    if (group.route != null) {
                        route = group.route;
                        SetTargetFromWaypoint();
                    }
                } else if (group.leader != null) {
                    if (group.formation == NpcGroup.Formation.SingleFile) {
                        target = group.GetNpcList()[order - 1].transform;
                    } else {
                        if (group.GetFollowTargets().ElementAtOrDefault(order) != null) {
                            target = group.GetFollowTargets()[order];
                        } else {
                            target = group.leader.transform;
                        }

                    }
                }
            } else {
                if (route != null) {
                    SetTargetFromWaypoint();
                }
            }
            controller.updateAgentDestination = true;
        }

        private void SetTargetFromWaypoint() {
            Waypoint waypoint = route.CurrentWaypoint();

            if (target == null || currentWaypointIndex != route.CurrentWaypointIndex()) {
                currentWaypointIndex = route.CurrentWaypointIndex();
                target = waypoint.transform;
            }
            if (!controller.AtEndOfPath()) {
                return;
            }
            Debug.Log("Next waypoint");
            target = route.NextWaypoint().transform;
            currentWaypointIndex = route.CurrentWaypointIndex();
        }

        private bool SetPath(Vector3 pos) {
            NavMeshHit hit;
            if (NavMesh.SamplePosition(transform.position, out hit, 20.0f, NavMesh.AllAreas)) {
                Vector3 start = hit.position;
                if (NavMesh.SamplePosition(pos, out hit, 20.0f, NavMesh.AllAreas)) {
                    NavMeshPath path = new NavMeshPath();
                    if (NavMesh.CalculatePath(start, hit.position, NavMesh.AllAreas, path)) {
                        navmeshPath = new Queue<Vector3>();
                        foreach (Vector3 vec in path.corners) {
                            navmeshPath.Enqueue(vec);
                        }
                        //Debug.Log("Found path segments " + navmeshPath.Count);
                        return true;
                    }
                }
            }
            Debug.Log("Unable to find path");
            return false;
        }
    }
}
