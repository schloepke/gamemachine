using UnityEngine;
using GameMachine;

namespace GameMachine.Core
{
	public class Logger
	{
		public static void Debug (string message)
		{
			UnityEngine.Debug.Log (message);
		}
	}


}
