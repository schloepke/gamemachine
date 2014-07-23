using System;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.IO;
using  ProtoBuf;
using GameMachine;
using NLog;
using MonoMessage = GameMachine.Messages.MonoMessage;
using Entity = GameMachine.Messages.Entity;

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

    public class Server
    {

        public static int port = 24320;
        public static Logger logger = LogManager.GetLogger("GameMachine");
        private Socket server;
        private MessageRouter router;

        static void Main(string[] args)
        {

            Server server = new Server();
            server.Run();
			
            Server.logger.Info("Server Starting");
            Console.WriteLine("Press any key to exit.");
            Console.ReadLine();
        }
		
        public void Run()
        {
            router = new MessageRouter();
            server = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp);
            server.Bind(new IPEndPoint(IPAddress.Loopback, port));

            for (int i=0; i<5; i++)
            {
                BeginReceive();
            }
        }

        private void BeginReceive()
        {
            byte[] buffer = new byte[4096];
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

                Task.Factory.StartNew(() => {
                    MonoMessage monoMessage = ByteArrayToMonoMessage(localMsg);
                    monoMessage.entity = router.Route(monoMessage.klass, monoMessage.entity);
                    byte[] bytes = MonoMessageToByteArray(monoMessage);
                    server.BeginSendTo(bytes, 0, bytes.Length, SocketFlags.None, packet.ep, new AsyncCallback(Sent), packet.ep);
                });

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

        public static MonoMessage ByteArrayToMonoMessage(byte[] bytes)
        {
            MonoMessage monoMessage;
            MemoryStream stream = new MemoryStream(bytes);
            monoMessage = Serializer.Deserialize<MonoMessage>(stream);
            return monoMessage;
        }
        
        public static byte[] MonoMessageToByteArray(MonoMessage monoMessage)
        {
            byte[] data;
            MemoryStream stream = new MemoryStream();
            Serializer.Serialize(stream, monoMessage);
            data = stream.ToArray();
            return data;
        }
    }
}
