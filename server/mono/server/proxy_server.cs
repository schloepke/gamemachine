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

namespace GameMachine
{
	public class ProxyServer
	{

		public static Logger logger = LogManager.GetLogger("GameMachine");
		static void Main(string[] args)
		{
			ProxyClient proxyClient = new ProxyClient(Int32.Parse(args[0]));
			proxyClient.Start();
			
			ProxyServer.logger.Info("Proxy Starting");
			Console.WriteLine("Press any key to exit.");
			Console.ReadLine();
		}

	}
}
