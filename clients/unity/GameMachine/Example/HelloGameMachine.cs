using UnityEngine;
using System;
using System.Collections;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using Player = GameMachine.Messages.Player;

namespace GameMachine.Example
{

    public class HelloGameMachine : MonoBehaviour
    {

        // Example of how to wire up GameMachine in your application.  We use the Start function
        // here as the entry point to kick everything off, but you can initialize anyplace you like.
   
        private GameMachine.App app;

        void Start()
        {
            // Replace with your own user object if you want. 
            User user = User.Instance;
            user.SetUser("player", "pass");
            Login(user.username, user.password);
        }
	
        void Login(string username, string password)
        {
            GameMachine.Authentication.Success success = OnAuthenticationSuccess;
            GameMachine.Authentication.Error error = OnAuthenticationError;

            // GameMachine.App is the only GameMachine class that is a MonoBehavior
            // You want to add it as a component to just ONE game object in your game.
            app = this.gameObject.AddComponent(Type.GetType("GameMachine.App")) as GameMachine.App;
            app.Login(username, password, success, error);

        }

        void OnAuthenticationError(string error)
        {
            Logger.Debug("Authentication Failed: " + error);
        }
        
        public void OnAuthenticationSuccess(string authtoken)
        {
            // After this is called GameMachine core is loaded, the client is connected, and
            // the actor system is running.
            app.Run(User.Instance.username, authtoken);

            // Start our chat example
            StartChat();

            // Setup the persistence layer.  This is an optional feature, see the Persistence class
            // for how it works.
            StartPersistence();

            // Start sending/receiving location updates
            StartAreaOfInterest();
                
        }

        void StartAreaOfInterest()
        {
            GameObject misc = GameObject.Find("Misc");
            misc.AddComponent("AreaOfInterest");
        }

        void StartChat()
        {
            GameObject camera = GameObject.Find("Camera");
            GameObject chatBox = new GameObject("ChatBox");
            chatBox.transform.parent = camera.transform;
            chatBox.AddComponent("Chat");
        }

        void StartPersistence()
        {
            GameObject camera = GameObject.Find("Camera");
            GameObject misc = new GameObject("Misc");
            misc.transform.parent = camera.transform;
            misc.AddComponent("Persistence");
        }

    }
}
