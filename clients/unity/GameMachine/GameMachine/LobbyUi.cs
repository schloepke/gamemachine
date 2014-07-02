using UnityEngine;
using System.Collections;
using  System.Collections.Generic;

namespace GameMachine
{
	public class LobbyUi : MonoBehaviour
	{
		public int windowId = 0;
		private Rect groupWindow;
		private float windowLeft;
		private float windowHeight;
		private float windowWidth;
		private float windowTop;
		private List<string> members = new List<string> ();
		public string channelName;
		private Messenger messenger;

		public delegate void ChannelLeft (string name);
		public ChannelLeft channelLeft;
				
		public void OnChannelLeft (ChannelLeft callback)
		{
			channelLeft = callback;
		}

		public void AddMember (string member)
		{
			members.Add (member);
		}
		
		public void RemoveMember (string member)
		{
			members.Remove (member);
		}
		
		void OnGUI ()
		{
			GUI.contentColor = Color.green;
			GUI.backgroundColor = Color.black;
			if (windowId > 0) {
				groupWindow = GUILayout.Window (windowId, groupWindow, WindowFunction, "Lobby", GUILayout.Width (windowWidth));
			}
		}
		
		void WindowFunction (int windowID)
		{
			foreach (string member in members) {
				
				GUILayout.Label (member);
			}
			GUILayout.Label ("");
			if (GUILayout.Button ("Leave Lobby")) {
				messenger.leaveChannel (channelName);
				if (channelLeft != null) {
					channelLeft (name);
				}
				Destroy (this.gameObject);
			}
			
			GUI.DragWindow ();
			
			
			
		}
		
		void UpdateMembers ()
		{
			members.Clear ();
			if (messenger.subscribers.ContainsKey (channelName)) {
				foreach (string member in messenger.subscribers[channelName]) {
					Logger.Debug ("AddMember " + member);
					AddMember (member);
				}
			}
		}
		
		void Start ()
		{
			windowId = 10100;

			windowWidth = 600;
			windowHeight = 300;
			windowLeft = 10;
			windowTop = 220;
			groupWindow = new Rect (windowLeft, windowTop, windowWidth, windowHeight);
			messenger = ActorSystem.Instance.Find ("Messenger") as Messenger;
			InvokeRepeating ("UpdateMembers", 0.01f, 1.0F);
		}
		
		void Update ()
		{
			
		}
	}
}

