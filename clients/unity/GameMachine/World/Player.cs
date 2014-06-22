using UnityEngine;
using System.Collections;
using KAM3RA;

namespace GameMachine.World
{
    public class Player : Actor
    {
        private float attackCooldown;
        private float lastAttack;

        protected override void Update()
        {
            base.Update();

            if (Time.time - lastAttack < 1.0f)
            {
                State = "Attack";
            }
            if (Input.GetKeyDown("1"))
            {
                MeleeAttack();
            }
            if (Input.GetKeyDown("2"))
            {
                AoeAttack();
            }
        }
        protected override void Start()
        {
            base.Start();
            attackCooldown = 1.5f;
            lastAttack = Time.time;
        }
        
                
        private void SendAttackMessage(string targetId, string combatAbility)
        {
            Attack attack = new Attack();
            attack.attacker = nameTag;
            attack.target = targetId;
            attack.combat_ability = combatAbility;
            attack.Send();
        }

        private void AoeAttack()
        {
            lastAttack = Time.time;
            State = "Attack";
            SendAttackMessage("aoe", "aoe");
        }

        private void MeleeAttack()
        {
            lastAttack = Time.time;
            State = "Attack";
            int NPCLayer = 8; 
            int NPCMask = 1 << NPCLayer; 
            
            Debug.Log("Attacking! from " + transform.position.ToString());
            //RaycastHit hit = new RaycastHit();
            Vector3 forward = transform.TransformDirection(Vector3.forward);
            
            RaycastHit[] hits = Physics.SphereCastAll(transform.position + (Vector3.back * 5.0f), 5f, forward, 10f, NPCMask);
            foreach (RaycastHit hit in hits)
            {
                Debug.Log("hit " + hit.collider.gameObject.name);
                SendAttackMessage(hit.collider.gameObject.name, "melee");
            }
            //if (Physics.SphereCast(transform.position - (Vector3.back * 4.0f), 4f, forward, out hit, 4f, NPCMask))
            //{
            //    Debug.Log("hit " + hit.collider.gameObject.name);
            //    SendAttackMessage();
            //}
            
            
            
            
            if (Time.time - lastAttack >= attackCooldown)
            {
                lastAttack = Time.time;
            }
        }
    }
}