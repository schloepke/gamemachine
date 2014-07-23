using System;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.IO;
using  ProtoBuf;
using GameMachine;
using NLog;
using GameMessage = GameMachine.Messages.GameMessage;

namespace GameMachine
{
    public class DataPacket
    {
        public Socket sock;
        public byte[] buf;
        public EndPoint ep;

        public DataPacket(Socket sock, byte[] buf, EndPoint ep)
        {
            this.sock = sock;
            this.buf = buf;
            this.ep = ep;
        }
    }

    public class ProxyServer
    {

        public static Logger logger = LogManager.GetLogger("GameMachine");
        private Socket server;

        static void Main(string[] args)
        {
           
            ProxyServer server = new ProxyServer();
            server.Run();

            //ProxyClient proxyClient = new ProxyClient(Int32.Parse(args [0]));
            //proxyClient.Start();
			
            ProxyServer.logger.Info("Proxy Starting");
            Console.WriteLine("Press any key to exit.");
            Console.ReadLine();
        }
		
        public void Run()
        {
            server = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            server.Bind(new IPEndPoint(IPAddress.Loopback, 4000));

            for (int i=0; i<5; i++)
            {
                BeginReceive();
            }
        }

        private void BeginReceive()
        {
            byte[] buffer = new byte[8096];
            EndPoint ep = (EndPoint)new IPEndPoint(IPAddress.Loopback, 0);
            DataPacket packet = new DataPacket(server, buffer, ep);
            server.BeginReceiveFrom(packet.buf, 0, packet.buf.Length, SocketFlags.None, ref packet.ep, new AsyncCallback(Received), packet);
        }

        private void Received(IAsyncResult iar)
        {
            try
            {
                DataPacket packet = (DataPacket)iar.AsyncState;
                int msgLen = packet.sock.EndReceiveFrom(iar, ref packet.ep);
                byte[] localMsg = new byte[msgLen];
                Array.Copy(packet.buf, localMsg, msgLen);

                server.BeginReceiveFrom(packet.buf, 0, packet.buf.Length, SocketFlags.None, ref packet.ep, new AsyncCallback(Received), packet);

                //GameMessage gameMessage = ByteArrayToGameMessage(localMsg);
                server.BeginSendTo(localMsg, 0, localMsg.Length, SocketFlags.None, packet.ep, new AsyncCallback(Sent), packet.ep);

            } catch (ObjectDisposedException e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public void Sent(IAsyncResult ar)
        {
            try
            {
                server.EndSend(ar);
            } catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }
        }

        public static GameMessage ByteArrayToGameMessage(byte[] bytes)
        {
            GameMessage gameMessage;
            MemoryStream stream = new MemoryStream(bytes);
            gameMessage = Serializer.Deserialize<GameMessage>(stream);
            return gameMessage;
        }
        
        public static byte[] GameMessageToByteArray(GameMessage gameMessage)
        {
            byte[] data;
            MemoryStream stream = new MemoryStream();
            Serializer.Serialize(stream, gameMessage);
            data = stream.ToArray();
            return data;
        }
    }
}
