using System;
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using GameMachine;
using GameMachine.Core;
using GameMachine.Common;
using GameMachine.HttpApi;

namespace GameMachine.Chat {

    public class ChatCommandHandler : ICharacterApi {

        public enum CommandResult {
            Ok,
            InvalidCommand,
            NotSubscribed,
            NoPermission
        }

        private Messenger messenger;
        public string groupName;
        private IChatUI chatUI;
        private string playerId;
        private string requestedGroup;

        public ChatCommandHandler(Messenger messenger, IChatUI chatUI, string playerId) {
            this.messenger = messenger;
            this.chatUI = chatUI;
            this.playerId = playerId;
            groupName = "priv_" + playerId + "_group";
        }

        public CommandResult process(string command) {
            List<string> words;
            string messageType = null;

            if (command.StartsWith("/speed")) {
                command = command.Replace("/speed", "");
                int speed = Int32.Parse(command);
                //string characterId = GameEntityManager.GetPlayerEntity().GetCharacterId();
                IGameEntityController controller = GameEntityManager.GetPlayerEntity().GetGameEntityController();
                controller.SetRunSpeed(speed);
                return CommandResult.Ok;
            } else if (command.StartsWith("/zone")) {
                command = command.Replace("/zone", "").Trim();
                GameMachine.DefaultClient.Client.instance.currentZone = command;
                GameMachine.DefaultClient.Client.instance.setZone = true;
                return CommandResult.Ok;
            } else if (command.StartsWith("/count")) {
                chatUI.LocalMessage(Color.yellow, GameEntityManager.GameEntityCount().ToString());
                return CommandResult.Ok;
            } else if (command.StartsWith("/stuck")) {
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/territories")) {
               
                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_create")) {
                command = command.Replace("/guild_create", "").Trim();
                //string guildId = Messenger.SanitizeChannelName(command);
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_destroy")) {
                command = command.Replace("/guild_destroy", "").Trim();
                

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_invite")) {
                command = command.Replace("/guild_invite", "").Trim();
               

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_leave")) {
                command = command.Replace("/guild_leave", "").Trim();
               

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_members")) {
                command = command.Replace("/guild_members", "").Trim();
                

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild_list")) {
                command = command.Replace("/guild_list", "").Trim();
               

                return CommandResult.Ok;
            } else if (command.StartsWith("/guild")) {
                command = command.Replace("/guild", "");
                messageType = "group";
                //messenger.SendText(playerId, "guild name here", command, messageType);
               
                return CommandResult.Ok;
            } else if (command.StartsWith("/tell")) {
                command = command.Replace("/tell", "");
                messageType = "private";
            } else if (command.StartsWith("/join")) {
                command = command.Replace("/join", "");
                string channel = Messenger.SanitizeChannelName(command);
                if (IsGuildChannel(channel)) {
                    Debug.Log("Attempt to join guild channel " + channel);
                    return CommandResult.Ok;
                }
                    messenger.JoinChannel(channel);
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/leave")) {
                command = command.Replace("/leave", "");
                    messenger.leaveChannel(Messenger.SanitizeChannelName(command));
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/channels")) {
                    chatUI.RemoteChannelList(messenger.channelSubscriptions);
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/group_create")) {
                    messenger.JoinChannel(groupName);
                
                return CommandResult.Ok;
            } else if (command.StartsWith("/group")) {
                string msg = command.Replace("/group", "");
                    messenger.SendText(playerId, chatUI.CurrentGroup(), msg, "group");
               
                return CommandResult.Ok;
            } else if (command.StartsWith("/invite")) {
                command = command.Replace("/invite", "");
                words = new List<string>(command.Split(" ".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
                string characterId = words[0];
                InviteToChannel(characterId, groupName);

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
					"/guild_create [guild characterId] - Create a guild",
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

            if (IsGuildChannel(channelName)) {
                Debug.Log("Attempt to send message to other guild " + channelName);
                return CommandResult.NoPermission;
            }

                if (messageType == "private") {
                    channelName = "character_id__" + channelName;
                }
                messenger.SendText(playerId, channelName, message, messageType);
            
            return CommandResult.Ok;
        }

        private bool IsGuildChannel(string name) {
            return false;
        }

        private void InviteToChannel(string characterId, string channel) {
            Debug.Log("request character " + characterId);
            CharacterApi.instance.GetCharacter(null, characterId, this);
            requestedGroup = channel;
        }

        public void OnCharacterCreated(io.gamemachine.messages.Character character) {
            throw new NotImplementedException();
        }

        public void OnCharacterCreateError(string error) {
            throw new NotImplementedException();
        }

        public void OnCharacterSet(string result) {
            throw new NotImplementedException();
        }

        public void OnCharacterSetError(string error) {
            throw new NotImplementedException();
        }

        public void OnCharacterGet(string playerId, io.gamemachine.messages.Character character) {
            Messenger messenger = ActorSystem.instance.Find("Messenger") as Messenger;
            string myPlayerId = GameEntityManager.GetPlayerEntity().GetEntityId();
            messenger.InviteToChannel(myPlayerId, character.playerId, requestedGroup);
            Debug.Log("invite sent to " + character.playerId + " for channel " + requestedGroup);
        }

        public void OnCharacterGetError(string playerId, string characterId, string error) {
            throw new NotImplementedException();
        }

        public void OnCharacterDeleted(string characterId) {
            throw new NotImplementedException();
        }

        public void OnCharacterDeleteError(string error) {
            throw new NotImplementedException();
        }
    }
}
