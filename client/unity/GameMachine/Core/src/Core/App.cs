using System.Collections;
using UnityEngine;
using System.Collections.Generic;
using GameMachine;
using Entity = io.gamemachine.messages.Entity;

namespace GameMachine.Core {
    public class App : MonoBehaviour {
        private static Dictionary<object, string> onRunningCallbacks = new Dictionary<object, string>();

        private double lastEcho = -1000;
        private double echosPerSecond = 1.0f;
        public static float gameTickInterval = 0.050f;
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
        public static Login.Protocol protocol;

        public delegate void AppStarted();
        public delegate void ConnectionTimeout();

        public static void onRunning(object caller, string methodName) {
            onRunningCallbacks[caller] = methodName;
        }

        private void RunOnRunningCallbacks() {
            foreach (object caller in onRunningCallbacks.Keys) {
                string methodName = onRunningCallbacks[caller];
                caller.GetType().GetMethod(methodName).Invoke(caller, null);
            }
        }

        public void OnAppStarted(AppStarted callback) {
            appStarted = callback;
        }

        public void OnConnectionTimeout(ConnectionTimeout connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public void Login(string gameId, string hostname, int httpPort, string username, string password, Authentication.Success success, Authentication.Error error) {
            Authentication auth = new Authentication();
            StartCoroutine(auth.Authenticate(gameId, hostname, httpPort, username, password, success, error));
        }

        public void Run(Login.Protocol protocol, string host, int port, string username, int authtoken) {
            App.protocol = protocol;
            // Create client and start actor system
            if (protocol == GameMachine.Login.Protocol.UDP) {
                client = new AsyncUdpClient(host, port, username, authtoken);
            } else {
                client = new AsyncTcpClient(host, port, username, authtoken);
            }

            client.Start();
           
            ConnectLoop();
           
        }

        public void StartCoreActors() {
            // Actors that come with GameMachine

            Messenger messenger = new Messenger();
            messenger.AddComponentSet("ChatMessage");
            messenger.AddComponentSet("ChatChannels");
            messenger.AddComponentSet("ChatInvite");
            ActorSystem.Instance.RegisterActor(messenger);

            EntityTracking entityTracking = new EntityTracking();
            entityTracking.AddComponentSet("Neighbors");
            ActorSystem.Instance.RegisterActor(entityTracking);


            remoteEcho = new RemoteEcho();
            remoteEcho.AddComponentSet("EchoTest");
            ActorSystem.Instance.RegisterActor(remoteEcho);

            RegionHandler regionHandler = new RegionHandler();
            regionHandler.AddComponentSet("Regions");
            ActorSystem.Instance.RegisterActor(regionHandler);


            RemoteEcho.EchoReceived callback = OnEchoReceived;
            remoteEcho.OnEchoReceived(callback);


            running = true;
            RunOnRunningCallbacks();
            Debug.Log("App running - waiting to verify connection");
        }

        void SetEchoInterval() {
            echoInterval = 0.60 / echosPerSecond;
        }
        void UpdateRegions() {
            RegionHandler.SendRequest();
        }

        void OnEchoReceived() {


            lastEchoReceived = Time.time;
        }

        void OnPlayerConnected() {
            Debug.Log("Player Connected");
            ActorSystem.Instance.Start(client);

            // Now create the actors
            StartCoreActors();

            connected = true;
            GameMachine.Login.userApp.ConnectionEstablished();
            if (!appStartedCalled) {
                appStarted();
                appStartedCalled = true;
                InvokeRepeating("UpdateRegions", 0.01f, 20.0F);
                echosPerSecond = 0.20f;
                SetEchoInterval();
            }

            lastEchoReceived = Time.time;
            remoteEcho.Echo();
            InvokeRepeating("UpdateNetwork", 0.010f, gameTickInterval);
        }

        void Start() {
            Application.runInBackground = true;
            SetEchoInterval();
        }

        void ConnectLoop() {
            if (client.ReceivedPlayerConnected()) {
                OnPlayerConnected();
            } else {
                client.SendPlayerConnect();
                Debug.Log("Sent PlayerConnect");
                Invoke("ConnectLoop", 2f);
            }
        }

        void OnApplicationQuit() {
            if (client != null) {
                Debug.Log("Stopping client");
                client.Stop();
            }
        }


        void Update() {
            ActorSystem.Instance.DeliverQueuedMessages();
        }

        void UpdateNetwork() {
            if (!running) {
                return;
            }


            if (running && ActorSystem.Instance.Running) {
                ActorSystem.Instance.Update(connected);
            }

            if (Time.time > (lastEcho + echoInterval)) {
                lastEcho = Time.time;

                if ((Time.time - lastEchoReceived) >= echoTimeout) {
                    connected = false;
                    Debug.Log("Echo timeout");
                    if ((Time.time - lastEchoReceived) >= disconnectTime) {
                        connectionTimeout();
                    }
                }
                remoteEcho.Echo();
            }
        }


    }
}
