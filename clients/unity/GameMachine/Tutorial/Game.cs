using UnityEngine;
using System;
using  System.Collections.Generic;
using GameMachine;
using GameMachine.Core;
using DynamicMessages;



namespace GameMachine.Tutorial
{

    public class Destination
    {
        public const int Inventory = 1;
    }

    public class Game : MonoBehaviour, GameMachineApp
    {
        
        void Awake ()
        {
            DontDestroyOnLoad (transform.gameObject);
        }

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
            this.gameObject.AddComponent<TutorialSelect> ();
            //Tracker tracker = this.gameObject.GetComponent<Tracker> () as Tracker;
            //EntityTracking.Register (tracker);
        }

    }
}
