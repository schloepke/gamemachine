using System;
using System.Collections;
using  System.Collections.Generic;
using System.Linq;
using  System.Text.RegularExpressions;
using GameMachine;
using JoinChat = GameMachine.Messages.JoinChat;
using LeaveChat = GameMachine.Messages.LeaveChat;
using ChatChannel = GameMachine.Messages.ChatChannel;
using ChatMessage = GameMachine.Messages.ChatMessage;
using ChatChannels = GameMachine.Messages.ChatChannels;
using Entity = GameMachine.Messages.Entity;

namespace GameMachine
{
    public class Messenger : UntypedActor
    {
    
        public List<string> subscriptions = new List<string>();

        public delegate void MessageReceived(string message);
        public delegate void ChannelJoined(string channelName);
        public delegate void ChannelLeft(string channelName);

        private ChannelJoined channelJoined;
        private ChannelLeft channelLeft;
        public MessageReceived messageReceived;
    
        public Messenger()
        {
        }

        public void OnChannelLeft(ChannelLeft callback)
        {
            channelLeft = callback;
        }

        public void OnChannelJoined(ChannelJoined callback)
        {
            channelJoined = callback;
        }

        public void OnMessageReceived(MessageReceived callback)
        {
            messageReceived = callback;
        }

        public void sendMessage(string senderId, string channelName, string messageText, string messageType)
        {
            Entity entity = new Entity();
            entity.id = "chatmessage";
        
            ChatMessage chatMessage = new ChatMessage();
            ChatChannel chatChannel = new ChatChannel();
            chatChannel.name = channelName;
            chatMessage.chatChannel = chatChannel;
            chatMessage.message = messageText;
            chatMessage.type = messageType;
            chatMessage.senderId = senderId;
            entity.chatMessage = chatMessage;
            actorSystem.Find("/remote/default").Tell(entity);
        }
    
        public void joinChannel(string channelName)
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
            actorSystem.Find("/remote/default").Tell(entity);
        }
    
        public void leaveChannel(string channelName)
        {
           
            Entity entity = new Entity();
            entity.id = "chatjoin";
            LeaveChat leaveChat = new LeaveChat();
            ChatChannel chatChannel = new ChatChannel();
            chatChannel.name = channelName;
            leaveChat.chatChannel.Add(chatChannel);
            entity.leaveChat = leaveChat;
            actorSystem.Find("/remote/default").Tell(entity);
        }

        private void processChannels(ChatChannels chatChannels)
        {
            List<string> channels = new List<string>();
            foreach (ChatChannel chatChannel in chatChannels.chatChannel)
            {
                channels.Add(chatChannel.name);
            }

            foreach (string name in channels.Except(subscriptions))
            {
                subscriptions.Add(name);
                channelJoined(name);
            }

            List<string> channelsToRemove = subscriptions.Except(channels).ToList();
            foreach (string name in channelsToRemove)
            {
                subscriptions.Remove(name);
                channelLeft(name);
            }

        }

        public override void OnReceive(object message)
        {
            Entity entity = message as Entity;

            if (entity.chatChannels != null)
            {
                processChannels(entity.chatChannels);
            }

            if (entity.chatMessage != null)
            {
                messageReceived(entity.chatMessage.message);
            }

        }

    }
}
