using UnityEngine;
using System;
using GameMachine;
using GameMachine.Core;
using GameMachine.Tutorials.GameMessages;
using UnityGameObject = GameMachine.Messages.UnityGameObject;
using UnityGameObjects = GameMachine.Messages.UnityGameObjects;

namespace GameMachine.Tutorials.NetworkObjects
{

	public class Game : MonoBehaviour, GameMachineApp
	{

		void Start ()
		{
			Login.SetGameMachineApp (this);

		}

		public void OnLoginFailure (string error)
		{
			Debug.Log ("Login Failed: " + error);
		}

		public void ConnectionTimeout ()
		{
			Debug.Log ("Connection timed out");
		}
		
		public void ConnectionEstablished ()
		{
			Debug.Log ("Connection established");
		}

		public void OnLoggedIn ()
		{
		}

		
	}
}
