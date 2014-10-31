using System.Collections;
using UnityEngine;
using  System.Collections.Generic;
using GameMachine;
using Entity = GameMachine.Messages.Entity;
using GameMachine.Models.Team;
using GameMachine.Models;

namespace GameMachine.Core
{
    public class App : MonoBehaviour
    {
        private static Dictionary<object, string> onRunningCallbacks = new Dictionary<object, string> ();

        private double lastEcho = -1000;
        private double echosPerSecond = 1.0f;
        private double echoInterval;
        private double lastEchoReceived = 0;
        private AppStarted appStarted;
        private ConnectionTimeout connectionTimeout;
        private bool appStartedCalled = false;

        public Client client;
        public static RemoteEcho remoteEcho;
        public bool running = false;
        public bool connected = false;
        public double echoTimeout = 5.0f;
        public float disconnectTime = 10f;
        public static string protocol = "UDP";

        public delegate void AppStarted ();
        public delegate void ConnectionTimeout ();

        public static void onRunning (object caller, string methodName)
        {
            onRunningCallbacks [caller] = methodName;
        }

        private void RunOnRunningCallbacks ()
        {
            foreach (object caller in onRunningCallbacks.Keys) {
                string methodName = onRunningCallbacks [caller];
                caller.GetType ().GetMethod (methodName).Invoke (caller, null);
            }
        }

        public void OnAppStarted (AppStarted callback)
        {
            appStarted = callback;
        }

        public void OnConnectionTimeout (ConnectionTimeout connectionTimeout)
        {
            this.connectionTimeout = connectionTimeout;
        }

        public void Login (string gameId, string hostname, int httpPort, string username, string password, Authentication.Success success, Authentication.Error error)
        {
            Authentication auth = new Authentication ();
            StartCoroutine (auth.Authenticate (gameId, hostname, httpPort, username, password, success, error));
        }

        public void Run (string protocol, string host, int port, string username, string authtoken)
        {
            App.protocol = protocol;
            // Create client and start actor system
            if (protocol == "UDP") {
                client = new AsyncUdpClient (host, port, username, authtoken);
            } else {
                client = new AsyncTcpClient (host, port, username, authtoken);
            }

            client.Start ();
            ActorSystem.Instance.Start (client);

            // Now create the actors
            StartCoreActors ();
            remoteEcho.Echo ();
        }

        public void StartCoreActors ()
        {
            // Actors that come with GameMachine

            Messenger messenger = new Messenger ();
            messenger.AddComponentSet ("ChatMessage");
            messenger.AddComponentSet ("ChatChannels");
            messenger.AddComponentSet ("ChatInvite");
            ActorSystem.Instance.RegisterActor (messenger);

            EntityTracking entityTracking = new EntityTracking ();
            entityTracking.AddComponentSet ("Neighbors");
            ActorSystem.Instance.RegisterActor (entityTracking);


            remoteEcho = new RemoteEcho ();
            remoteEcho.AddComponentSet ("EchoTest");
            ActorSystem.Instance.RegisterActor (remoteEcho);

            RegionHandler regionHandler = new RegionHandler ();
            regionHandler.AddComponentSet ("Regions");
            ActorSystem.Instance.RegisterActor (regionHandler);


            RemoteEcho.EchoReceived callback = OnEchoReceived;
            remoteEcho.OnEchoReceived (callback);

            // Json models
            JsonModel.Register (typeof(PlayerSkills), "GameMachine::Models::PlayerSkills", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(LockTeam), "GameMachine::Models::LockTeam", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(UnlockTeam), "GameMachine::Models::UnlockTeam", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(Team), "GameMachine::Models::Team", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(CreateTeam), "GameMachine::Models::CreateTeam", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(DestroyTeam), "GameMachine::Models::DestroyTeam", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(JoinTeam), "GameMachine::Models::JoinTeam", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(TeamAcceptInvite), "GameMachine::Models::TeamAcceptInvite", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(TeamInvite), "GameMachine::Models::TeamInvite", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(Teams), "GameMachine::Models::Teams", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(TeamsRequest), "GameMachine::Models::TeamsRequest", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(TeamJoined), "GameMachine::Models::TeamJoined", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(LeaveTeam), "GameMachine::Models::LeaveTeam", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(TeamLeft), "GameMachine::Models::TeamLeft", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(Match), "GameMachine::Models::Match", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(FindMatch), "GameMachine::Models::FindMatch", "GameMachine/GameSystems/TeamManager");
            JsonModel.Register (typeof(EchoTest), "GameMachine::Models::EchoTest");

            running = true;
            RunOnRunningCallbacks ();
            InvokeRepeating ("DeliverQueuedMessages", 0.005f, 0.005f);
            Logger.Debug ("App running - waiting to verify connection");
        }

        void SetEchoInterval ()
        {
            echoInterval = 0.60 / echosPerSecond;
        }
        void UpdateRegions ()
        {
            RegionHandler.SendRequest ();
        }

        void OnEchoReceived ()
        {
            if (!connected) {
                connected = true;
                GameMachine.Login.userApp.ConnectionEstablished ();
                if (!appStartedCalled) {
                    appStarted ();
                    appStartedCalled = true;
                    InvokeRepeating ("UpdateRegions", 0.01f, 20.0F);

                    // The server on initial connect has to setup
                    // a child actor for routing outgoing messages, so the first
                    // echo or two can be lost.  We set the pre connect echo's to go
                    // at a faster rate, then throttle them down after we connect
                    echosPerSecond = 0.20f;
                    SetEchoInterval ();
                }
            }

            lastEchoReceived = Time.time;
        }

        void Start ()
        {
            Application.runInBackground = true;
            SetEchoInterval ();
            lastEchoReceived = Time.time;
            InvokeRepeating ("UpdateNetwork", 0.010f, 0.066f);
        }

        void OnApplicationQuit ()
        {
            if (client != null) {
                Logger.Debug ("Stopping client");
                client.Stop ();
            }
        }


        void DeliverQueuedMessages ()
        {
            ActorSystem.Instance.DeliverQueuedMessages ();
        }

        void UpdateNetwork ()
        {
            if (!running) {
                return;
            }
            
           
            if (running && ActorSystem.Instance.Running) {
                ActorSystem.Instance.Update (connected);
            }
                
            if (Time.time > (lastEcho + echoInterval)) {
                lastEcho = Time.time;
                    
                if ((Time.time - lastEchoReceived) >= echoTimeout) {
                    connected = false;
                    //Logger.Debug ("Echo timeout");
                    if ((Time.time - lastEchoReceived) >= disconnectTime) {
                        //Logger.Debug ("Connection timeout");
                        connectionTimeout ();
                    }
                }
                remoteEcho.Echo ();
            }
        }


    }
}
