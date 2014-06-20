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

        // Example of how to wire up GameMachine in your application.  

   
        private GameMachine.App app;


        void Start()
        {
            // Set our server properties
            GameMachine.Config.authUri = "http://192.168.1.8:3000/auth";
            GameMachine.Config.udpHost = "192.168.1.8";
            //GameMachine.Config.udpHost = "127.0.0.1";
            GameMachine.Config.udpPort = 8100;

            // Replace with your own user object if you want. 
            GameMachine.User user = GameMachine.User.Instance;
            user.SetUser("player", "pass");

            // Callbacks for authentication
            GameMachine.Authentication.Success success = OnAuthenticationSuccess;
            GameMachine.Authentication.Error error = OnAuthenticationError;

            // You need to add GameMachine.App as a component to either this or another game object in your scene
            app = this.gameObject.AddComponent(Type.GetType("GameMachine.App")) as GameMachine.App;

            // Attempt to login.  Authentication success callback will be fired on success.
            app.Login(user.username, user.password, success, error);
        }

        void OnAuthenticationError(string error)
        {
            Logger.Debug("Authentication Failed: " + error);
        }

        // Authentication successful.  Now we run the app which sets up the connection and 
        // initializes the actor system. 
        public void OnAuthenticationSuccess(string authtoken)
        {
            Logger.Debug("Authentication Success");
            GameMachine.App.AppStarted callback = OnAppStarted;
            app.OnAppStarted(callback);
           
            app.Run(User.Instance.username, authtoken);
        }

        // This is called once we have a connection and everything is started
        public void OnAppStarted()
        {
            Logger.Debug("OnAppStarted");

            // Start our chat example
            StartChat();
            Logger.Debug("Chat started");
            
            // Setup the persistence layer.  This is an optional feature, see the Persistence class
            // for how it works.
            StartPersistence();
            Logger.Debug("Peristence started");
            
            // Start sending/receiving location updates
            StartAreaOfInterest();
            Logger.Debug("AreaOfInterest started");

            JsonModel.Register(typeof(Attack), "Example::Models::Attack", "Example/CombatController");
            Attack attack = new Attack();
            attack.attacker = "test";
            attack.target = "test";
            attack.combat_ability = "test attack";
            attack.Send();
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
