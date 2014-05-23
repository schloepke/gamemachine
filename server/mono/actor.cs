using System.Net;
using System.Net.Sockets;
using System;
using  ProtoBuf;
using System.Runtime.InteropServices;
using System.Text;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using System.Net.Http;
using System.Configuration;
using System.Net.Configuration;
using Entity = com.game_machine.entity_system.generated.Entity;
using MessageEnvelope = com.game_machine.entity_system.generated.MessageEnvelope;
using Rpc = com.game_machine.entity_system.generated.Rpc;
using Neighbors = com.game_machine.entity_system.generated.Neighbors;
using GameMachine;
using System.Collections.Concurrent;

namespace GameMachine
{
	public abstract class Actor : IActor
	{
		//private int udpPort = 4000;
		//private string udpHost = "127.0.0.1";
		//private UdpClient udpClient;
		//private IPEndPoint remote;
		private static Mutex mut = new Mutex ();
		public static ConcurrentDictionary<string,IActor> actors = new ConcurrentDictionary<string,IActor> ();
				
		public Actor ()
		{
			AppDomain domain = AppDomain.CurrentDomain;
			//Console.WriteLine ("Domain = " + domain.FriendlyName);
			//Console.WriteLine ("ApplicationBase = " + domain.SetupInformation.ApplicationBase);
			//Console.WriteLine ("ConfigurationFile = " + domain.SetupInformation.ConfigurationFile);
			//remote = new IPEndPoint (IPAddress.Parse (udpHost), udpPort);
			//udpClient = new UdpClient ();
			//udpClient.Connect (remote);
			//udpClient.Client.ReceiveTimeout = 20;
			string configKey = "system.net/connectionManagement";
			object cfg = ConfigurationManager.GetSection (configKey);
			if (cfg == null) {
				Console.WriteLine ("Net config is null");
			}
			Console.WriteLine ("Actor.new");
			System.Net.ServicePointManager.DefaultConnectionLimit = 1500;
			//ServicePointManager.MaxServicePointIdleTime = 900000;
			//System.Net.ServicePointManager.MaxServicePoints = 10;
			//System.Net.ServicePointManager.FindServicePoint(new Uri("http://localhost"));
			//ServicePointManager.Expect100Continue = false;
		}
		
		public static void ReceiveMessage (string id, string name_space, string klass, string str, int strlen)
		{
			try {
				if (str.Length != strlen) {
					Console.WriteLine (str.Length + " != " + strlen);
				}
				//Console.WriteLine (str);
				byte[] bytes = Convert.FromBase64String (str);
				//byte[] bytes2 = System.Text.Encoding.ASCII.GetBytes (str);
				byte[] bytes2 = new byte[str.Length * sizeof(char)];
				System.Buffer.BlockCopy (str.ToCharArray (), 0, bytes2, 0, bytes2.Length);
				int len = Buffer.ByteLength (bytes);
				//Console.WriteLine ("bytes len= "+len+" str len= "+str.Length);
				//Console.WriteLine (str);
				IActor actor;
				Entity entity = Actor.ByteArrayToEntity (bytes);
				if (Actor.actors.TryGetValue (id, out actor)) {
					actor.OnReceive (entity);
				} else {
					string typeName = name_space + "." + klass;
					//Console.WriteLine ("typeName=" + typeName);
					Type type = Type.GetType (typeName);
					if (type == null) {
						Console.WriteLine (typeName + " is null");
						return;
					}
					actor = Activator.CreateInstance (type) as IActor;
					if (Actor.actors.TryAdd (id, actor)) {
						actor.OnReceive (entity);
					} else {
						Console.WriteLine ("Unable to add actor " + id);
					}
				}
			} catch (Exception ex) {
				Console.WriteLine (ex);
			}
		}
		
		[DllImport("actor")]
		public static extern void callJava (string message, int len, StringBuilder result);
		
