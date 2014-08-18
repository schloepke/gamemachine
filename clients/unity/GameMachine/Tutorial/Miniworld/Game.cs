using UnityEngine;
using System;
using GameMachine;
using GameMachine.Core;
using GameMachine.Chat;


namespace GameMachine.Tutorials.Miniworld
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
            GameObject gameObject = (GameObject)Instantiate (Resources.Load ("PlayerCharacter"));
            this.gameObject.AddComponent<EntityManager> ();
            this.gameObject.AddComponent<GameMachine.Chat.ChatManager> ();
        }
		
		
    }
}
