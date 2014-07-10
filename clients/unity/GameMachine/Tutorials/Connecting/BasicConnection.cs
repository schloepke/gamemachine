using UnityEngine;
using System;
using GameMachine;


namespace GameMachine.Tutorials.Connecting
{
	// Example of the minimum required to setup a connection.
	// We use the simple Login ui that comes with Game Machine.
	// You can easily replace it with your own (LoginUi.cs)
	public class BasicConnection : MonoBehaviour, GameMachineApp
	{

		void Start ()
		{
			// Give the internal systems a reference to us so they can call us back for certain events
			Login.SetGameMachineApp (this);
		}

		// Called if authentication fails.
		public void OnLoginFailure (string error)
		{
			Debug.Log ("Login Failed: " + error);
		}

		// We get called here if the connection times out at some point.  Udp will just
		// keep trying forever.  Tcp will eventually time out for good if it gets too
		// many connection failed attempts
		public void ConnectionTimeout ()
		{
			Debug.Log ("Connection timed out");
		}

		// Called when a connection is established or re-established after a timeout
		public void ConnectionEstablished ()
		{
			Debug.Log ("Connection established");
		}

		// This is called when we have an initial connection and everything is started.  This is only called once
		public void OnLoggedIn ()
		{
		
			// Region connections  Connecting to a region will automatically handle disconnecting from whatever
			// regions you are currently connected to before establishing the new connection
			// Do not use the region client to connect to the same server as your primary connection. It will confuse
			// the node and you will most likely lose messages, or even stop receiving them altogether.

			// Connect directly to the specified hostname.  In this case the region is required but not used.
			// Use this with any custom methodology you might have for connecting clients to task specific nodes or clusters.
			//Login.regionClient.Connect ("zone", "192.168.1.8");

			// Connect to a named regoin that is decided by the server using the server side region system
			//Login.regionClient.Connect ("zone");
		}

				
	}
}
