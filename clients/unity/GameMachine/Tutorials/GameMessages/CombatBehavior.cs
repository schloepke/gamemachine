using UnityEngine;
using System.Collections;
using GameMachine;
using Attack = GameMachine.Messages.Attack;
using GameMessage = GameMachine.Messages.GameMessage;
using GameMachine.Core;

namespace GameMachine.Tutorials.GameMessages
{
	public class CombatBehavior : MonoBehaviour, GameMachine.Core.Behavior
	{

		private MessageSystem messageSystem = MessageSystem.Instance;

		public void OnMessage (object message)
		{
			if (message is Attack) {
				Attack attack = (Attack)message;
				Debug.Log ("attack.damage " + attack.damage);
			}
		}
		// Use this for initialization
		void Start ()
		{
			messageSystem.RegisterBehavior (this, "Attack");
			InvokeRepeating ("AttackTarget", 1.0f, 1.0f);
		}

		public void AttackTarget ()
		{
			Attack attack = new Attack ();
			attack.attacker = "me";
			attack.target = "badguy";
			messageSystem.Send (attack);
		}
		// Update is called once per frame
		void Update ()
		{
			//Debug.Log (playerName);
		}
	}
}
