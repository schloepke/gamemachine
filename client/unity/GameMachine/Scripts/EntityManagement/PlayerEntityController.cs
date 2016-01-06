using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;
using GameMachine.Animation;

namespace GameMachine {
    namespace Common {
        public class PlayerEntityController : MonoBehaviour, GameEntityController {

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
            private float playerSpeed = 1f;
            public float swimJumpSpeed = 0.5f;
            public float walkSpeed = 3;
            public float swimSpeed = 3;
            public float runSpeed = 6;
            public float backSpeed = 3;
            public float turnSpeed = 6;
            public float jumpPower = 12;
            public float gravity = 20;
            public float slopeLimit = 55;
            public float antiBump = 0.75f;

            private bool grounded = false;
            public float speed = 0;
            private Vector3 velocity = Vector3.zero;
            private float inputX = 0;
            private float inputY = 0;
            public Vector3 lastPosition = Vector3.zero;
            public float moveSpeed = 0;

            private CharacterController characterController;
            private IGameEntityInput inputController;
            private float moveY = 0;
            private Vector3 moveTo;

            public float swimLevel;
            public bool swimming = false;
            private int swimDirection = 0;
            private float swimVelocity = 0f;
            public bool underWater = false;
            private Camera gameCam;
            private bool initialized = false;
            private DefaultAnimationController animationController;
            private ITerrainCollision terrainCollisionHandler;

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
            
            private void SetAnimation(AnimationName name) {
                animationController.SetAnimation(name);
            }


            void ServerMove(float y, float spd) {
                float speed = spd;
                moveTo = gameEntity.GetTargetPosition() - transform.position;
                moveTo = moveTo * speed;
                moveTo.y = -antiBump;
                moveY -= gravity * Time.deltaTime;
                moveTo.y = moveY;

                if (moveTo.magnitude > 0.1f) {
                    characterController.Move(moveTo * Time.deltaTime);
                }
            }

            public void ManualUpdate() {
                

                if (gameEntity.IsPlayer()) {
                    if (Input.GetKeyDown(KeyBinds.Binding("RunWalk"))) {
                        if (playerSpeed == 0f) {
                            playerSpeed = 1f;
                        } else {
                            playerSpeed = 0f;
                        }
                    }
                } else {
                    playerSpeed = inputController.GetFloat(GMKeyCode.PlayerSpeed);
                }

                if (terrainCollisionHandler != null) {
                    terrainCollisionHandler.CollisionCheck();
                }

                if (gameEntity.IsDead()) {
                    velocity.y -= gravity * Time.deltaTime;
                    SetAnimation(AnimationName.Dead);
                    return;
                }

                if (gameEntity.MovementDisabled()) {
                    velocity.y -= gravity * Time.deltaTime;
                    SetAnimation(AnimationName.Idle);
                    return;
                }

                // If on the ground, allow jumping
                if (grounded) {
                    if (inputController.GetBool(GMKeyCode.Jumping)) {
                        velocity.y = jumpPower;
                        grounded = false;
                    }
                }

                if (gameEntity.IsOtherPlayer()) {
                    Quaternion wanted = Quaternion.Euler(0, inputController.GetFloat(GMKeyCode.Heading), 0);
                    transform.rotation = Quaternion.Slerp(transform.rotation, wanted, Time.deltaTime * speed);
                }

              
                if (gameEntity.IsPlayer()) {
                    if (OnBoat()) {
                        onBoat = true;
                    } else {
                        onBoat = false;
                    }
                } else {
                    onBoat = false;
                }
               

                if (transform.position.y <= swimLevel) {
                    swimDirection = 0;
                    float angleY = inputController.GetFloat(GMKeyCode.AngleY);

                    if (angleY >= 40f) {
                        swimDirection = 1;
                    }

                    swimming = true;
                    if (Mathf.Abs(transform.position.y - swimLevel) <= 0.1f) {
                        underWater = false;
                    } else {
                        underWater = true;
                        if (angleY <= -30f || Input.GetButton("Jump")) {
                            swimDirection = 2;
                        }

                    }
                    grounded = true;
                } else {
                    swimming = false;
                }

                float input_modifier = (inputX != 0.0f && inputY != 0.0f) ? 0.7071f : 1.0f;

                inputX = inputController.GetFloat(GMKeyCode.Haxis);
                inputY = inputController.GetFloat(GMKeyCode.Vaxis);
               
                // If the user is not holding right-mouse button, rotate the player with the X axis instead of strafing
                if (!inputController.GetBool(GMKeyCode.MouseRight) && inputX != 0) {
                    transform.Rotate(new Vector3(0, inputX * (turnSpeed / 2.0f), 0));
                    inputX = 0;

                }

                // Movement direction and speed
                if (swimming) {
                    speed = swimSpeed;
                } else {
                    if (inputY < 0) {
                        speed = backSpeed;
                    } else {
                        if (inputController.GetFloat(GMKeyCode.PlayerSpeed) == 1f) {
                            speed = runSpeed;
                        } else {
                            speed = walkSpeed;
                        }
                    }
                }
                
                // If on the ground, test to see if still on the ground and apply movement direction
                if (grounded) {
                    if (swimming) {
                        velocity = new Vector3(inputX * input_modifier, -antiBump, inputY * input_modifier);
                        velocity = transform.TransformDirection(velocity) * speed;
                        //_velocity = new Vector3 (_velocity.x, y, _velocity.z);
                    } else {
                        velocity = new Vector3(inputX * input_modifier, -antiBump, inputY * input_modifier);
                        velocity = transform.TransformDirection(velocity) * speed;
                    }

                    if (lastPosition == Vector3.zero) {
                        lastPosition = transform.position;
                    }
                    moveSpeed = (transform.position - lastPosition).magnitude;
                    lastPosition = transform.position;
                }

                // npc's always grounded
                if (gameEntity.IsNpc()) {
                    grounded = true;
                } else {
                    //if (!Physics.Raycast(transform.position, -Vector3.up, 0.2f)) {
                    //    grounded = false;
                    //}
                }

                UpdateAnimations();

                if (swimming) {
                    if (swimDirection == 2) {
                        swimVelocity = 2f;
                    } else if (swimDirection == 1) {
                        swimVelocity = -2f;
                    } else {
                        swimVelocity = 0f;
                    }

                    velocity.y = swimVelocity;

                } else {
                    velocity.y -= gravity * Time.deltaTime;
                }

                if (gameEntity.IsPlayer()) {
                    if (activePlatform == null) {
                        characterController.Move(velocity * Time.deltaTime);
                    } else {
                        if (onBoat) {
                            MoveOnPlatform();
                            if (!controllingBoat) {
                                characterController.Move(velocity * Time.deltaTime);
                            }
                            PlatformPostCalc();
                        }
                    }
                } else {
                    ServerMove(velocity.y, speed);
                }
            }

           

