using System;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.IO;
using ProtoBuf;
using System.Collections.Concurrent;
using System.Collections.Generic;
using GameMachine;
using ClientMessage = io.gamemachine.messages.ClientMessage;
using Entity = io.gamemachine.messages.Entity;
using PlayerLogout = io.gamemachine.messages.PlayerLogout;


namespace GameMachine.Core
{
    public class AsyncUdpClient : Client
    {
        private IPEndPoint udp_ep;
        private UdpClient udpClient;
        private int port;
        private string host;
        private ClientMessage clientMessage;
        private string playerId;
        private string authtoken;
        private bool measure = false;
        public bool running = false;

        // 0 = combined.  Single server setup where cluster/region is on one server, or where
        // you simply do not have regions.
        // 1 = region.  Region connection.
        // 2 = cluster.  Cluster connection
        private int connectionType = 0;
        public static ConcurrentQueue<Entity> entityQueue = new ConcurrentQueue<Entity> ();

        public AsyncUdpClient (string host, int port, string _playerId, string _authtoken, bool measure=false)
        {
            playerId = _playerId;
            authtoken = _authtoken;
            this.host = host;
            this.port = port;
            clientMessage = CreateClientMessage ();
            this.measure = measure;
        }

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

        public void Stop ()
        {
            running = false;
            PlayerLogout logout = new PlayerLogout ();
            logout.authtoken = authtoken;
            logout.playerId = playerId;
            ClientMessage message = CreateClientMessage ();
            message.playerLogout = logout;
            byte[] bytes = Serialize (message);
            udpClient.Send (bytes, bytes.Length, host, port);
            udpClient.Close ();
        }

        public void Start ()
        {
            running = true;
            udp_ep = new IPEndPoint (IPAddress.Any, 0);
            udpClient = new UdpClient (udp_ep);
            receiveData ();

            ClientMessage message = CreateClientMessage ();
            message.playerConnect = new io.gamemachine.messages.PlayerConnect ();
            Send (Serialize (message));
        }
				
        private void SendCallback (IAsyncResult ar)
        {
            UdpClient u = (UdpClient)ar.AsyncState;
            u.EndSend (ar);
        }

        public void SendEntity (Entity entity)
        {
            List<Entity> entities = new List<Entity> ();
            entities.Add (entity);
            SendEntities (entities);
        }

        public void SendEntities (List<Entity> entities)
        {
            foreach (Entity entity in entities) {
                clientMessage.entity.Add (entity);
            }

            Send (Serialize (clientMessage));
            clientMessage.entity.Clear ();
        }

        public void Send (byte[] bytes)
        {
            udpClient.BeginSend (bytes, bytes.Length, host, port, new AsyncCallback (SendCallback), udpClient);
        }
		
        private void dataReady (IAsyncResult ar)
        {
            byte[] bytes = udpClient.EndReceive (ar, ref udp_ep);
            ClientMessage message = Deserialize (bytes);
            foreach (Entity entity in message.entity) {
                ClientMessageQueue.entityQueue.Enqueue (entity); 
            }
            receiveData ();
        }
		
        private void receiveData ()
        {
            udpClient.BeginReceive (new AsyncCallback (dataReady), udp_ep);
        }

        private ClientMessage Deserialize (byte[] bytes)
        {
            MemoryStream stream = new MemoryStream (bytes);
            return Serializer.Deserialize<ClientMessage> (stream);
        }
		
        private byte[] Serialize (ClientMessage message)
        {
            MemoryStream stream = new MemoryStream ();
            Serializer.Serialize (stream, message);
            return stream.ToArray ();
        }
    }
}