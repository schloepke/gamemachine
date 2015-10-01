using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using System;
using System.Linq;

namespace GameMachine {
    namespace Common {
        public class WorldInteraction : MonoBehaviour {

            public static WorldInteraction instance;

             [Serializable]
            public class ViewAngle {
                public float distance;
                public float angle;
            }

            public struct HitResponse {
                public string name;
                public string displayName;
                public TargetType targetType;
                public bool hit;
            }

            public struct Status {
                public TargetType currentType;
                public GameObject currentTarget;
            }

            public enum TargetType {
                None,
                Door,
                Block,
                GameEntity,
                Tree,
                Harvestable
            }


            public delegate HitResponse CheckHit(Collider collider);
            public CheckHit checkHit;

            public delegate void SetStatus(Status status);
            public SetStatus setStatus;

            private LayerMask hitMask;
            public bool useGameEntities = true;
            public float maxObjectDistance = 30f;
            public float maxGameEntityDistance = 40f;
            public List<ViewAngle> viewAngles = new List<ViewAngle>();

            private TargetType currentType;
            private GameObject currentTarget;
            private bool disabled = false;
            private Text label;
            private Canvas canvas;


            public Transform playerTransform;
            private IGameEntity currentGameEntity;

            public static bool HasInstance() {
                return (instance != null);
            }

            public void Disable() {
                disabled = true;
                DisableSlider();
            }

            public void Enable() {
                disabled = false;
            }

            public TargetType CurrentTargetType() {
                return currentType;
            }

            public GameObject CurrentTarget() {
                return currentTarget;
            }


            void Awake() {
                instance = this;
            }

            void Start() {
                SetupViewAngles();                
                
                hitMask = Settings.Instance().worldInteractionMask;

                if (useGameEntities) {
                    playerTransform = GamePlayer.Instance().playerTransform;
                }

                canvas = GetComponent<Canvas>();
                label = transform.Find("label").GetComponent<Text>();
                DisableSlider();
                InvokeRepeating("UpdateTargets", 1f, 0.2f);
            }

            public void SetupViewAngles() {
                if (viewAngles.Count == 0) {
                    ViewAngle viewAngle = new ViewAngle();
                    viewAngle.distance = 20f;
                    viewAngle.angle = 10f;
                    viewAngles.Add(viewAngle);

                    viewAngle = new ViewAngle();
                    viewAngle.distance = 10f;
                    viewAngle.angle = 15f;
                    viewAngles.Add(viewAngle);

                    viewAngle = new ViewAngle();
                    viewAngle.distance = 0f;
                    viewAngle.angle = 50f;
                    viewAngles.Add(viewAngle);
                }
                viewAngles = viewAngles.OrderByDescending(va => va.distance).ToList();
            }

            public void SetSlider(string text) {
                if (!canvas.enabled) {
                    canvas.enabled = true;
                }

                label.text = text;
            }

            public void DisableSlider() {
                if (canvas.enabled) {
                    canvas.enabled = false;
                }
            }
            

            public void UpdateTargets() {
                if (disabled) {
                    return;
                }

                if (playerTransform == null) {
                    Debug.Log("Player transform not set!");
                    return;
                }

                    GetTargetWithRaycast();
                

                if (useGameEntities) {
                    UpdateGameEntities();
                }
                
                if (currentGameEntity == null && currentTarget == null) {
                    DisableSlider();
                }

                if (setStatus != null) {
                    Status status = new Status();
                    status.currentTarget = currentTarget;
                    status.currentType = currentType;
                    setStatus(status);
                }
            }

            private void GetTargetWithRaycast() {
                currentTarget = null;
                currentType = TargetType.None;
                RaycastHit hit;
                float offset;
                if (GamePlayer.Instance().controllerType == GamePlayer.ControllerType.ThirdPerson) {
                    offset = 1f;
                } else {
                    offset = 0f;
                }
                Vector3 origin = new Vector3(Camera.main.transform.position.x, playerTransform.position.y + offset, Camera.main.transform.position.z);
                bool success = Physics.Raycast(Camera.main.transform.position, Camera.main.transform.forward, out hit, 200f, hitMask);
                if (Vector3.Distance(Camera.main.transform.position, hit.point) < Vector3.Distance(Camera.main.transform.position, playerTransform.position)) {
                    success = false;
                }

                if (!success) {
                    origin = new Vector3(playerTransform.position.x, playerTransform.position.y + offset, playerTransform.position.z);
                    success = Physics.Raycast(origin, playerTransform.forward, out hit, 200f, hitMask);
                }

                if (success) {
                    if (hit.collider != null && Vector3.Distance(hit.point,playerTransform.position) <= maxObjectDistance) {

                        foreach (CheckHit part in checkHit.GetInvocationList()) {
                            HitResponse hitResponse = part(hit.collider);
                            if (hitResponse.hit) {
                                currentType = hitResponse.targetType;
                                currentTarget = hit.collider.gameObject;
                                if (string.IsNullOrEmpty(hitResponse.displayName)) {
                                    SetSlider(hitResponse.name);
                                } else {
                                    SetSlider(hitResponse.displayName);
                                }
                            }
                        }

                    }
                }
            }

            
            private void UpdateGameEntities() {
                float bestDistance = 1000f;
                IGameEntity bestTarget = null;
                currentGameEntity = null;

                foreach (IGameEntity gameEntity in GameEntityManager.GetGameEntities()) {
                    Transform target = gameEntity.GetTransform();
                    Vector3 directionToTarget = target.position - playerTransform.position;
                    float angle = Vector3.Angle(playerTransform.forward, directionToTarget);
                    float angle2 = Mathf.Abs(angle);
                    float distance = directionToTarget.magnitude;

                    float mangle;
                    if (distance > 20f) {
                        mangle = 10f;
                    } else if (distance > 10f) {
                        mangle = 15f;
                    } else {
                        mangle = 20f;
                    }


                    if (angle2 <= mangle) {

                        if (distance < maxGameEntityDistance && distance < bestDistance) {
                            bestTarget = gameEntity;
                        }
                    }
                }
                if (bestTarget != null) {
                    currentGameEntity = bestTarget;
                    currentType = TargetType.GameEntity;
                    SetSlider(bestTarget.GetCharacterId());
                }
            }

        }
    }
}
