using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using System.Text.RegularExpressions;
using ChatMessage = io.gamemachine.messages.ChatMessage;
using ChatInvite = io.gamemachine.messages.ChatInvite;
using GameMachine;
using GameMachine.Core;


// This class loads a UI component and sets up the callbacks to tie the UI to the messaging system.

// Note that the chat system is built using our messaging system, which uses a publish/subscribe model.  This makes it
// simple to use not just for chat but for general grouping amd matchmaking as well. 

// When joining and leaving channels, the server will send you a complete list of the channels you are subscribed to.
//  This can be accessed by calling messenger.subscriptions.  If you have set the flags for the channel to return
// subscribers (see below), the update from the server will include a complete subscriber list for each channel.
// You can access the subscribers list by calling messenger.subscribers, which returns a dictionary where the key
// is the channel name, and the value is a list of subscribers.

// Basic workflow

// Incoming messages get routed to messenger.  Messenger calls our callbacks in this class for new messages as well as when joining/leaving
// a channel and when we get an invite to a private channel.
// This class does some formatting on the message and then sends it to Chatbox for display.

// Outgoing messages go straight from Chatbox to messenger, which sends them to the server.

// Note that channels you are subscribed to persist when you logout by default.  You can call messenger.LeaveAllChannels()
// to unsubscribe from all channels.  We currently do not do that anywhere.

// Note on attaching arbitrary data to chat messages.  Yes you can do that, chat messages can have an entity attached that can contain anything.
// No you should probably not do that.  Just because someone is in a channel with you does not mean that channel is the right mechanism for obtaining
// other information about that user.  For example, the combat system sends combat updates that include health for every player in range if they get hit.
//  That is where you should get health of players.  The ability to pass extra information via chat messages was primarily put there for P2P data.  The chat system
// is simply not designed for handling things like low latency updates between players.  For that case see the extra info that can be passed via entity tracking, that
// is generally the right way to distribute small pieces of extra player data, as that information is already being updated 10 times a second, and it is region based so
// performance will be much better.

namespace GameMachine.Chat
{
	public class ChatManagerOld : MonoBehaviour
	{
		public Chatbox chatbox;
		private Messenger messenger;
		private ChatCommands chatCommands;
        private ChatApi chatApi;
		public static string currentGroup;
		public static string currentTeam;

        public static ChatManagerOld Create(GameObject go, ChatApi chatApi) {
            ChatManagerOld chatManager = go.AddComponent<ChatManagerOld>();
            chatManager.Init(chatApi);
            return chatManager;
        }

		void Start ()
		{
			

		}

        public void Init(ChatApi chatApi) {
            this.chatApi = chatApi;
            // Gui component.
            chatbox = this.gameObject.AddComponent<Chatbox>() as Chatbox;
            chatbox.CloseChatWindow();

            // The messaging actor
            messenger = ActorSystem.Instance.Find("Messenger") as Messenger;

            // Parses the chat language used by the gui and calls messenger
            // Feel free to provide your own implementation, this is mostly a starting point.
            // Supported syntax is documented in the source
            chatCommands = new ChatCommands(messenger, chatbox);
            chatCommands.SetChatApi(chatApi);

            // Setup callacks so we get notified when we join/leave channels and receive messages
            Messenger.ChannelJoined channelCallback = ChannelJoined;
            messenger.OnChannelJoined(channelCallback);

            Messenger.ChannelLeft channelLeftCallback = ChannelLeft;
            messenger.OnChannelLeft(channelLeftCallback);

            Messenger.MessageReceived messageCallback = MessageReceived;
            messenger.OnMessageReceived(messageCallback);

            Messenger.InviteReceived inviteCallback = InviteReceived;
            messenger.OnInviteReceived(inviteCallback);

            // Send this whenever you want a list of subscribed channels, and the optional
            // subscriber list if you have set the subscribers flag.  We do it on an interval
            // so that you get notified when new players join a group you are in.
            // For matchmaking you probably want this value lower so it appears more
            // responsive.  For mmo type games it could be somewhat higher.
            InvokeRepeating("UpdateChatStatus", 0.01f, 5.0F);
        }

		private void UpdateChatStatus ()
		{
			messenger.ChatStatus ();
		}

		public void InviteReceived (object message)
		{
			ChatInvite chatInvite = message as ChatInvite;
			messenger.JoinChannel (chatInvite.channelName, chatInvite.invite_id);
		}

		public void ChannelLeft (string channelName)
		{
			if (channelName.StartsWith ("priv_")) {
				if (channelName.EndsWith ("group")) {
					LeaveGroup (channelName);
				}
			} else {
				chatbox.AddMessage ("yellow", "You have left channel " + channelName);
			}
		}

		public void LeaveGroup (string channelName)
		{
			chatbox.AddMessage ("white", "You have left your group");
		}


		public void CreateGroup (string channelName)
		{
			ChannelUi.DestroyChannelUi (channelName);
			ChannelUi.CreateChannelUi (chatApi,this.gameObject, channelName, "Group");
			currentGroup = channelName;
			chatbox.AddMessage ("white", "You have joined a group");
		}

		public void ChannelJoined (string channelName)
		{
			// private message
			if (channelName.StartsWith ("priv_")) {
				// private group
				if (channelName.EndsWith ("group")) {
					CreateGroup (channelName);
				}
			} else {
				chatbox.AddMessage ("white", "You have joined channel " + channelName);
			}

		}

		// This is our callback function.  Messenger will send us
		// ChatMessage objects.  We also use this for our chat commands
		// handler, that will send error messages to this function to 
		// be send to the gui.
		public void MessageReceived (object message)
		{
			string text;
			string color = "white";
			string channel = "local";
			string name = message.GetType ().Name;
			if (name == "String") {
				text = message as string;
			} else {
				ChatMessage chatMessage = message as ChatMessage;
				string channelName = chatMessage.chatChannel.name;
				text = chatMessage.senderId + ": " + chatMessage.message;

				if (chatMessage.type == "group") {
					if (chatMessage.chatChannel.name.StartsWith ("priv_")) {
						color = "magenta";
						channel = "group";
					} else {
						color = "green";
						channel = channelName;
					}

				} else {
					color = "white";
				}
				text = "|" + "[" + channel + "] " + text;
			}

			Logger.Debug ("color: " + color + " text: " + text);
			chatbox.AddMessage (color, text);
		}

		public void SendChatMessage (string message)
		{
			chatCommands.process (User.Instance.username, message);
		}

		void Update ()
		{
        
		}
	}
}
