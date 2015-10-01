using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System;

namespace GameMachine {
    namespace Common {
        public class GameEntityController : MonoBehaviour, IGameEntityController {

            public bool onBoat = false;
            public bool controllingBoat = false;
            private RaycastHit boatHit;
            private Vector3 platformCurrent;
            private Vector3 platformPrevious;
            public Transform activePlatform;
            private Vector3 lastPlatformVelocity;
            private Vector3 activeLocalPlatformPoint;
            private Vector3 activeGlobalPlatformPoint;
            private Quaternion activeLocalPlatformRotation;
            private Quaternion activeGlobalPlatformRotation;
            public IGameEntity gameEntity;
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

            public bool toggleRun = true;

            public bool running = true;
            private bool grounded = false;
            public float speed = 0;
            private bool autorun = false;
            private Vector3 velocity = Vector3.zero;
            private float inputX = 0;
            private float inputY = 0;
            private float rotation = 0;
            public Vector3 lastPosition = Vector3.zero;
            private float animationSpeed = 1;
            public float moveSpeed = 0;

            private CharacterController characterController;
            private IGameEntityInput inputController;
            private int jumpPhase = 0;
            private float moveY = 0;
            private Vector3 moveTo;

            public float swimLevel;
            public bool swimming = false;
            private int swimDirection = 0;
            private float swimVelocity = 0f;
            public bool underWater = false;
            private Camera gameCam;
            private bool initialized = false;
            private AnimationController animationController;

            void Start() {

                gameEntity = gameObject.GetComponent<IGameEntity>() as IGameEntity;
                inputController = gameObject.GetComponent<IGameEntityInput>() as IGameEntityInput;

                characterController = GetComponent<CharacterController>();

                characterController.slopeLimit = slopeLimit;

                animationController = transform.GetComponentInChildren<AnimationController>();

                swimLevel = Settings.Instance().waterLevel - 1.5f;
                initialized = true;
            }
            
            private void SetAnimation(int triggerId) {
                animationController.RequestAnimation(triggerId);
            }


            void ServerMove(float y, float spd) {
                float speed = spd;
                moveTo = gameEntity.GetServerLocation() - transform.position;
                moveTo = moveTo * speed;
                moveTo.y = -antiBump;
                moveY -= gravity * Time.deltaTime;
                moveTo.y = moveY;

                if (moveTo.magnitude > 0.1f) {
                    characterController.Move(moveTo * Time.deltaTime);
                }
            }

            public void ManualUpdate() {
               
                // If on the ground, allow jumping
                if (grounded) {
                    if (inputController.GetBool(GMKeyCode.Jumping)) {
                        jumpPhase = 1;
                        velocity.y = jumpPower;
                        grounded = false;
                    }
                }

                if (inputController.GetBool(GMKeyCode.Running)) {
                    running = true;
                } else {
                    running = false;
                }
                
                if (!gameEntity.IsPlayer()) {
                    if (gameEntity.IsOtherPlayer()) {
                        transform.rotation = Quaternion.Euler(0, inputController.GetFloat(GMKeyCode.Heading), 0);
                    } else if (gameEntity.IsNpc()) {
                        Vector3 dir = (gameEntity.GetServerLocation() - transform.position).normalized;
                        // Zero out the y component of your forward vector to only get the direction in the X,Z plane
                        dir.y = 0;
                        if (dir != Vector3.zero) {
                            float headingAngle = Quaternion.LookRotation(dir).eulerAngles.y;
                            transform.rotation = Quaternion.Euler(0, headingAngle, 0);
                        }
                        
                    }
                   
                }
                Stage2();
            }

