using System;
using System.Collections;
using  System.Collections.Generic;
using System.Linq;
using  System.Text.RegularExpressions;
using GameMachine;
using GameMachine.Core;
using JoinChat = GameMachine.Messages.JoinChat;
using LeaveChat = GameMachine.Messages.LeaveChat;
using ChatChannel = GameMachine.Messages.ChatChannel;
using ChatMessage = GameMachine.Messages.ChatMessage;
using ChatChannels = GameMachine.Messages.ChatChannels;
using ChatStatus = GameMachine.Messages.ChatStatus;
using ChatInvite = GameMachine.Messages.ChatInvite;
using JsonEntity = GameMachine.Messages.JsonEntity;
using DynamicMessage = GameMachine.Messages.DynamicMessage;

using Entity = GameMachine.Messages.Entity;

namespace GameMachine.Core
{
    public class Messenger : UntypedActor
    {
    
        public Dictionary<string, List<string>> subscribers = new Dictionary<string, List<string>> ();
        public List<string> channelSubscriptions = new List<string> ();
        public List<string> privateChannelSubscriptions = new List<string> ();
        

        public delegate void MessageReceived (object message);
        public delegate void DynamicMessageReceived (DynamicMessage message);
        private Dictionary<string, DynamicMessageReceived> dynamicMessageCallbacks = new Dictionary<string, DynamicMessageReceived> ();

        public delegate void InviteReceived (object message);
        public delegate void ChannelJoined (string channelName);
        public delegate void ChannelLeft (string channelName);

        private ChannelJoined channelJoined;
        private ChannelLeft channelLeft;
        public MessageReceived messageReceived;
        public InviteReceived inviteReceived;
    
        public Messenger ()
        {
        }

       
        public bool HasSubscription (string subscription)
        {
            if (channelSubscriptions.Contains (subscription)) {
                return true;
            } else {
                return false;
            }
        }

        public List<string> SubcribersFor (string subscription)
        {
            if (subscribers.ContainsKey (subscription)) {
                return subscribers [subscription];
            } else {
                return new List<string> ();
            }
        }

        public void OnChannelLeft (ChannelLeft callback)
        {
            channelLeft = callback;
        }

        public void OnChannelJoined (ChannelJoined callback)
        {
            channelJoined = callback;
        }

        public void OnMessageReceived (MessageReceived callback)
        {
            messageReceived = callback;
        }

        public void OnDynamicMessageReceived (String channelName, DynamicMessageReceived callback)
        {
            dynamicMessageCallbacks [channelName] = callback;
        }

        public void OnInviteReceived (InviteReceived callback)
        {
            inviteReceived = callback;
        }

        // Use this if you want to create your own ChatMessage.
        // This is useful if you want to embed other data besides text.
        //  Do this by attaching an entity to the ChatMessage.
        public void SendChatMessage (ChatMessage message)
        {
            Entity entity = new Entity ();
            entity.id = "chatmessage";
            entity.chatMessage = message;
            actorSystem.FindRemote ("default").Tell (entity);
        }

        public void SendDynamicMessage (string senderId, string channelName, object message, string messageType)
        {
            DynamicMessage dynamicMessage = DynamicMessageUtil.ToDynamicMessage (message);
            Entity entity = new Entity ();
            entity.id = "dm";
            
            ChatMessage chatMessage = new ChatMessage ();
            ChatChannel chatChannel = new ChatChannel ();
            chatChannel.name = channelName;
            chatMessage.chatChannel = chatChannel;
            chatMessage.message = "dynamic_message";
            chatMessage.type = messageType;
            chatMessage.senderId = senderId;

            chatMessage.dynamicMessage = dynamicMessage;
            
            entity.chatMessage = chatMessage;
            actorSystem.FindRemote ("default").Tell (entity);
        }

        // Sends a JsonModel
        public void SendJsonModel (string senderId, string channelName, JsonModel model, string messageType)
        {

            Entity entity = new Entity ();
            entity.id = "object";
			
            ChatMessage chatMessage = new ChatMessage ();
            ChatChannel chatChannel = new ChatChannel ();
            chatChannel.name = channelName;
            chatMessage.chatChannel = chatChannel;
            chatMessage.message = "object";
            chatMessage.type = messageType;
            chatMessage.senderId = senderId;

            Entity jsonEntity = JsonModel.ToJsonEntity (model);
            chatMessage.entity = jsonEntity;

            entity.chatMessage = chatMessage;
            actorSystem.FindRemote ("default").Tell (entity);
        }

        // senderId = sending player id
        // channelName = chat channel
        // messageText = chat text
        // messageType = group or private
        public void SendText (string senderId, string channelName, string messageText, string messageType)
        {
            Entity entity = new Entity ();
            entity.id = "chatmessage";
        
            ChatMessage chatMessage = new ChatMessage ();
            ChatChannel chatChannel = new ChatChannel ();
            chatChannel.name = channelName;
            chatMessage.chatChannel = chatChannel;
            chatMessage.message = messageText;
            chatMessage.type = messageType;
            chatMessage.senderId = senderId;
            entity.chatMessage = chatMessage;
            actorSystem.FindRemote ("default").Tell (entity);
        }
    
