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
using Entity = com.game_machine.entity_system.generated.Entity;

namespace GameMachine
{
	public class ProxyClient
	{
		private IPEndPoint udp_ep;
		private UdpClient udpClient;

		public ProxyClient()
		{
		}

		public void Start()
		{
			udp_ep = new IPEndPoint (IPAddress.Any, 11000);
			udpClient = new UdpClient (udp_ep);
			receiveData ();
			
			Task.Factory.StartNew(() => {Ping(); });
		}

		private void Ping()
		{
			Entity entity = new Entity ();
			entity.id = "ping";
			byte[] bytes = MessageUtil.EntityToByteArray (entity);
			while (true) 
			{
				Thread.Sleep (1000);
				Send (bytes);
			}
		}

		private void SendCallback(IAsyncResult ar)
		{
			UdpClient u = (UdpClient)ar.AsyncState;
			u.EndSend(ar);
		}
		
		public void Send(byte[] bytes)
		{
			udpClient.BeginSend(bytes, bytes.Length, "127.0.0.1", 8800, new AsyncCallback(SendCallback), udpClient);
		}
		
		private void dataReady (IAsyncResult ar)
		{
			byte[] bytes = udpClient.EndReceive (ar, ref udp_ep);
			receiveData ();
			MessageRouter.Route(bytes,this);
			//Task.Factory.StartNew(() => {MessageRouter.Route(bytes,this); });
			//receiveData ();
		}
		
		private void receiveData ()
		{
			udpClient.BeginReceive (new AsyncCallback (dataReady), udp_ep);
		}
		
	}
}