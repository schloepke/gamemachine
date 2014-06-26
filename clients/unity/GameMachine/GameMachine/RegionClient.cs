using System.Collections;
using UnityEngine;
using GameMachine;
using Entity = GameMachine.Messages.Entity;


namespace GameMachine
{
    public class RegionClient : MonoBehaviour
    {
        public Client client;

        public bool running = false;
        public static bool connected = false;
        
        
        private double lastEcho = 0;
        private double echosPerSecond = 1;
        private double echoInterval;
        private double lastEchoReceived = 0;

        public double echoTimeout = 5.0f;
        public float disconnectTime = 10f;

        public delegate void RegionClientStarted();
        public delegate void ConnectionTimeout();
        private RegionClientStarted regionClientStarted;
        private ConnectionTimeout connectionTimeout;

        private bool regionStartedCalled = false;
        private int port;
        private string username;
        private string authtoken;
        public string currentRegion;

        
        public void OnRegionClientStarted(RegionClientStarted callback)
        {
            regionClientStarted = callback;
        }
        
        public void OnConnectionTimeout(ConnectionTimeout connectionTimeout)
        {
            this.connectionTimeout = connectionTimeout;
        }
         
        public void Disconnect()
        {
            Logger.Debug("Region client: disconnecting from current region");
            running = false;
            regionStartedCalled = false;
            connected = false;
            ActorSystem.Instance.SetRegionClient(null);
            if (client != null && client.running)
            {
                client.Stop();
            }
        }

        public void Init(int port, string username, string authtoken)
        {
            this.port = port;
            this.username = username;
            this.authtoken = authtoken;
        }

        public void Connect(string region, string host)
        {
            if (running)
            {
                Disconnect();
            }
            Logger.Debug("RegionClient.Connect " + region + ":" + host + ":" + authtoken);
            client = new Client(host, port, username, authtoken);
            client.SetConnectionType(2);
            client.Start();
            ActorSystem.Instance.SetRegionClient(client);

            RemoteEcho.RegionEchoReceived callback = OnEchoReceived;
            GameMachine.App.remoteEcho.OnRegionEchoReceived(callback);
            running = true;
            currentRegion = region;
            Logger.Debug("Region client running - waiting to verify connection");
        }

        public void Connect(string region)
        {
            string host = RegionHandler.regions [region];
            Connect(region, host);
        }
                
        void OnEchoReceived()
        {
            if (!connected)
            {
                connected = true;
                Logger.Debug("Region client connected " + currentRegion);
                if (!regionStartedCalled)
                {
                    regionClientStarted();
                    regionStartedCalled = true;
                }
            }
            
            lastEchoReceived = Time.time;
        }
        
        void Start()
        {
            echoInterval = 0.60 / echosPerSecond;
            lastEchoReceived = Time.time;
            InvokeRepeating("UpdateNetwork", 1.0f, 1.0F);
        }
        
        void OnApplicationQuit()
        {
            Disconnect();
        }
        
        
        void UpdateNetwork()
        {
             
            if (!running)
            {
                return;
            }

            if (Time.time > (lastEcho + echoInterval))
            {
                lastEcho = Time.time;
                
                if ((Time.time - lastEchoReceived) >= echoTimeout)
                {
                    connected = false;
                    Logger.Debug("RegionEcho timeout");
                    if ((Time.time - lastEchoReceived) >= disconnectTime)
                    {
                        Logger.Debug("Region Connection timeout");
                        if (connectionTimeout != null)
                        {
                            Disconnect();
                            connectionTimeout();
                        }

                    }
                }
                GameMachine.App.remoteEcho.RegionEcho();
            }
        }
        
        
    }
}
