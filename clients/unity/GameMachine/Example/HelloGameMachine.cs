using UnityEngine;
using System;
using System.Collections;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;
using Entity = GameMachine.Messages.Entity;
using Player = GameMachine.Messages.Player;
using GameMachine.Models.Team;

namespace GameMachine.Example
{
	public class HelloGameMachine : MonoBehaviour, GameMachineApp
	{

		private Login login;

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

		public void ConnectionTimeout ()
		{
			Logger.Debug ("Connection timed out");
		}

		// This is called once we have a connection and everything is started
		public void OnLoggedIn ()
		{
			Logger.Debug ("OnAppStarted");



			// Start our chat example
			StartChat ();
			Logger.Debug ("Chat started");
            
			// Setup the persistence layer.  This is an optional feature, see the Persistence class
			// for how it works.
			StartPersistence ();
			Logger.Debug ("Peristence started");
            
			// Start sending/receiving location updates
			StartAreaOfInterest ();
			Logger.Debug ("AreaOfInterest started");

		}

		void StartAreaOfInterest ()
		{
			GameObject misc = GameObject.Find ("Misc");
			misc.AddComponent ("AreaOfInterest");
		}

		void StartChat ()
		{
			GameObject camera = GameObject.Find ("Camera");
			GameObject chatBox = new GameObject ("ChatBox");
			chatBox.transform.parent = camera.transform;
			chatBox.AddComponent ("Chat");

			// Add Teams
			chatBox.AddComponent<TeamUi> ();
		}

		void StartPersistence ()
		{
			GameObject camera = GameObject.Find ("Camera");
			GameObject misc = new GameObject ("Misc");
			misc.transform.parent = camera.transform;
			misc.AddComponent ("Persistence");
		}

	}
}
