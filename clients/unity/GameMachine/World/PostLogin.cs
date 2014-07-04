using UnityEngine;
using System.Collections;
using GameMachine;
using GameMachine.Core;

namespace GameMachine.World
{


	public class PostLogin : MonoBehaviour, GameMachineApp
	{

		private Login login;

		public void OnLoggedIn ()
		{
			JsonModel.Register (typeof(Attack), "Example::Models::Attack", "Example/CombatController");
			JsonModel.Register (typeof(Vitals), "Example::Models::Vitals");
			JsonModel.Register (typeof(CombatUpdate), "Example::Models::CombatUpdate");
			Invoke ("WaitForPlayerVitals", 0.05f);
		}
	
		public void ConnectionTimeout ()
		{
			Application.LoadLevel ("world_disconnected");
		}

		public void WaitForPlayerVitals ()
		{
			if (GameMachine.World.Player.vitals == null) {
				Invoke ("WaitForPlayerVitals", 0.05f);
			} else {
				OnPlayerVitalsReceived ();
			}
		}
	
		public void OnPlayerVitalsReceived ()
		{
			Destroy (GameObject.Find ("Main Camera"));
			Application.LoadLevelAdditive ("world_main");
		}

		void LoadLogin ()
		{
			login = this.gameObject.GetComponent<Login> () as Login;
			if (login == null) {
				Invoke ("LoadLogin", 0.05f);
			} else {
				login.SetGameMachineApp (this);
			}

		}

		void Start ()
		{
			LoadLogin ();
		}

		void Update ()
		{
		
		}

	}
}
