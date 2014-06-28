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
        private GameMachine.RegionClient regionClient;
        private string authUri;

        public string udpHost = "127.0.0.1";
        public int udpPort = 24130;
        public int udpRegionPort = 24130;
        public bool useRegions = false;
        public string username;
        public string password;

        void Start()
        {
            if (username != null && password != null)
            {
                User.Instance.SetUser(username, password);
            }
           

            authUri = "http://" + udpHost + ":3000/auth";
            // Replace with your own user object if you want. 
            GameMachine.User user = GameMachine.User.Instance;

            // Callbacks for authentication
            GameMachine.Authentication.Success success = OnAuthenticationSuccess;
            GameMachine.Authentication.Error error = OnAuthenticationError;

            // You need to add GameMachine.App as a component to either this or another game object in your scene
            app = this.gameObject.AddComponent(Type.GetType("GameMachine.App")) as GameMachine.App;

            // Attempt to login.  Authentication success callback will be fired on success.
            app.Login(authUri, user.username, user.password, success, error);
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
           
            GameMachine.App.ConnectionTimeout connectionCallback = OnConnectionTimeout;
            app.OnConnectionTimeout(connectionCallback);

            app.Run(udpHost, udpPort, User.Instance.username, authtoken);

            // Region connections.  
            StartRegionClient(authtoken);
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

        }

        public void OnConnectionTimeout()
        {
            Logger.Debug("Connection timed out");
        }

        void StartRegionClient(string authtoken)
        {
            regionClient = this.gameObject.AddComponent(Type.GetType("GameMachine.RegionClient")) as GameMachine.RegionClient;
            GameMachine.RegionClient.ConnectionTimeout connectionCallback = OnRegionConnectionTimeout;
            regionClient.OnConnectionTimeout(connectionCallback);



            RegionClient.RegionClientStarted callback = OnRegionClientStarted;
            regionClient.OnRegionClientStarted(callback);

            // This is how you would setup and connect/disconnect from regions.  This is
            // basically a stub for being able to test the functionality.
            if (useRegions)
            {
                // Call this once
                regionClient.Init(udpRegionPort, User.Instance.username, authtoken);

                // Connect to a region by name
                // regionClient.Connect("zone2");

                // Disconnect from the current region
                //regionClient.Disconnect();

                // Connect to region server specifying a host.  Just ignore this
                // unless you are developing/testing the core server code. This is
                // needed when running multilple server instances under the same ip,
                // which for a real game you would never do.
                regionClient.Connect("zone2", "192.168.1.8");


            }
        }

        public void OnRegionConnectionTimeout()
        {
            Logger.Debug("Region Connection timed out");
        }

        void OnRegionClientStarted()
        {
            Logger.Debug("OnRegionClientStarted called");
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
