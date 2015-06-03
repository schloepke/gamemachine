using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using GameMachine.Core;

namespace GameMachine.Chat
{
	public class ChannelUi : MonoBehaviour
	{
        private ChatApi chatApi;
		public int windowId = 0;
		private Rect groupWindow;
		private float windowLeft;
		private float windowHeight;
		private float windowWidth;
		private float windowTop;
		private List<string> members = new List<string> ();
		public string channelName;
		private Messenger messenger;
		private string uiType = "Group";
		public string title;
		public string buttonName;
		public static Dictionary<string, ChannelUi> channelUis = new Dictionary<string, ChannelUi> ();


		public static int nextWindowId = 1000;

		public delegate void ChannelLeft (string name);
		public ChannelLeft channelLeft;

		public static void DestroyChannelUi (string name)
		{
			if (channelUis.ContainsKey (name)) {
				ChannelUi channelUi = channelUis [name];
				if (channelUi != null) {
                    channelUi.chatApi.ChannelWindowClosed();
					Destroy (channelUi.gameObject);
					
				}
				
				channelUis.Remove (name);
			}
		}

		public static ChannelUi CreateChannelUi (ChatApi chatApi,GameObject gameObject, string channelName, string uiType)
		{
			DestroyChannelUi (channelName);

			GameObject uiObject = new GameObject (channelName);
			ChannelUi ui = uiObject.AddComponent<ChannelUi> () as ChannelUi;
            ui.chatApi = chatApi;
			ui.windowId = nextWindowId;
			nextWindowId += 1;
			ui.name = channelName;
			ui.channelName = channelName;
			ui.uiType = uiType;
			ui.title = uiType;
			ui.buttonName = "Leave " + ui.uiType;
			channelUis [channelName] = ui;
			ui.InvokeRepeating ("UpdateMembers", 0.01f, 1.0F);
            chatApi.ChannelWindowOpened();
			
			return ui;
		}

		public void OnChannelLeft (ChannelLeft callback)
		{
			channelLeft = callback;
		}

		public void SetType (string groupType)
		{
			this.uiType = groupType;
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
				groupWindow = GUILayout.Window (windowId, groupWindow, WindowFunction, title, GUILayout.Width (windowWidth));
			}
		}
        
		void WindowFunction (int windowID)
		{
			foreach (string member in members) {
					
				GUILayout.Label (member);
			}
			GUILayout.Label ("");
			if (GUILayout.Button (buttonName)) {
				messenger.leaveChannel (channelName);
				if (channelLeft != null) {
					channelLeft (name);
				}
				DestroyChannelUi (name);
			}
				
			GUI.DragWindow ();
			


		}

		void UpdateMembers ()
		{
			members.Clear ();
			if (messenger.subscribers.ContainsKey (channelName)) {
				foreach (string member in messenger.subscribers[channelName]) {
					AddMember (member);
				}
			}
		}

		void Start ()
		{
			windowWidth = 190;
			windowHeight = 50;
			windowLeft = Screen.width - 200;
			windowTop = 200;
			groupWindow = new Rect (windowLeft, windowTop, windowWidth, windowHeight);
			messenger = ActorSystem.instance.Find ("Messenger") as Messenger;
		}
        
		void Update ()
		{
            
		}
	}
}

