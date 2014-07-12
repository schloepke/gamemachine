using UnityEngine;
using System;
using GameMachine;
using GameMachine.Core;
using UnityGameObject = GameMachine.Messages.UnityGameObject;
using UnityGameObjects = GameMachine.Messages.UnityGameObjects;

namespace GameMachine.Tutorials.NetworkObjects
{

	public class Game : MonoBehaviour, GameMachineApp
	{
		private NetworkObject networkObject;

		void Start ()
		{
			Login.SetGameMachineApp (this);
			NetworkObjectManager.RegisterPrefab ("TestGameObject", 2);
			NetworkObjectManager.AddComponent ("SampleComponent", "id", "v", "test", "playerName");


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
			networkObject = GameObject.Find ("MyObject").GetComponent<NetworkObject> () as NetworkObject;
			SampleComponent s = GameObject.Find ("MyObject").GetComponent<SampleComponent> () as SampleComponent;
			s.playerName = User.Instance.username;
			//Test ();
		}

		public void Test ()
		{
			
			for (int i = 0; i < 10; i++) {
				UnityGameObjects unityGameObjects = new UnityGameObjects ();
				UnityGameObject unityGameObject = networkObject.ToUnityGameObject ();
				unityGameObject.scope = 2;
				unityGameObjects.unityGameObject.Add (unityGameObject);
				string player = "player_" + i.ToString ();
				NetworkObjectManager.SyncNetworkObjects (player, unityGameObjects);
			}
		}

		
		
	}
}
