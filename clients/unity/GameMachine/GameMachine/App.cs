using System.Collections;
using UnityEngine;
using GameMachine;
using Entity = GameMachine.Messages.Entity;


namespace GameMachine
{
    public class App : MonoBehaviour
    {
        public Client client;
        private RemoteEcho remoteEcho;

        public bool running = false;
        public bool connected = false;

        private double lastUpdate = 0;
        private double updatesPerSecond = 10;
        private double updateInterval;

        private double lastEcho = 0;
        private double echosPerSecond = 1;
        private double echoInterval;
        private double lastEchoReceived = 0;
        private double echoTimeout = 5.0f;

        public delegate void AppStarted();
        private AppStarted appStarted;

        public void OnAppStarted(AppStarted callback)
        {
            appStarted = callback;
        }

        public void Login(string username, string password, Authentication.Success success, Authentication.Error error)
        {
            Authentication auth = new Authentication();
            StartCoroutine(auth.Authenticate(username, password, success, error));
        }

        public void Run(string username, string authtoken)
        {
            // Create client and start actor system
            client = new Client(username, authtoken);
            ActorSystem.Instance.Start(client);

            // Now create the actors
            StartCoreActors();
        }

        public void StartCoreActors()
        {
            // Actors that come with GameMachine

            ObjectDb db = new ObjectDb();
            db.AddComponentSet("ObjectdbGetResponse");
            ActorSystem.Instance.RegisterActor(db);

            Messenger messenger = new Messenger();
            messenger.AddComponentSet("ChatMessage");
            messenger.AddComponentSet("ChatChannels");
            ActorSystem.Instance.RegisterActor(messenger);

            EntityTracking entityTracking = new EntityTracking();
            entityTracking.AddComponentSet("Neighbors");
            ActorSystem.Instance.RegisterActor(entityTracking);


            remoteEcho = new RemoteEcho();
            remoteEcho.AddComponentSet("EchoTest");
            ActorSystem.Instance.RegisterActor(remoteEcho);
            
            RemoteEcho.EchoReceived callback = OnEchoReceived;
            remoteEcho.OnEchoReceived(callback);
            running = true;
            Logger.Debug("App running - waiting to verify connection");
        }

        void OnEchoReceived()
        {
            if (!connected)
            {
                connected = true;
                Logger.Debug("App connected");
                appStarted();

            }

            lastEchoReceived = Time.time;
        }

        void Start()
        {
            Application.runInBackground = true;
            echoInterval = 0.60 / echosPerSecond;
            updateInterval = 0.60 / updatesPerSecond;
        }

        void OnApplicationQuit()
        {
            if (client != null)
            {
                client.Stop();
            }
        }


        void Update()
        {
            if (!running)
            {
                return;
            }

            if (Time.time > (lastUpdate + updateInterval))
            {
                lastUpdate = Time.time;
                if (running && ActorSystem.Instance.Running)
                {
                    ActorSystem.Instance.Update();
                }

                if (Time.time > (lastEcho + echoInterval))
                {
                    lastEcho = Time.time;

                    if ((Time.time - lastEchoReceived) >= echoTimeout)
                    {
                        connected = false;
                    }
                    remoteEcho.Echo();
                }
            }
        }

    }
}
