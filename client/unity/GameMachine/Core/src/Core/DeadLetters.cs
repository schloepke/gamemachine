using UnityEngine;
using GameMachine;

namespace GameMachine.Core
{
	public class DeadLetters : UntypedActor
	{
		public override void OnReceive (object message)
		{
			Debug.Log ("DeadLetters received message: " + message);
		}
	}
}