            private void UpdateAnimations() {
                if (!grounded) {
                    SetAnimation(AnimationName.RunJump);
                    return;
                }

                if (moveSpeed > 0.01) {
                    if (gameEntity.IsNpc()) {
                        SetAnimation(AnimationName.Run);
                    } else if (controllingBoat) {
                        SetAnimation(AnimationName.Idle);
                    } else if (swimming) {
                        SetAnimation(AnimationName.Swim);
                    } else {

                        if ((inputController.GetBool(GMKeyCode.MouseRight)) && inputX != 0) {
                            if (inputX < 0) {
                                SetAnimation(DefaultAnimationController.GetAnimationName(Vector3.left,playerSpeed));
                            } else {
                                SetAnimation(DefaultAnimationController.GetAnimationName(Vector3.right, playerSpeed));
                            }
                        } else {
                            if (inputY > 0) {
                                SetAnimation(DefaultAnimationController.GetAnimationName(Vector3.forward, playerSpeed));
                            } else if (inputY < 0) {
                                SetAnimation(DefaultAnimationController.GetAnimationName(Vector3.back, playerSpeed));
                            }

                        }
                    }
                } else {
                    if (swimming) {
                        SetAnimation(AnimationName.SwimIdle);
                    } else {
                        if (inputController.GetBool(GMKeyCode.Hidden)) {
                            SetAnimation(AnimationName.CrouchIdle);
                        } else {
                            if (gameEntity.IsInCombat()) {
                                SetAnimation(AnimationName.IdleCombat);
                            } else {
                                SetAnimation(AnimationName.Idle);
                            }
                            
                        }

                    }
                }
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

            void OnControllerColliderHit(ControllerColliderHit col) {
                // This keeps the player from sticking to walls
                float angle = col.normal.y * 90;

                if (angle < slopeLimit) {
                    if (grounded) {
                        velocity = Vector3.zero;
                    }

                    if (velocity.y > 0) {
                        velocity.y = 0;
                    } else {
                        velocity += new Vector3(col.normal.x, 0, col.normal.z).normalized;
                    }

                    grounded = false;
                } else {
                    grounded = true;
                    velocity.y = 0;
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