		public async void RpcAsync (Entity message)
		{
			byte[] data = Actor.EntityToByteArray (message);
			string encoded = Convert.ToBase64String (data);
			using (var client = new HttpClient()) {
				
				var content = new FormUrlEncodedContent (new[] 
            {
                new KeyValuePair<string, string> ("test", "login")
            });
				//content.Headers.Add("Keep-Alive", "true");
				var response = client.PostAsync ("http://127.0.0.1/rpc", content);
				if (response.Result.IsSuccessStatusCode) {
					// by calling .Result you are performing a synchronous call
					var responseContent = response.Result.Content; 

					// by calling .Result you are synchronously reading the result
					string responseString = responseContent.ReadAsStringAsync ().Result;

					Console.WriteLine (responseString);
				}
			}
			
		}
		
		public void RpcGet (Entity message)
		{
			byte[] data = Actor.EntityToByteArray (message);
			string encoded = Convert.ToBase64String (data);
			HttpWebRequest request = (HttpWebRequest)WebRequest.Create ("http://localhost/rpc");
			ServicePoint sp = request.ServicePoint;
			//sp.SetTcpKeepAlive(true, 6000000, 100000);
			request.KeepAlive = true;
			
			request.Method = "GET";
			
			HttpWebResponse response = (HttpWebResponse)request.GetResponse ();
			//Console.WriteLine (((HttpWebResponse)response).StatusDescription);
			Stream dataStream = response.GetResponseStream ();
			StreamReader reader = new StreamReader (dataStream);
			string responseFromServer = reader.ReadToEnd ();
			//Console.WriteLine (responseFromServer);
			reader.Close ();
			dataStream.Close ();
			response.Close ();
			int currentHashCode = sp.GetHashCode ();
			Console.WriteLine ("HashCode = " + currentHashCode);
			Console.WriteLine (request.KeepAlive);
			//ShowProperties (sp);
		}
		
		public void Rpc (Entity message)
		{
			byte[] data = Actor.EntityToByteArray (message);
			string encoded = Convert.ToBase64String (data);
			HttpWebRequest request = (HttpWebRequest)WebRequest.Create ("http://127.0.0.1/rpc");
			request.KeepAlive = true;
			
			//request.ProtocolVersion = HttpVersion.Version10;
			ServicePoint sp = request.ServicePoint;
			sp.SetTcpKeepAlive (true, 6000000, 100000);
			
			request.Method = "POST";
			//request.Connection = "keep-alive";
			string postData = "message=" + encoded;
			byte[] byteArray = Encoding.UTF8.GetBytes (postData);
			request.ContentType = "application/x-www-form-urlencoded";
			request.ContentLength = byteArray.Length;
			
			Stream dataStream = request.GetRequestStream ();
			dataStream.Write (byteArray, 0, byteArray.Length);
			dataStream.Close ();
			HttpWebResponse response = (HttpWebResponse)request.GetResponse ();
			//Console.WriteLine (((HttpWebResponse)response).StatusDescription);
			dataStream = response.GetResponseStream ();
			StreamReader reader = new StreamReader (dataStream);
			string responseFromServer = reader.ReadToEnd ();
			//Console.WriteLine (responseFromServer);
			reader.Close ();
			dataStream.Close ();
			response.Close ();
			int currentHashCode = sp.GetHashCode ();
			Console.WriteLine ("HashCode = " + currentHashCode);
			Console.WriteLine (request.KeepAlive);
			//ShowProperties (sp);
		}
		
