using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using GameMachine.Animation;

namespace GameMachine {
    namespace Common {
        public class NpcEntityController : MonoBehaviour, GameEntityController {

            public bool onBoat = false;
            public bool controllingBoat = false;
            private RaycastHit boatHit;
            private Vector3 platformPrevious;
            public Transform activePlatform;
            private Vector3 activeLocalPlatformPoint;
            private Vector3 activeGlobalPlatformPoint;
            private Quaternion activeLocalPlatformRotation;
            private Quaternion activeGlobalPlatformRotation;
            public IGameEntity gameEntity;
            public float swimJumpSpeed = 0.5f;
            public float playerSpeed = 1f;
            public float walkSpeed = 3;
            public float swimSpeed = 3;
            public float runSpeed = 6;
            public float backSpeed = 3;
            public float turnSpeed = 6;
            public float jumpPower = 12;
            public float gravity = 20;
            public float slopeLimit = 55;
            public float antiBump = 0.75f;

            public float speed = 0;
            private Vector3 velocity = Vector3.zero;

            public Vector3 lastPosition = Vector3.zero;
            public float moveSpeed = 0;

            private CharacterController characterController;
            private IGameEntityInput inputController;
            private float moveY = 0;
            private Vector3 moveTo;

            public float swimLevel;
            public bool swimming = false;
            private Camera gameCam;
            private bool initialized = false;
            private DefaultAnimationController animationController;
            private ITerrainCollision terrainCollisionHandler;
            private Transform transformTarget;
            public NavMeshAgent agent;
            public bool useNavmesh = false;
            public float pathEndThreshold = 0.1f;
            private bool hasPath = false;
            public bool stopped = false;
            public Transform targetTransform;
            public bool updateAgentDestination = false;
            public Vector3 agentTarget;
            public Vector3 serverTarget;

            void Start() {

                gameEntity = gameObject.GetComponent<IGameEntity>() as IGameEntity;
                inputController = gameObject.GetComponent<IGameEntityInput>() as IGameEntityInput;

                terrainCollisionHandler = gameObject.GetComponent<ITerrainCollision>() as ITerrainCollision;
                if (terrainCollisionHandler != null) {
                    terrainCollisionHandler.Initialize();
                }

                characterController = GetComponent<CharacterController>();

                characterController.slopeLimit = slopeLimit;

                animationController = transform.GetComponentInChildren<DefaultAnimationController>();

                swimLevel = Settings.Instance().waterLevel - 1.5f;
                initialized = true;
            }

            public bool AtEndOfPath() {
                hasPath |= agent.hasPath;
                if (hasPath && agent.remainingDistance <= agent.stoppingDistance + pathEndThreshold) {
                    // Arrived
                    hasPath = false;
                    return true;
                }
                return false;
            }

            public void SetAgentTarget(Vector3 agentTarget) {
                this.agentTarget = agentTarget;
            }

            public void SetTargetTransform(Transform targetTransform) {
                this.targetTransform = targetTransform;
            }

            private void SetAnimation(AnimationName name) {
                animationController.SetAnimation(name);
            }

            void ServerMove(float y, float spd, Vector3 targetPosition) {
                float speed = spd;
                moveTo = targetPosition - transform.position;
                moveTo = moveTo * speed;
                moveTo.y = -antiBump;
                moveY -= gravity * Time.deltaTime;
                moveTo.y = moveY;

                if (moveTo.magnitude > 0.01f) {
                    characterController.Move(moveTo * Time.deltaTime);
                }
            }

            public void ManualUpdate() {
                if (useNavmesh) {
                    AgentUpdate();
                } else {
                    Vector3 targetPosition = gameEntity.GetTargetPosition();
                    serverTarget = targetPosition;
                    RemoteUpdate(targetPosition);
                }
            }

            private void AgentUpdate() {

                if (gameEntity.IsDead()) {
                    SetAnimation(AnimationName.Dead);
                    return;
                }

                if (agentTarget == Vector3.zero) {
                    return;
                }




                if (playerSpeed == 0f) {
                    agent.speed = walkSpeed;
                    agent.acceleration = walkSpeed + 1f;
                    speed = walkSpeed;
                } else {
                    agent.speed = runSpeed;
                    agent.acceleration = runSpeed + 1f;
                    speed = runSpeed;
                }

                agent.acceleration = speed;

                moveSpeed = (transform.position - lastPosition).magnitude;
                lastPosition = transform.position;

                if (moveSpeed > 0.01f) {
                    agent.updateRotation = true;
                    SetAnimation(DefaultAnimationController.GetAnimationName(Vector3.forward, playerSpeed));
                } else {
                    if (targetTransform != null) {
                        agent.updateRotation = false;
                        Vector3 dir = (targetTransform.position - transform.position);
                        RotateTowards(dir);
                    } else {
                        agent.updateRotation = true;
                    }
                    SetAnimation(AnimationName.Idle);
                }



                if (updateAgentDestination) {
                    agent.SetDestination(agentTarget);
                    updateAgentDestination = false;
                }
            }

