using UnityEngine;
using System;
using System.Collections;
using  System.Collections.Generic;
using  System.Text.RegularExpressions;
using GameMachine;
using JoinChat = GameMachine.Messages.JoinChat;
using ChatChannel = GameMachine.Messages.ChatChannel;
using ChatMessage = GameMachine.Messages.ChatMessage;
using ChatChannels = GameMachine.Messages.ChatChannels;
using ClientMessage = GameMachine.Messages.ClientMessage;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{

    public class ChatManager : UntypedActor
    {
	
        private string username;
        private bool joinedServerChat = false;
        List<string> subscriptions = new List<string>();
	
        public ChatManager(string _username)
        {
            username = _username;
        }
        void sendChatMessage(string channelName, string messageText, string messageType)
        {
            Entity entity = new Entity();
            entity.id = "chatmessage";
		
            ChatMessage chatMessage = new ChatMessage();
            ChatChannel chatChannel = new ChatChannel();
            chatChannel.name = channelName;
            chatMessage.chatChannel = chatChannel;
            chatMessage.message = messageText;
            chatMessage.type = messageType;
            chatMessage.senderId = username;
            entity.chatMessage = chatMessage;
            //gameClient.sendMessage(clientMessage);
        }
	
        void joinChannel(string channelName)
        {
            if (subscriptions.Contains(channelName))
            {
                return;
            }
		
            Entity entity = new Entity();
            entity.id = "chatjoin";
            JoinChat joinChat = new JoinChat();
            ChatChannel chatChannel = new ChatChannel();
            chatChannel.name = channelName;
            joinChat.chatChannel.Add(chatChannel);
            entity.joinChat = joinChat;
            //clientMessage.entity.Add(entity);
            //gameClient.sendMessage(clientMessage);
            subscriptions.Add(channelName);
        }
	
        private void doChatCommand(string command)
        {
            List<string> words;
            string messageType = null;
		
            if (command.StartsWith("/tell"))
            {
                command = command.Replace("/tell", "");
                messageType = "private";
            } else if (command.StartsWith("/"))
            {
                command = command.Replace("/", "");
                messageType = "group";
            }
            Debug.Log(command);
            words = new List<string>(command.Split(" ".ToCharArray(), StringSplitOptions.RemoveEmptyEntries));
            string channelName = words [0];
            Debug.Log(channelName);
            command = command.Replace(channelName, "");
            string message = command;
				
            if (!subscriptions.Contains(channelName))
            {
                joinChannel(channelName);
            }
            string logMessage = string.Format("channel={0} message={1} messageType={2}", channelName, message, messageType);
            Debug.Log(logMessage);
            sendChatMessage(channelName, message, messageType);
        }
	
        public void chatCommand(string message)
        {
            doChatCommand(message);
        }
	
        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;

        }
        public void receiveChatMessage(ChatMessage msg)
        {
            if (msg.senderId == username)
            {
                return;
            }
            //GameObject.Find("ChatBox").GetComponent("FPSChat").
            //SendMessage("receiveMessage", msg.message);
        }
	
	
    }
}
