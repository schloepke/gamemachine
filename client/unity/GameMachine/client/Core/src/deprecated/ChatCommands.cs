using System;
using UnityEngine;
using System.Collections;
using  System.Collections.Generic;
using System.Linq;
using  System.Text.RegularExpressions;
using GameMachine;
using GameMachine.Core;

namespace GameMachine.Chat
{
	// Simple chat command parsing that understands the following syntax
	//  /tell <player id> <message string> -> Send message to specific player
	// /<channel characterId> <message string> -> Send message to channel
	// /join <channel characterId> -> Joins a channel
	// /leave <channel characterId> -> Leave a channel


	public class ChatCommands
	{

        
		private Messenger messenger;
		private Chatbox chatbox;
		public string groupName;
        private ChatApi chatApi;

		public ChatCommands (Messenger _messenger, Chatbox chatbox)
		{
			messenger = _messenger;
			this.chatbox = chatbox;
		}

        public void SetChatApi(ChatApi chatApi) {
            this.chatApi = chatApi;
            groupName = "priv_" + chatApi.ChatUser() + "_group";
        }

		public void process (string senderId, string command)
		{
			List<string> words;
			string messageType = null;
            
			if (command.StartsWith ("/speed")) {
				command = command.Replace ("/speed", "");
				int speed = Int32.Parse (command);
				if (speed > 40) {
					return;
				}
                chatApi.Speed(speed);
				
			} else if (command.StartsWith ("/stuck")) {
                chatApi.Stuck();
                return;
			} else if (command.StartsWith ("/territories")) {
                chatApi.Territories();
				return;
			} else if (command.StartsWith ("/guild_create")) {
				command = command.Replace ("/guild_create", "").Trim ();
				string guildId = Messenger.SanitizeChannelName (command);
                chatApi.GuildCreate(guildId, command);
				return;
			} else if (command.StartsWith ("/guild_destroy")) {
				command = command.Replace ("/guild_destroy", "").Trim ();
                chatApi.GuildDestroy();
				
				return;
			} else if (command.StartsWith ("/guild_invite")) {
				command = command.Replace ("/guild_invite", "").Trim ();
                chatApi.GuildInvite(command);
				
				return;
			} else if (command.StartsWith ("/guild_leave")) {
				command = command.Replace ("/guild_leave", "").Trim ();
                chatApi.GuildLeave();
				
				return;
			} else if (command.StartsWith ("/guild_members")) {
				command = command.Replace ("/guild_members", "").Trim ();
                chatApi.GuildMembers();
				
				return;
			} else if (command.StartsWith ("/guild_list")) {
				command = command.Replace ("/guild_list", "").Trim ();
                chatApi.GuildList();
				
				return;
			} else if (command.StartsWith ("/guild")) {
				command = command.Replace ("/guild", "");
				messageType = "group";
				messenger.SendText (senderId, chatApi.GuildName(), command, messageType);
				return;
			} else if (command.StartsWith ("/tell")) {
				command = command.Replace ("/tell", "");
				messageType = "private";
			} else if (command.StartsWith ("/join")) {
				command = command.Replace ("/join", "");
				string channel = Messenger.SanitizeChannelName (command);
				if (chatApi.IsGuildChannel (channel)) {
					Logger.Debug ("Attempt to join guild channel " + channel);
					return;
				}
				messenger.JoinChannel (channel);
				return;
			} else if (command.StartsWith ("/leave")) {
				command = command.Replace ("/leave", "");
				messenger.leaveChannel (Messenger.SanitizeChannelName (command));
				return;
			} else if (command.StartsWith ("/channels")) {
				foreach (string channel in messenger.channelSubscriptions) {
					messenger.messageReceived (channel);
				}
				return;
			} else if (command.StartsWith ("/group_create")) {
				messenger.JoinChannel (groupName);
				return;
			} else if (command.StartsWith ("/group")) {
				string msg = command.Replace ("/group", "");
				messenger.SendText (senderId, ChatManagerOld.currentGroup, msg, "group");
				return;
			} else if (command.StartsWith ("/invite")) {
				command = command.Replace ("/invite", "");
				words = new List<string> (command.Split (" ".ToCharArray (), StringSplitOptions.RemoveEmptyEntries));
				string invitee = words [0];
                chatApi.InviteToChannel(invitee, groupName);
				
				return;
			} else if (command.StartsWith ("/help")) {
				string[] commandlist = new string[] {
                    "Valid commands:\n",
                    "/tell [recipient] [message] - send private message",
                    "/join [channel] - join channel",
                    "/leave [channel] - leave channel",
                    "/channels - show channels you are subscribed to",
                    "/group_create - create group",
                    "/group [message] - send group message",
                    "/invite [player] - invite to private group",
                    "/help - this message",
					"/guild_create [guild characterId] - Create a guild",
					"/guild_destroy - destroys your guild (must be owner)",
					"/guild_invite [player] - invite player to guild",
					"/guild_leave - leave your guild.  If owner, destroys it",
					"/guild [message] - send message to guild",
					"/guild_list - show list of all guilds",
					"/guild_members - show members of your guild"
                };
				chatbox.AddMessage ("yellow", String.Join ("\n", commandlist));
				return;
			} else if (command.StartsWith ("/")) {
				command = command.Replace ("/", "");
				messageType = "group";
			}

			words = new List<string> (command.Split (" ".ToCharArray (), StringSplitOptions.RemoveEmptyEntries));
			string channelName = words [0];
			command = command.Replace (channelName, "");
			string message = command;
            
			string logMessage = string.Format ("channel={0} message={1} messageType={2}", channelName, message, messageType);
			Logger.Debug (logMessage);

			if (message.Length == 0) {
				chatbox.AddMessage ("red", "Invalid command");
				return;
			}

			if (messageType == "group" && !messenger.channelSubscriptions.Contains (channelName)) {
				chatbox.AddMessage ("red", "You are not subscribed to " + channelName);
				return;
			}
            
			if (chatApi.IsGuildChannel (channelName)) {
				Logger.Debug ("Attempt to send message to other guild " + channelName);
				return;
			}

			messenger.SendText (senderId, channelName, message, messageType);
		}
	}
}
