// http://robjdavey.wordpress.com/2011/02/11/asynchronous-tcp-client-example/
using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using ProtoBuf;
using System.Collections.Concurrent;
using System.Collections.Generic;
using GameMachine;
using ClientMessage = io.gamemachine.messages.ClientMessage;
using Entity = io.gamemachine.messages.Entity;
using PlayerLogout = io.gamemachine.messages.PlayerLogout;
using System.IO;

namespace GameMachine.Core
{
    public class AsyncTcpClient : Client
    {
        private int port;
        private TcpClient tcpClient;
        private int failedConnectionCount;
        private int maxFailedConnections;
        private string authtoken;
        private ClientMessage clientMessage;
        private string playerId;
        private bool measure = false;
        public bool running = false;
        private bool connected = false;
        private bool connecting = false;
        private IPAddress address;

        // 0 = combined.  Single server setup where cluster/region is on one server, or where
        // you simply do not have regions.
        // 1 = region.  Region connection.
        // 2 = cluster.  Cluster connection
        private int connectionType = 0;
        public static ConcurrentQueue<Entity> entityQueue = new ConcurrentQueue<Entity> ();

        public bool IsRunning ()
        {
            return running;
        }

        public void SetConnectionType (int connectionType)
        {
            this.connectionType = connectionType;
        }
		
        public ClientMessage CreateClientMessage ()
        {
            io.gamemachine.messages.Player player = new io.gamemachine.messages.Player ();
            player.id = playerId;
            player.authtoken = authtoken;
            ClientMessage clientMessage = new ClientMessage ();
            clientMessage.connection_type = connectionType;
            clientMessage.player = player;
            return clientMessage;
        }

        private void Connected ()
        {
            connecting = false;
            connected = true;
            tcpClient.NoDelay = true;
            ClientMessage message = CreateClientMessage ();
            message.playerConnect = new io.gamemachine.messages.PlayerConnect ();
            Write (Serialize (message));
        }

        public AsyncTcpClient (string address, int port, string playerId, string _authtoken)
			: this(port)
        {
            this.authtoken = _authtoken;
            this.playerId = playerId;
            Logger.Debug (address);

            if (!IPAddress.TryParse (address, out this.address)) {
                this.address = Dns.GetHostEntry (address).AddressList [0];
            }
            clientMessage = CreateClientMessage ();
        }
		

        private AsyncTcpClient (int port)
        {
            if (port < 0)
                throw new ArgumentException ();
            this.port = port;
            this.tcpClient = new TcpClient ();
            this.Encoding = Encoding.Default;
        }

        public Encoding Encoding { get; set; }
		

        public void Start ()
        {
            failedConnectionCount = 0;
            maxFailedConnections = 10;
            Connect ();
            running = true;
        }

        public void Stop ()
        {
            running = false;
            PlayerLogout logout = new PlayerLogout ();
            logout.authtoken = authtoken;
            logout.playerId = playerId;
            ClientMessage message = CreateClientMessage ();
            message.playerLogout = logout;
            byte[] bytes = Serialize (message);
            Write (bytes);
            tcpClient.Close ();
        }

        public void Connect ()
        {
            if (connecting) {
                return;
            }

            connecting = true;
            tcpClient.BeginConnect (address, port, ConnectCallback, null);
        }

        public void SendEntity (Entity entity)
        {
            List<Entity> entities = new List<Entity> ();
            entities.Add (entity);
            SendEntities (entities);
        }

        public void SendEntities (List<Entity> entities)
        {
            if (!connected) {
                return;
            }

            foreach (Entity entity in entities) {
                clientMessage.entity.Add (entity);
            }
			
            Write (Serialize (clientMessage));
            clientMessage.entity.Clear ();
        }

        public void Write (byte[] bytes)
        {
            if (!tcpClient.Connected) {
                connected = false;
                if (!connecting) {
                    Connect ();
                }

                return;
            }

            NetworkStream networkStream = tcpClient.GetStream ();
            networkStream.BeginWrite (bytes, 0, bytes.Length, WriteCallback, null);
        }

        private void WriteCallback (IAsyncResult result)
        {
            NetworkStream networkStream = tcpClient.GetStream ();
            networkStream.EndWrite (result);
        }

        private void ConnectCallback (IAsyncResult result)
        {
            if (!tcpClient.Connected) {
                connecting = false;
                if (failedConnectionCount > maxFailedConnections) {
                    running = false;
                    Logger.Debug ("Tcp: Too many failed connection attempts, aborting");
                    return;
                }
                failedConnectionCount++;
                Connect ();
                return;
            }

            tcpClient.EndConnect (result);

            NetworkStream networkStream = tcpClient.GetStream ();
            byte[] buffer = new byte[tcpClient.ReceiveBufferSize];
            Logger.Debug ("Tcp: Connected");
            networkStream.BeginRead (buffer, 0, buffer.Length, ReadCallback, buffer);
            Connected ();
        }

        private void ReadCallback (IAsyncResult result)
        {
            int read;
            NetworkStream networkStream;
            try {
                networkStream = tcpClient.GetStream ();
                read = networkStream.EndRead (result);
            } catch {
                Logger.Debug ("Error reading stream");
                return;
            }
			
            if (read == 0) {
                Logger.Debug ("Connection closed");
                return;
            }

            // This might look strange but it works well.  We enter here
            // once and then just forever loop on reading the network stream, which
            // blocks until it can read a full protobuf message.  So basically every
            // iteration either has a complete message, or it's blocking, never a busy loop
            ClientMessage clientMessage;

            while (true) {
                clientMessage = Deserialize (networkStream);
                foreach (Entity entity in clientMessage.entity) {
                    ClientMessageQueue.entityQueue.Enqueue (entity); 
                }
            }

            byte[] buffer = result.AsyncState as byte[];
            networkStream.BeginRead (buffer, 0, buffer.Length, ReadCallback, buffer);
        }

        private byte[] Serialize (ClientMessage message)
        {
            MemoryStream stream = new MemoryStream ();
            Serializer.SerializeWithLengthPrefix (stream, message, PrefixStyle.Base128);
            return stream.ToArray ();
        }
		
        public ClientMessage Deserialize (Stream inc)
        {
            ClientMessage clientMessage = Serializer.DeserializeWithLengthPrefix<ClientMessage> (inc, PrefixStyle.Base128);
            return clientMessage;
        }

    }
}