        public void InviteToChannel (string playerId, string invitee, string channelName)
        {
            ChatInvite chatInvite = new ChatInvite ();
            chatInvite.invitee = invitee;
            chatInvite.inviter = playerId;
            chatInvite.channelName = channelName;
            chatInvite.invite_id = "0";

            Entity entity = new Entity ();
            entity.id = "chatinvite";
            entity.chatInvite = chatInvite;
            actorSystem.FindRemote ("default").Tell (entity);
        }

        public void JoinChannel (string channelName)
        {
            JoinChannel (channelName, "subscribers", null);
        }

        public void JoinChannel (string channelName, string inviteId)
        {
            JoinChannel (channelName, "subscribers", inviteId);
        }

        // flags is a pipe separated list of strings.  Currently subscribers is the only flag recognized
        //  If the subscribers flag is set, the status updates from the server will include a list of subscribers
        // in each channel, instead of just the channel name.
        public void JoinChannel (string channelName, string flags, string inviteId)
        {
            if (channelSubscriptions.Contains (channelName)) {
                return;
            }
        
            Entity entity = new Entity ();
            entity.id = "chatjoin";
            JoinChat joinChat = new JoinChat ();
            ChatChannel chatChannel = new ChatChannel ();
            chatChannel.name = channelName;
            chatChannel.flags = flags;
            if (inviteId != null) {
                chatChannel.invite_id = inviteId;
            }
            joinChat.chatChannel.Add (chatChannel);
            entity.joinChat = joinChat;
            actorSystem.FindRemote ("default").Tell (entity);
        }
    
        public void leaveChannel (string channelName)
        {
           
            Entity entity = new Entity ();
            entity.id = "chatjoin";
            LeaveChat leaveChat = new LeaveChat ();
            ChatChannel chatChannel = new ChatChannel ();
            chatChannel.name = channelName;
            leaveChat.chatChannel.Add (chatChannel);
            entity.leaveChat = leaveChat;
            actorSystem.FindRemote ("default").Tell (entity);
            subscribers.Remove (channelName);
        }

        public void LeaveAllChannels ()
        {
            foreach (string channelName in channelSubscriptions) {
                leaveChannel (channelName);
            }
        }

        // Tells the chat system to send you a complete list of channels you are subscribed to.
        //  If you joined a channel with the subscribers flag set, you will also get back a full
        // list of subscribers for that channel.
        public void ChatStatus ()
        {
            Entity entity = new Entity ();
            entity.id = "chat_status";
            entity.chatStatus = new ChatStatus ();
            actorSystem.FindRemote ("default").Tell (entity);
        }

        private void processChannels (ChatChannels chatChannels)
        {
            List<string> channels = new List<string> ();
            foreach (ChatChannel chatChannel in chatChannels.chatChannel) {
                channels.Add (chatChannel.name);
                subscribers [chatChannel.name] = chatChannel.subscribers.subscriberId;
            }

            foreach (string name in channels.Except(channelSubscriptions)) {
                channelSubscriptions.Add (name);
                if (name.StartsWith ("priv_")) {
                    privateChannelSubscriptions.Add (name);
                }

                if (channelJoined != null) {
                    channelJoined (name);
                }
            }

            List<string> channelsToRemove = channelSubscriptions.Except (channels).ToList ();
            foreach (string name in channelsToRemove) {
                channelSubscriptions.Remove (name);
                if (name.StartsWith ("priv_")) {
                    privateChannelSubscriptions.Remove (name);
                }

                if (channelLeft != null) {
                    channelLeft (name);
                }
            }

        }

        public override void OnReceive (object message)
        {
            Entity entity = message as Entity;

            if (entity.chatInvite != null) {
                inviteReceived (entity.chatInvite);
            }

            if (entity.chatChannels != null) {
                processChannels (entity.chatChannels);
            }

            if (entity.chatMessage != null) {

                if (entity.chatMessage.message == "object") {
                    JsonEntity jsonEntity = entity.chatMessage.entity.jsonEntity;
                    JsonModel.OnReceive (jsonEntity.klass, jsonEntity.json);
                    return;
                }

                if (messageReceived != null) {
                    messageReceived (entity.chatMessage);
                }

                if (entity.chatMessage.message == "dynamic_message") {
                    string channelName = entity.chatMessage.chatChannel.name;
                    if (dynamicMessageCallbacks.ContainsKey (channelName)) {
                        dynamicMessageCallbacks [channelName] (entity.chatMessage.dynamicMessage);
                    }
                }

            }

        }

    }
}
