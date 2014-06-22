using UnityEngine;
using System.Collections;
using GameMachine.World;

public class Combat : MonoBehaviour
{

    private float attackCooldown;
    private float lastAttack;
    GameMachine.World.Player playerComponent;

    // Use this for initialization
    void Start()
    {
        playerComponent = this.gameObject.GetComponent<GameMachine.World.Player>();
        attackCooldown = 1.5f;
        lastAttack = Time.time;
    }
	
    // Update is called once per frame
    void Update()
    {
        return;
        if (Input.GetKeyDown("1"))
        {
            AttackAllInRange();
        }
    }

    private void SendAttackMessage(string targetId)
    {
        Attack attack = new Attack();
        attack.attacker = playerComponent.nameTag;
        attack.target = targetId;
        attack.combat_ability = "melee";
        attack.Send();
    }
    private void AttackAllInRange()
    {
        playerComponent.State = "Attack";
        int NPCLayer = 8; 
        int NPCMask = 1 << NPCLayer; 

        Debug.Log("Attacking! from " + transform.position.ToString());
        //RaycastHit hit = new RaycastHit();
        Vector3 forward = transform.TransformDirection(Vector3.forward);

        RaycastHit[] hits = Physics.SphereCastAll(transform.position + (Vector3.back * 5.0f), 5f, forward, 10f, NPCMask);
        foreach (RaycastHit hit in hits)
        {
            Debug.Log("hit " + hit.collider.gameObject.name);
            SendAttackMessage(hit.collider.gameObject.name);
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