            void Stage2() {
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

                animationSpeed = 1;
                float input_modifier = (inputX != 0.0f && inputY != 0.0f) ? 0.7071f : 1.0f;

                inputX = inputController.GetFloat(GMKeyCode.Haxis);
                inputY = inputController.GetFloat(GMKeyCode.Vaxis);

                // If autorun is enabled, set Y input to always be 1 until user uses the Y axis
                if (autorun) {
                    if (inputY == 0) {
                        inputY = 1;
                    } else {
                        autorun = false;
                    }
                }

                // If the user is not holding right-mouse button, rotate the player with the X axis instead of strafing
                if (!inputController.GetBool(GMKeyCode.MouseRight) && inputX != 0) {
                    transform.Rotate(new Vector3(0, inputX * (turnSpeed / 2.0f), 0));
                    rotation = inputX;
                    inputX = 0;

                } else {
                    rotation = 0;
                }

                // Movement direction and speed
                if (swimming) {
                    speed = swimSpeed;
                } else {
                    if (inputY < 0) {
                        speed = backSpeed;
                    } else {
                        if (running) {
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
                    if (!Physics.Raycast(transform.position, -Vector3.up, 0.2f)) {
                        grounded = false;
                    }
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
                    SetAnimation(AnimationController.runJumpId);
                    return;
                }

                if (moveSpeed > 0.01) {
                    if (gameEntity.IsNpc()) {
                        SetAnimation(AnimationController.runId);
                    } else if (controllingBoat) {
                        SetAnimation(AnimationController.idleId);
                    } else if (swimming) {
                        SetAnimation(AnimationController.swimId);
                    } else if (running) {

                        if ((inputController.GetBool(GMKeyCode.MouseRight)) && inputX != 0) {
                            if (inputX < 0) {
                                if (inputY > 0) {
                                    SetAnimation(AnimationController.runLeftId);
                                } else {
                                    SetAnimation(AnimationController.runLeftId);
                                }
                            } else {
                                if (inputY > 0) {
                                    SetAnimation(AnimationController.runRightId);
                                } else {
                                    SetAnimation(AnimationController.runRightId);
                                }
                            }
                        } else {
                            if (inputY > 0) {
                                SetAnimation(AnimationController.runId);
                            } else if (inputY < 0) {
                                SetAnimation(AnimationController.runBackId);
                            }

                        }

                        animationSpeed = 1;
                    } else if (inputController.GetBool(GMKeyCode.Hidden)) {
                        if ((inputController.GetBool(GMKeyCode.MouseRight)) && inputX != 0) {
                            if (inputX < 0) {
                                if (inputY > 0) {
                                    SetAnimation(AnimationController.crouchLeftId);
                                } else if (inputY < 0) {
                                    SetAnimation(AnimationController.crouchRightId);
                                } else {
                                    SetAnimation(AnimationController.crouchLeftId);
                                }
                            } else {
                                if (inputY > 0) {
                                    SetAnimation(AnimationController.crouchRightId);
                                } else if (inputY < 0) {
                                    SetAnimation(AnimationController.crouchLeftId);
                                } else {
                                    SetAnimation(AnimationController.crouchRightId);
                                }
                            }
                        } else {
                            if (inputY > 0) {
                                SetAnimation(AnimationController.crouchWalkId);
                            } else {
                                SetAnimation(AnimationController.crouchBackId);
                            }
                        }

                        animationSpeed = moveSpeed * 13 + 1;

                        if (inputY < 0) {
                            animationSpeed = -animationSpeed;
                        }
                    } else {
                        if ((inputController.GetBool(GMKeyCode.MouseRight)) && inputX != 0) {
                            if (inputX < 0) {
                                if (inputY > 0) {
                                    SetAnimation(AnimationController.idleLeftId);
                                } else if (inputY < 0) {
                                    SetAnimation(AnimationController.idleRightId);
                                } else {
                                    SetAnimation(AnimationController.idleLeftId);
                                }
                            } else {
                                if (inputY > 0) {
                                    SetAnimation(AnimationController.idleRightId);
                                } else if (inputY < 0) {
                                    SetAnimation(AnimationController.idleLeftId);
                                } else {
                                    SetAnimation(AnimationController.idleRightId);
                                }
                            }
                        } else {
                            if (inputY > 0) {
                                SetAnimation(AnimationController.walkId);
                            } else {
                                SetAnimation(AnimationController.walkBackId);
                            }
                        }

                        animationSpeed = moveSpeed * 13 + 1;

                        if (inputY < 0) {
                            animationSpeed = -animationSpeed;
                        }
                    }
                } else {
                    if (swimming) {
                        SetAnimation(AnimationController.swimIdleId);
                    } else {
                        if (inputController.GetBool(GMKeyCode.Hidden)) {
                            SetAnimation(AnimationController.crouchIdleId);
                        } else {
                            SetAnimation(AnimationController.idleId);
                        }

                    }
                }
            }

            public bool OnBoat() {
                Physics.Raycast(transform.position, -Vector3.up, out boatHit, 10f);

                if (boatHit.collider != null && boatHit.collider.gameObject.name == "boat_model") {
                    activePlatform = boatHit.collider.transform;
                    platformCurrent = boatHit.point;
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

                    Vector3 lastPlatformVelocity = moveDistance / Time.deltaTime;

                    // If you want to support moving platform rotation as well:
                    Quaternion newGlobalPlatformRotation = activePlatform.rotation * activeLocalPlatformRotation;
                    Quaternion rotationDiff = newGlobalPlatformRotation * Quaternion.Inverse(activeGlobalPlatformRotation);

                    // Prevent rotation of the local up vector
                    rotationDiff = Quaternion.FromToRotation(rotationDiff * transform.up, transform.up) * rotationDiff;

                    transform.rotation = rotationDiff * transform.rotation;
                    //_t.rotation = Quaternion.Euler (0, _t.rotation.y, 0);
                } else {
                    lastPlatformVelocity = Vector3.zero;
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

            public float GetSwimSpeed() {
                return swimSpeed;
            }

            public float GetRunSpeed() {
                return runSpeed;
            }

            public float GetWalkSpeed() {
                return walkSpeed;
            }

            public void SetSwimSpeed(float speed) {
                swimSpeed = speed;
            }

            public void SetRunSpeed(float speed) {
                runSpeed = speed;
            }

            public void SetWalkSpeed(float speed) {
                walkSpeed = speed;
            }
        }
    }
}