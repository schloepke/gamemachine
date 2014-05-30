using System;
using System.Collections;
using  System.Collections.Generic;
using System.Linq;
using  System.Text.RegularExpressions;
using GameMachine;

namespace GameMachine
{
    // Simple chat command parsing that understands the following syntax
    //  /tell <player id> <message string> -> Send message to specific player
    // /<channel name> <message string> -> Send message to channel
    // /join <channel name> -> Joins a channel
    // /leave <channel name> -> Leave a channel


    public class ChatCommands
    {

        private Messenger messenger;

        public ChatCommands(Messenger _messenger)
        {
            messenger = _messenger;
        }
                       
        private string SanitizeChannelName(string str)
        {
            return Regex.Replace(str, @"\s+", "");
        }
        

        public void process(string senderId, string command)
        {
            List<string> words;
            string messageType = null;
            
            if (command.StartsWith("/tell"))
            {
                command = command.Replace("/tell", "");
                messageType = "private";
            } else if (command.StartsWith("/join"))
            {
                command = command.Replace("/join", "");
                messenger.joinChannel(SanitizeChannelName(command));
                return;
            } else if (command.StartsWith("/leave"))
            {
                command = command.Replace("/leave", "");
                messenger.leaveChannel(SanitizeChannelName(command));
                return;
            } else if (command.StartsWith("/"))
            {
                command = command.Replace("/", "");
                messageType = "group";
            }
            words = new List<string>(command.Split(" ".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
            string channelName = words [0];
            command = command.Replace(channelName, "");
            string message = command;
            
            string logMessage = string.Format("channel={0} message={1} messageType={2}", channelName, message, messageType);
            Logger.Debug(logMessage);
            
            if (messageType == "group" && !messenger.subscriptions.Contains(channelName))
            {
                messenger.messageReceived("You are not subscribed to " + channelName);
                return;
            }
            
            messenger.sendMessage(senderId, channelName, message, messageType);
        }
    }
}
