using UnityEngine;
using System.Collections;
using System.Collections.Generic;


namespace GameMachine {
    namespace Common {
        public class AnimationController : MonoBehaviour {

            private Animator animator;
            private AnimatorStateInfo combatStateInfo;
            private AnimatorStateInfo baseStateInfo;

            public static Dictionary<int, string> ids = new Dictionary<int, string>();
            public static AnimationController instance;

            public static int crouchIdleId = Animator.StringToHash("Base Layer.CrouchIdle");
            public static int crouchLeftId = Animator.StringToHash("Base Layer.CrouchLeft");
            public static int crouchRightId = Animator.StringToHash("Base Layer.CrouchRight");
            public static int crouchWalkId = Animator.StringToHash("Base Layer.CrouchWalk");
            public static int crouchBackId = Animator.StringToHash("Base Layer.CrouchBack");

            public static int runId = Animator.StringToHash("Base Layer.Run");
            public static int runLeftId = Animator.StringToHash("Base Layer.RunLeft");
            public static int runRightId = Animator.StringToHash("Base Layer.RunRight");
            public static int runJumpId = Animator.StringToHash("Base Layer.RunJump");
            public static int runBackId = Animator.StringToHash("Base Layer.RunBack");

            public static int idleLeftId = Animator.StringToHash("Base Layer.IdleLeft");
            public static int idleRightId = Animator.StringToHash("Base Layer.IdleRight");
            public static int walkId = Animator.StringToHash("Base Layer.Walk");
            public static int walkBackId = Animator.StringToHash("Base Layer.WalkBack");
            public static int idleId = Animator.StringToHash("Base Layer.Idle");
            public static int idleCombatId = Animator.StringToHash("Base Layer.IdleCombat");

            public static int swimId = Animator.StringToHash("Base Layer.Swim");
            public static int swimIdleId = Animator.StringToHash("Base Layer.SwimIdle");
            public static int dodgeRollLeftId = Animator.StringToHash("Base Layer.DodgeRollLeft");


            public static int mineId = Animator.StringToHash("Base Layer.Mine");
            public static int dieId = Animator.StringToHash("Base Layer.Die");

            public static int offId = Animator.StringToHash("Combat.Off");
            public static int bowAttack1Id = Animator.StringToHash("Base Layer.BowAttack1");
            public static int staffAttack1Id = Animator.StringToHash("Combat.StaffAttack1");
            public static int staffIdleId = Animator.StringToHash("Combat.StaffIdle");
            public static int sword2hAttack1Id = Animator.StringToHash("Combat.Sword2hAttack1");
            public static int sword2hIdleId = Animator.StringToHash("Combat.Sword2hIdle");
            public static int sword1hAttack1Id = Animator.StringToHash("Combat.Sword1hAttack1");
            public static int sword1hIdleId = Animator.StringToHash("Combat.Sword1hIdle");

            public static int inAttackId = Animator.StringToHash("Combat.InAttack");
            public static int inCombatId = Animator.StringToHash("Combat.InCombat");
            public static int weaponTypeId = Animator.StringToHash("Combat.WeaponType");

            private int currentHashId = idleId;
            private int requestedId = idleId;
            private float lastRequest;
            private bool playing = false;

            void Awake() {
                instance = this;
            }

            public void OnAnimatorMove() {
            }
            
            // Update is called once per frame
            void Update() {
                if (playing) {
                    return;
                }

                if (animator == null) {
                    animator = GetComponent<Animator>();
                    if (animator == null) {
                        return;
                    }
                }
                combatStateInfo = animator.GetCurrentAnimatorStateInfo(1);
                baseStateInfo = animator.GetCurrentAnimatorStateInfo(0);

                SetAnimation(requestedId);
            }

            public void SetAnimation(int triggerId, float duration) {
                playing = true;
                SetAnimation(triggerId);
                Invoke("EndAnimation", duration);
            }

            private void EndAnimation() {
                playing = false;
            }

            public void RequestAnimation(int triggerId) {
                if (Time.time - lastRequest < 0.100) {
                    return;
                }
                requestedId = triggerId;
                lastRequest = Time.time;
            }
            
            public void SetAnimation(int triggerId) {
                if (baseStateInfo.fullPathHash == triggerId || currentHashId == triggerId) {
                    return;
                }

                currentHashId = triggerId;
                
                if (triggerId == offId) {
                    animator.SetTrigger("BaseOff");
                } else if (triggerId == mineId) {
                    animator.SetTrigger("Mine");
                } else if (triggerId == dieId) {
                    animator.SetTrigger("Die");
                } else if (triggerId == idleCombatId) {
                    animator.SetTrigger("IdleCombat");
                } else if (triggerId == idleId) {
                    animator.SetTrigger("Idle");
                } else if (triggerId == idleLeftId) {
                    animator.SetTrigger("IdleLeft");
                } else if (triggerId == idleRightId) {
                    animator.SetTrigger("IdleRight");
                } else if (triggerId == runId) {
                    animator.SetTrigger("Run");
                } else if (triggerId == walkId) {
                    animator.SetTrigger("Walk");
                } else if (triggerId == walkBackId) {
                    animator.SetTrigger("WalkBack");
                } else if (triggerId == runBackId) {
                    animator.SetTrigger("RunBack");
                } else if (triggerId == runLeftId) {
                    animator.SetTrigger("RunLeft");
                } else if (triggerId == runRightId) {
                    animator.SetTrigger("RunRight");
                } else if (triggerId == runJumpId) {
                    animator.SetTrigger("RunJump");
                } else if (triggerId == dodgeRollLeftId) {
                    animator.SetTrigger("DodgeRollLeft");
                } else if (triggerId == swimId) {
                    animator.SetTrigger("Swim");
                } else if (triggerId == swimIdleId) {
                    animator.SetTrigger("SwimIdle");
                } else if (triggerId == bowAttack1Id) {
                    animator.SetTrigger("BowAttack1");
                } else if (triggerId == crouchIdleId) {
                    animator.SetTrigger("CrouchIdle");
                } else if (triggerId == crouchLeftId) {
                    animator.SetTrigger("CrouchLeft");
                } else if (triggerId == crouchRightId) {
                    animator.SetTrigger("CrouchRight");
                } else if (triggerId == crouchWalkId) {
                    animator.SetTrigger("CrouchWalk");
                } else if (triggerId == crouchBackId) {
                    animator.SetTrigger("CrouchBack");
                }
            }

        }
    }
}
