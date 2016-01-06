using UnityEngine;
using System.Collections.Generic;

namespace GameMachine.Core {
    public class App : MonoBehaviour {
        private static Dictionary<object, string> onRunningCallbacks = new Dictionary<object, string>();

        private double lastEcho = -1000;
        private double echosPerSecond = 1.0f;
        public static float gameTickInterval = 0.060f;
        public bool showClientStats = false;
        private double echoInterval;
        private double lastEchoReceived = 0;
        private AppStarted appStarted;
        private ConnectionTimeout connectionTimeout;
        private bool appStartedCalled = false;
        private ActorSystem actorSystem;

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
            ActorSystem.instance.RegisterActor(messenger);

            remoteEcho = new RemoteEcho();
            remoteEcho.AddComponentSet("EchoTest");
            actorSystem.RegisterActor(remoteEcho);

            RemoteEcho.EchoReceived callback = OnEchoReceived;
            remoteEcho.OnEchoReceived(callback);


            running = true;
            RunOnRunningCallbacks();
            //Debug.Log("App running - waiting to verify connection");
        }

        void SetEchoInterval() {
            echoInterval = 0.60 / echosPerSecond;
        }

        void OnEchoReceived() {
            lastEchoReceived = Time.time;
        }

        void OnPlayerConnected() {
            //Debug.Log("Player Connected");
            actorSystem = gameObject.GetComponent<ActorSystem>() as ActorSystem;
            actorSystem.Activate(client);

            // Now create the actors
            StartCoreActors();

            connected = true;
            GameMachine.Login.userApp.ConnectionEstablished();
            if (!appStartedCalled) {
                appStarted();
                appStartedCalled = true;
                echosPerSecond = 0.20f;
                SetEchoInterval();
            }

            lastEchoReceived = Time.time;
            remoteEcho.Echo();
            InvokeRepeating("UpdateNetwork", 0.060f, gameTickInterval);
            if (showClientStats) {
                InvokeRepeating("UpdateStats", 1f, 1f);
            }
        }

        void Start() {
            Application.runInBackground = true;
            SetEchoInterval();
        }

        void UpdateStats() {
            Debug.Log("Bytes in " + client.GetBytesIn());
            Debug.Log("Bytes out " + client.GetBytesOut());
            client.ResetBytes();
        }

        void ConnectLoop() {
            if (client.ReceivedPlayerConnected()) {
                OnPlayerConnected();
            } else {
                client.SendPlayerConnect();
                //Debug.Log("Sent PlayerConnect");
                Invoke("ConnectLoop", 2f);
            }
        }

        void OnApplicationQuit() {
            if (client != null) {
                //Debug.Log("Stopping client");
                client.Stop();
            }
        }
        

        void UpdateNetwork() {
           
            if (!running) {
                return;
            }


            if (running && ActorSystem.instance.running) {
                ActorSystem.instance.AppUpdate(connected);
            }

            if (Time.time > (lastEcho + echoInterval)) {
                lastEcho = Time.time;

                if ((Time.time - lastEchoReceived) >= echoTimeout) {
                    connected = false;
                    Debug.Log("Echo timeout");
                    if ((Time.time - lastEchoReceived) >= disconnectTime) {
                        connectionTimeout();
                        client.Reconnect();
                    }
                }
                remoteEcho.Echo();
            }
        }


    }
}
