using System.Net;
using System.Net.Sockets;
using System;
using System.Text;
using System.Collections.Generic;
using System.IO;
using System.Net.Http;

public class Client
{
	
	public static void Main (string[] args)
	{
		//System.Net.ServicePointManager.SetTcpKeepAlive (true, 90000, 90000);
		
		Get ();
	}
	
	public static void Get ()
	{
		for (int i = 1; i <= 1000; i++) {
			
			HttpWebRequest request = (HttpWebRequest)WebRequest.Create ("http://192.168.1.6/doc/index.html");
			ServicePoint sp = request.ServicePoint;
			//sp.SetTcpKeepAlive(true, 6000000, 100000);
			request.KeepAlive = false;
			
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
	}
	
	public static void Test ()
	{
		
		HttpClient client = new HttpClient ();
		client.BaseAddress = new Uri ("http://192.168.1.6/doc/index.html");
		var content = new FormUrlEncodedContent (new[] 
            {
                new KeyValuePair<string, string> ("test", "login")
            });
		//content.Headers.Add("Keep-Alive", "true");
		for (int i = 1; i <= 5000000; i++) {
			var response = client.GetAsync ("");
			//var response = client.PostAsync ("http://127.0.0.1/doc/index.html", content);
			if (response.Result.IsSuccessStatusCode) {
				// by calling .Result you are performing a synchronous call
				var responseContent = response.Result.Content; 

				// by calling .Result you are synchronously reading the result
				string responseString = responseContent.ReadAsStringAsync ().Result;

				//Console.Out.WriteLine (responseString);
			}
		}
			
	}
}
