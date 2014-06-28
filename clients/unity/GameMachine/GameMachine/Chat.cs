using UnityEngine;
using System.Collections;
using ChatMessage = GameMachine.Messages.ChatMessage;
using ChatInvite = GameMachine.Messages.ChatInvite;
using GameMachine;

// This class loads a UI component and sets up the callbacks to tie the UI to the messaging system.

// Note that the chat system is built using our messaging system, which uses a publish/subscribe model.  This makes it
// simple to use not just for chat but for general grouping amd matchmaking as well. 

// When joining and leaving channels, the server will send you a complete list of the channels you are subscribed to.
//  This can be accessed by calling messenger.subscriptions.  If you have set the flags for the channel to return
// subscribers (see below), the update from the server will include a complete subscriber list for each channel.
// You can access the subscribers list by calling messenger.subscribers, which returns a dictionary where the key
// is the channel name, and the value is a list of subscribers.

//  You can use the messenger system to create matchmaking systems or for groups.  It is designed for any kind of
// group based messeging that you need.  Chat messages are also not restricted to just text.  You can create chat
// messages with an attached entity, that can contain anything an entity can hold, which is literally anything.
// To send advanced messages use the SendChatMessage function instead of SendMessage. 



// To replace the UI with you own you will need to provide your own UI game object with
// a method for receiving and sending messages.  This example uses a javascript ui, so we have to use
// the SendMessage functionality to pass messages back and forth.

namespace GameMachine
{
    public class Chat : MonoBehaviour
    {
        private Chatbox chatbox;
        private Messenger messenger;
        private ChatCommands chatCommands;
        private GroupUi groupUi;

        void Start()
        {
            // Gui component.
            chatbox = this.gameObject.AddComponent<Chatbox>() as Chatbox;

            // The messaging actor
            messenger = ActorSystem.Instance.Find("Messenger") as Messenger;

            // Parses the chat language used by the gui and calls messenger
            // Feel free to provide your own implementation, this is mostly a starting point.
            // Supported syntax is documented in the source
            chatCommands = new ChatCommands(messenger);

            // Setup callacks so we get notified when we join/leave channels and receive messages
            Messenger.ChannelJoined channelCallback = ChannelJoined;
            messenger.OnChannelJoined(channelCallback);

            Messenger.ChannelLeft channelLeftCallback = ChannelLeft;
            messenger.OnChannelLeft(channelLeftCallback);
        
            Messenger.MessageReceived messageCallback = MessageReceived;
            messenger.OnMessageReceived(messageCallback);

            Messenger.InviteReceived inviteCallback = InviteReceived;
            messenger.OnInviteReceived(inviteCallback);

            // Join an initial channel.  The second argument is a flags string
            // that sets certain flags on a channel.  Currently 'subscribers' is the
            // one valid option.  If subscribers is set, status updates from the server
            // will include a complete subscriber list for each channel.
            messenger.JoinChannel("global", "subscribers");

            // Send this whenever you want a list of subscribed channels, and the optional
            // subscriber list if you have set the subscribers flag.  Remember you get this
            // automatically when you join/leave a channel.
            InvokeRepeating("UpdateChatStatus", 0.01f, 5.0F);

        }
	
        private void UpdateChatStatus()
        {
            messenger.ChatStatus();
        }
        public void InviteReceived(object message)
        {
            ChatInvite chatInvite = message as ChatInvite;
            messenger.JoinChannel(chatInvite.channelName, chatInvite.invite_id);
        }

        public void ChannelLeft(string channelName)
        {
            Logger.Debug("Left " + channelName);
            chatbox.AddMessage("yellow|You have left " + channelName);
            if (channelName.StartsWith("priv_"))
            {
                groupUi = this.gameObject.GetComponent<GameMachine.GroupUi>() as GroupUi;
                if (groupUi != null)
                {
                    if (groupUi.channelName == channelName)
                    {
                        Destroy(groupUi);
                    }
                }
            }
        }

        public void ChannelJoined(string channelName)
        {
            Logger.Debug("Joined " + channelName);


            if (channelName.StartsWith("priv_"))
            {
                groupUi = this.gameObject.GetComponent<GameMachine.GroupUi>() as GroupUi;
                if (groupUi != null)
                {
                    Destroy(groupUi);
                }
                groupUi = this.gameObject.AddComponent<GameMachine.GroupUi>() as GroupUi;
                groupUi.Join(messenger, channelName);
                chatCommands.currentGroup = channelName;
                chatbox.AddMessage("yellow|You have joined a group");
            } else
            {
                chatbox.AddMessage("yellow|You have joined " + channelName);
            }

        }

        // This is our callback function.  Messenger will send us
        // ChatMessage objects.  We also use this for our chat commands
        // handler, that will send error messages to this function to 
        // be send to the gui. (If we had a C# chat gui this would be cleaner).
        public void MessageReceived(object message)
        {
            string text;
            string color = "white";
            string name = message.GetType().Name;
            Logger.Debug("MessageRecieved type " + name);
            if (name == "String")
            {
                text = message as string;
            } else
            {
                ChatMessage chatMessage = message as ChatMessage;
                string channelName = chatMessage.chatChannel.name;
                text = chatMessage.senderId + ": " + chatMessage.message;

                if (chatMessage.type == "group")
                {
                    color = "green";
                } else
                {
                    color = "white";
                }
                text = color + "|" + text;
            }

            //Logger.Debug("Chat message " + text);
            chatbox.AddMessage(text);
        }

        public void SendChatMessage(string message)
        {
            chatCommands.process(User.Instance.username, message);
        }

        void Update()
        {
        
        }
    }
}