            private void RemoteUpdate(Vector3 targetPosition) {
                playerSpeed = inputController.GetFloat(GMKeyCode.PlayerSpeed);
                float distanceToTarget = Vector3.Distance(new Vector3(transform.position.x, targetPosition.y, transform.position.z), targetPosition);

                if (terrainCollisionHandler != null) {
                    terrainCollisionHandler.CollisionCheck();
                }

                if (gameEntity.IsDead()) {
                    velocity.y -= gravity * Time.deltaTime;
                    SetAnimation(AnimationName.Dead);
                    return;
                }



                Quaternion wanted = Quaternion.Euler(0, inputController.GetFloat(GMKeyCode.Heading), 0);
                transform.rotation = Quaternion.Slerp(transform.rotation, wanted, Time.deltaTime * speed);
                onBoat = false;



                if (inputController.GetFloat(GMKeyCode.PlayerSpeed) == 1f) {
                    speed = runSpeed;
                } else {
                    speed = walkSpeed;
                }

                if (lastPosition == Vector3.zero) {
                    lastPosition = transform.position;
                }
                moveSpeed = (transform.position - lastPosition).magnitude;
                lastPosition = transform.position;


                UpdateAnimations(distanceToTarget);


                velocity.y -= gravity * Time.deltaTime;

                if (distanceToTarget >= characterController.radius * 2) {
                    ServerMove(velocity.y, speed, targetPosition);
                }

            }

            private void UpdateAnimations(float distanceToTarget) {
                if (moveSpeed > 0.01f) {
                    if (distanceToTarget <= characterController.radius * 2) {
                        SetAnimation(AnimationName.Idle);
                    } else {
                        SetAnimation(DefaultAnimationController.GetAnimationName(Vector3.forward, playerSpeed));
                    }
                } else {
                    if (inputController.GetBool(GMKeyCode.Hidden)) {
                        SetAnimation(AnimationName.CrouchIdle);
                    } else {
                        SetAnimation(AnimationName.Idle);
                    }
                }
            }

            protected void RotateTowards(Vector3 dir) {

                if (dir == Vector3.zero)
                    return;

                Quaternion rot = transform.rotation;
                Quaternion toTarget = Quaternion.LookRotation(dir);

                rot = Quaternion.Slerp(rot, toTarget, 5f * Time.deltaTime);
                Vector3 euler = rot.eulerAngles;
                euler.z = 0;
                euler.x = 0;
                rot = Quaternion.Euler(euler);

                transform.rotation = rot;
            }

            public bool OnBoat() {
                Physics.Raycast(transform.position, -Vector3.up, out boatHit, 10f);

                if (boatHit.collider != null && boatHit.collider.gameObject.name == "boat_model") {
                    activePlatform = boatHit.collider.transform;
                    return true;
                } else {
                    activePlatform = null;
                    activeGlobalPlatformPoint = Vector3.zero;
                    return false;
                }
            }

            void PlatformPostCalc() {
                activeGlobalPlatformPoint = transform.position;
                activeLocalPlatformPoint = activePlatform.InverseTransformPoint(activeGlobalPlatformPoint);

                // If you want to support moving platform rotation as well:
                activeGlobalPlatformRotation = transform.rotation;
                activeLocalPlatformRotation = Quaternion.Inverse(activePlatform.rotation) * transform.rotation;
            }

            void MoveOnPlatform() {
                if (activePlatform != null && activeGlobalPlatformPoint != Vector3.zero) {
                    Vector3 newGlobalPlatformPoint = activePlatform.TransformPoint(activeLocalPlatformPoint);
                    Vector3 moveDistance = (newGlobalPlatformPoint - activeGlobalPlatformPoint);

                    if (moveDistance != Vector3.zero) {
                        characterController.Move(moveDistance);
                    }


                    // If you want to support moving platform rotation as well:
                    Quaternion newGlobalPlatformRotation = activePlatform.rotation * activeLocalPlatformRotation;
                    Quaternion rotationDiff = newGlobalPlatformRotation * Quaternion.Inverse(activeGlobalPlatformRotation);

                    // Prevent rotation of the local up vector
                    rotationDiff = Quaternion.FromToRotation(rotationDiff * transform.up, transform.up) * rotationDiff;

                    transform.rotation = rotationDiff * transform.rotation;
                } else {
                }
            }

            public bool IsInitialized() {
                return initialized;
            }

            public float GetPlayerSpeed() {
                return playerSpeed;
            }

            public void SetPlayerSpeed(float speed) {
                playerSpeed = speed;
            }

            public void SetNetworkFields(NetworkFields networkFields) {
                Vector3 forward = transform.forward;
                forward.y = 0;
                float heading = Quaternion.LookRotation(forward).eulerAngles.y;

                networkFields.SetPosition(transform.position);
                networkFields.SetFloat(GMKeyCode.PlayerSpeed, GetPlayerSpeed());
                networkFields.SetFloat(GMKeyCode.Heading, heading);

                if (!gameEntity.IsNpc()) {
                    networkFields.SetFloat(GMKeyCode.Vaxis, inputController.GetFloat(GMKeyCode.Vaxis));
                    networkFields.SetFloat(GMKeyCode.Haxis, inputController.GetFloat(GMKeyCode.Haxis));
                    networkFields.SetBool(GMKeyCode.MouseRight, inputController.GetBool(GMKeyCode.MouseRight));
                }
            }
        }
    }
}