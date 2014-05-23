using System.Net;
using System.Net.Sockets;
using System;
using System.Threading;
using System.Net.Http;
using  System.Linq;
using Nancy;
using Nancy.Hosting.Self;

class Server
{
	public static void Main (string[] args)
	{
		// initialize an instance of NancyHost (found in the Nancy.Hosting.Self package)
		var host = new NancyHost (new Uri ("http://127.0.0.1:8888"));    
		host.Start ();  // start hosting
 
		//Under mono if you deamonize a process a Console.ReadLine with cause an EOF 
		//so we need to block another way
		if (args.Any (s => s.Equals ("-d", StringComparison.CurrentCultureIgnoreCase))) {
			while (true)
				Thread.Sleep (10000000);	
		} else {
			Console.ReadKey ();    
		}
    	    
		host.Stop ();  // stop hosting
	}
}