		private void ShowProperties (ServicePoint sp)
		{

			// Display the ServicePoint Internet resource address.
			Console.WriteLine ("Address = {0} ", sp.Address.ToString ());

			// Display the date and time that the ServicePoint was last  
			// connected to a host.
			Console.WriteLine ("IdleSince = " + sp.IdleSince.ToString ());

			// Display the maximum length of time that the ServicePoint instance   
			// is allowed to maintain an idle connection to an Internet   
			// resource before it is recycled for use in another connection.
			Console.WriteLine ("MaxIdleTime = " + sp.MaxIdleTime);

			Console.WriteLine ("ConnectionName = " + sp.ConnectionName);

			// Display the maximum number of connections allowed on this  
			// ServicePoint instance.
			Console.WriteLine ("ConnectionLimit = " + sp.ConnectionLimit);

			// Display the number of connections associated with this  
			// ServicePoint instance.
			Console.WriteLine ("CurrentConnections = " + sp.CurrentConnections);

			if (sp.Certificate == null)
				Console.WriteLine ("Certificate = (null)");
			else
				Console.WriteLine ("Certificate = " + sp.Certificate.ToString ());

			if (sp.ClientCertificate == null)
				Console.WriteLine ("ClientCertificate = (null)");
			else
				Console.WriteLine ("ClientCertificate = " + sp.ClientCertificate.ToString ());

			Console.WriteLine ("ProtocolVersion = " + sp.ProtocolVersion.ToString ());
			Console.WriteLine ("SupportsPipelining = " + sp.SupportsPipelining);

			Console.WriteLine ("UseNagleAlgorithm = " + sp.UseNagleAlgorithm.ToString ());
			Console.WriteLine ("Expect 100-continue = " + sp.Expect100Continue.ToString ());
		}
//		public void Rpc3 ()
//		{
//			using (var client = new HttpClient(){BaseAddress = new Uri ("http://localhost:8080")}) {
//				//client.BaseAddress = new Uri ("http://localhost:8080");
//				var content = new FormUrlEncodedContent (new[] 
//            {
//                new KeyValuePair<string, string> ("test", "login")
//            });
//				var result = client.PostAsync ("rpc", content).Result;
//				string resultContent = result.Content.ReadAsStringAsync ().Result;
//				Console.WriteLine (resultContent);
//			}
//		}
		
		void Tell (string server, string name, string id, string type, Entity entity)
		{
			MessageEnvelope messageEnvelope = new MessageEnvelope ();
			messageEnvelope.name = name;
			entity.messageEnvelope = messageEnvelope;
			
			if (type == "r") {
				messageEnvelope.type = "r";
				messageEnvelope.server = server;
			} else if (type == "dl") {
				messageEnvelope.type = "dl";
				messageEnvelope.id = id;
			} else if (type == "d") {
				messageEnvelope.type = "d";
				messageEnvelope.id = id;
			} else if (type == "l") {
				messageEnvelope.type = "l";
			} else {
				throw new System.ArgumentException ("type is null or invalid", type);
			}
			
			byte[] bytes = EntityToByteArray (entity);
			//udpClient.Send (bytes, bytes.Length);
		}
		
		public void Tell (string name, Entity entity)
		{
			Tell (null, name, null, "l", entity);
		}
		
		public void TellRemote (string server, string name, Entity entity)
		{
			Tell (server, name, null, "r", entity);
		}
		
		public void TellDistributedLocal (string id, string name, Entity entity)
		{
			Tell (null, name, id, "dl", entity);
		}
		
		public void TellDistributed (string id, string name, Entity entity)
		{
			Tell (null, name, id, "d", entity);
		}
		
		public Neighbors GetNeighbors (float x, float z, string entityType="player")
		{
			try {
				Entity entity = new Entity ();
				entity.id = "0";
				entity.rpc = new Rpc ();
				entity.rpc.method = "neighbors";
				entity.rpc.arguments.Add (x.ToString ("N4"));
				entity.rpc.arguments.Add (z.ToString ("N4"));
				entity.rpc.arguments.Add (entityType);
				entity.rpc.returnValue = true;
				byte[] bytes = EntityToByteArray (entity);
				//udpClient.Send (bytes, bytes.Length);
				//bytes = udpClient.Receive (ref remote);
				entity = ByteArrayToEntity (bytes);
				return entity.neighbors;
			} catch (Exception ex) {
				Console.WriteLine (ex.Message);
				return null;
			}
		}
		
		public static Entity ByteArrayToEntity (byte[] bytes)
		{
			Entity entity;
			MemoryStream stream = new MemoryStream (bytes);
			entity = Serializer.Deserialize<Entity> (stream);
			return entity;
		}
		
		public static byte[] EntityToByteArray (Entity entity)
		{
			byte[] data;
			MemoryStream stream = new MemoryStream ();
			Serializer.Serialize (stream, entity);
			data = stream.ToArray ();
			return data;
		}
		
		public abstract void OnReceive (object message);
		
	}
}
