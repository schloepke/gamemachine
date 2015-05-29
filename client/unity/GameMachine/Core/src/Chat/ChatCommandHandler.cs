using System;
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using GameMachine;
using GameMachine.Core;
using GameMachine.Common;

namespace GameMachine.Chat {
    // Simple chat command parsing that understands the following syntax
    //  /tell <player id> <message string> -> Send message to specific player
    // /<channel name> <message string> -> Send message to channel
    // /join <channel name> -> Joins a channel
    // /leave <channel name> -> Leave a channel


    public class ChatCommandHandler {

        public enum CommandResult {
            Ok,
            InvalidCommand,
            NotSubscribed,
            NoPermission
        }

        private Messenger messenger;
        public string groupName;
        private ChatApi chatApi;
        private IChatUI chatUI;
        private bool testmode;
        private string playerId;

        public ChatCommandHandler(Messenger messenger, IChatUI chatUI, ChatApi chatApi, string playerId,bool testmode) {
            this.messenger = messenger;
            this.chatUI = chatUI;
            this.chatApi = chatApi;
            this.playerId = playerId;
            groupName = "priv_" + playerId + "_group";
            this.testmode = testmode;
        }

        public CommandResult process(string command) {
            List<string> words;
            string messageType = null;

            if (command.StartsWith("/speed")) {
                command = command.Replace("/speed", "");
                int speed = Int32.Parse(command);
                if (speed > 40) {
                    return CommandResult.Ok;
                }
                chatApi.Speed(speed);
            } else if (command.StartsWith("/count")) {
                chatUI.LocalMessage(Color.yellow, GameEntityManager.GameEntityCount().ToString());
                return CommandResult.Ok;
            } else if (command.StartsWith("/stuck")) {
                chatApi.Stuck();
                return CommandResult.Ok;
            } else if (command.StartsWith("/territories")) {
                chatApi.Territories();
                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_create")) {
                command = command.Replace("/guild_create", "").Trim();
                string guildId = Messenger.SanitizeChannelName(command);
                chatApi.GuildCreate(guildId, command);
                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_destroy")) {
                command = command.Replace("/guild_destroy", "").Trim();
                chatApi.GuildDestroy();

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_invite")) {
                command = command.Replace("/guild_invite", "").Trim();
                chatApi.GuildInvite(command);

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_leave")) {
                command = command.Replace("/guild_leave", "").Trim();
                chatApi.GuildLeave();

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_members")) {
                command = command.Replace("/guild_members", "").Trim();
                chatApi.GuildMembers();

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_list")) {
                command = command.Replace("/guild_list", "").Trim();
                chatApi.GuildList();

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild")) {
                command = command.Replace("/guild", "");
                messageType = "group";
                if (!testmode) {
                    messenger.SendText(playerId, chatApi.GuildName(), command, messageType);
                }
               
                return CommandResult.Ok;
            } else if (command.StartsWith("/tell")) {
                command = command.Replace("/tell", "");
                messageType = "private";
            } else if (command.StartsWith("/join")) {
                command = command.Replace("/join", "");
                string channel = Messenger.SanitizeChannelName(command);
                if (chatApi.IsGuildChannel(channel)) {
                    Debug.Log("Attempt to join guild channel " + channel);
                    return CommandResult.Ok;
                }
                if (!testmode) {
                    messenger.JoinChannel(channel);
                }
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/leave")) {
                command = command.Replace("/leave", "");
                if (!testmode) {
                    messenger.leaveChannel(Messenger.SanitizeChannelName(command));
                }
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/channels")) {
                if (!testmode) {
                    foreach (string channel in messenger.channelSubscriptions) {
                        messenger.messageReceived(channel);
                    }
                }
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/group_create")) {
                if (!testmode) {
                    messenger.JoinChannel(groupName);
                }
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/group")) {
                string msg = command.Replace("/group", "");
                if (!testmode) {
                    messenger.SendText(playerId, ChatManagerOld.currentGroup, msg, "group");
                }
               
                return CommandResult.Ok;
            } else if (command.StartsWith("/invite")) {
                command = command.Replace("/invite", "");
                words = new List<string>(command.Split(" ".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
                string characterId = words[0];
                chatApi.InviteToChannel(characterId, groupName);
                //messenger.InviteToChannel(GameMachine.Core.User.Instance.username, character.playerId, chatInviteChannel);

                return CommandResult.Ok;
            } else if (command.StartsWith("/help")) {
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
					"/guild_create [guild name] - Create a guild",
					"/guild_destroy - destroys your guild (must be owner)",
					"/guild_invite [player] - invite player to guild",
					"/guild_leave - leave your guild.  If owner, destroys it",
					"/guild [message] - send message to guild",
					"/guild_list - show list of all guilds",
					"/guild_members - show members of your guild"
                };
                chatUI.LocalMessage(Color.yellow, String.Join("\n", commandlist));
                return CommandResult.Ok;
            } else if (command.StartsWith("/")) {
                command = command.Replace("/", "");
                messageType = "group";
            }

            words = new List<string>(command.Split(" ".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
            string channelName = words[0];
            command = command.Replace(channelName, "");
            string message = command;

            string logMessage = string.Format("channel={0} message={1} messageType={2}", channelName, message, messageType);
            Debug.Log(logMessage);

            if (message.Length == 0) {
                chatUI.LocalMessage(Color.red, "Invalid command");
                return CommandResult.InvalidCommand;
            }

            if (messageType == "group" && !messenger.channelSubscriptions.Contains(channelName)) {
                chatUI.LocalMessage(Color.red, "You are not subscribed to " + channelName);
                return CommandResult.NotSubscribed;
            }

            if (chatApi.IsGuildChannel(channelName)) {
                Debug.Log("Attempt to send message to other guild " + channelName);
                return CommandResult.NoPermission;
            }

            if (!testmode) {
                if (messageType == "private") {
                    channelName = "character_id__" + channelName;
                }
                messenger.SendText(playerId, channelName, message, messageType);
            }
            
            return CommandResult.Ok;
        }
    }
}
