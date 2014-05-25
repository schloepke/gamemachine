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

namespace GameMachine
{
	public class ProxyServer
	{

		static void Main(string[] args)
		{
			ProxyClient proxyClient = new ProxyClient();
			proxyClient.Start();

			Console.WriteLine("Press any key to exit.");
			Console.ReadLine();
		}

	}
}