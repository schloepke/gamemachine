using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System;

namespace GameMachine {
    namespace Common {
        public class WorldInteraction : MonoBehaviour {

            public static WorldInteraction instance;

            public struct HitResponse {
                public string name;
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
                GameEntity
            }


            public delegate HitResponse CheckHit(RaycastHit hit);
            public CheckHit checkHit;

            public delegate void SetStatus(Status status);
            public SetStatus setStatus;

            private LayerMask hitMask;

            public float maxObjectDistance = 30f;
            public float maxGameEntityDistance = 40f;

            private TargetType currentType;
            private GameObject currentTarget;
            private bool disabled = false;
            private Text label;
            private Slider slider;
            private Canvas canvas;


            private IGameEntity player;
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
                hitMask = Settings.Instance().worldInteractionMask;
            }

            void Start() {
                player = GameEntityManager.GetPlayerEntity();

                canvas = GetComponent<Canvas>();
                label = transform.Find("label").GetComponent<Text>();
                slider = transform.Find("slider").GetComponent<Slider>();
                DisableSlider();
                InvokeRepeating("UpdateTargets", 1f, 0.2f);
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

                UpdateObjects();
                UpdateGameEntities();
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


            private void UpdateObjects() {
                currentTarget = null;
                currentType = TargetType.None;
                RaycastHit hit;
                Vector3 origin = new Vector3(Camera.main.transform.position.x, player.GetTransform().position.y + 2f, Camera.main.transform.position.z);
                if (Physics.Raycast(origin, player.GetTransform().forward, out hit, maxObjectDistance, hitMask)) {
                    if (hit.collider != null && hit.distance <= maxObjectDistance) {

                        foreach (CheckHit part in checkHit.GetInvocationList()) {
                            HitResponse hitResponse = part(hit);
                            if (hitResponse.hit) {
                                currentType = hitResponse.targetType;
                                currentTarget = hit.collider.gameObject;
                                SetSlider(hitResponse.name);
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
                    Vector3 directionToTarget = target.position - player.GetTransform().position;
                    float angle = Vector3.Angle(player.GetTransform().forward, directionToTarget);
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
