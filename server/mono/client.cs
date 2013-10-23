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
			System.Net.ServicePointManager.SetTcpKeepAlive(true,90000,90000);
		 for (int i = 1; i <= 50000; i++)
        {
            Test ();
        }
	}
	
	public static void Test ()
	{
		
		using (var client = new HttpClient()) {
				
			var content = new FormUrlEncodedContent (new[] 
            {
                new KeyValuePair<string, string> ("test", "login")
            });
			//content.Headers.Add("Keep-Alive", "true");
			var response = client.GetAsync ("http://127.0.0.1/index.html");
			//var response = client.PostAsync ("http://127.0.0.1/doc/index.html", content);
			if (response.Result.IsSuccessStatusCode) {
				// by calling .Result you are performing a synchronous call
				var responseContent = response.Result.Content; 

				// by calling .Result you are synchronously reading the result
				string responseString = responseContent.ReadAsStringAsync ().Result;

				Console.Out.WriteLine (responseString);
			}
		}
			
	}
}
